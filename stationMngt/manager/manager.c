#include <stdio.h>
#include <string.h>

#include "system_state.h"
#include "setup.h"
#include "ui.h"
#include "logs.h"
#include <unistd.h>
#include <fcntl.h>
#include <termios.h>
#include "update_track.h"

int  last_temp_value = 0;
int  last_hum_value  = 0;
char last_temp_unit[32] = "N/A";
char last_hum_unit[32]  = "N/A";
int serial_open(const char *device);
void serial_close(void);
int serial_send(const char *cmd);
int serial_read(char *buf, int size);
void manager_control_signal(void);

User *current_user = NULL;

/* ASM encrypt */
extern int encrypt_data(char *, int, char *);

/* ASM extract */
extern int extract_data(
    const char *str,
        const char *token,
            char *unit,
                int *value
                );

static int find_track_index_by_id(int id) {
    for (int i = 0; i < num_tracks; i++)
        if (tracks[i].id == id) return i;
    return -1;
}

static void send_signal_to_track(const char *sig, int track_id) {
    char cmd[16];
    snprintf(cmd, sizeof(cmd), "%s,%d", sig, track_id);
    serial_send(cmd);
    sleep(1);
}


void manager_control_signal(void) {
    char cmd[64];

    if (!serial_open("/dev/ttyS0")) {
        printf("Failed to open serial port\n");
        return;
    }

    int c;
    while ((c = getchar()) != '\n' && c != EOF);

    printf("\n--- USAC14: Control Signals ---\n");
    printf("Type commands like: RE,1 | YE,2 | GE,1 | RB,2\n");
    printf("Type EXIT to return to menu\n");

    while (1) {
        printf("Signal> ");

        if (!fgets(cmd, sizeof(cmd), stdin))
            break;

        cmd[strcspn(cmd, "\n")] = '\0';

        if (strcasecmp(cmd, "EXIT") == 0)
            break;

        if (strlen(cmd) == 0)
            continue;

        serial_send(cmd);
        sleep(1);

        printf("Command sent: %s\n", cmd);

        char log_msg[96];
        snprintf(log_msg, sizeof(log_msg), "control_signal cmd=%s", cmd);
        write_log(current_user->username, log_msg);
    }

    serial_close();
}

void manager_manage_tracks(void) {
    int option;

    do {
        printf("\n--- USAC16: Manage Tracks ---\n");
        printf("1. Assign track to arriving train\n");
        printf("2. Set track as FREE\n");
        printf("3. Set track as INOPERATIVE\n");
        printf("4. Give departure order\n");
        printf("0. Back\n");
        printf("Option: ");
        scanf("%d", &option);

        switch (option) {
            case 1: usac16_assign_track(); break;
            case 2: usac16_set_free(); break;
            case 3: usac16_set_inoperative(); break;
            case 4: usac16_depart_train(); break;
        }
    } while (option != 0);
}

void usac16_set_free(void) {
    int id;
    printf("Track ID: ");
    scanf("%d", &id);

    int idx = find_track_index_by_id(id);
    if (idx < 0) { printf("Invalid track.\n"); return; }

    if (!serial_open("/dev/ttyS0")) { printf("Serial failed.\n"); return; }

    update_track(&tracks[idx], FREE, -1);     
    send_signal_to_track("GE", id);         
    write_log(current_user->username, "usac16 set_free");
    serial_close();
}



void usac16_set_inoperative(void) {
    int id;
    printf("Track ID: ");
    scanf("%d", &id);

    int idx = find_track_index_by_id(id);
    if (idx < 0) { printf("Invalid track.\n"); return; }

    if (!serial_open("/dev/ttyS0")) { printf("Serial failed.\n"); return; }

    update_track(&tracks[idx], INOPERATIVE, -1); 
    send_signal_to_track("RB", id);              

    write_log(current_user->username, "usac16 set_inoperative");
    serial_close();
}


void usac16_depart_train(void) {
    int id;
    printf("Track ID: ");
    scanf("%d", &id);

    int idx = find_track_index_by_id(id);
    if (idx < 0) { printf("Invalid track.\n"); return; }

    if (tracks[idx].state != BUSY) {
        printf("No stopped train on this track.\n");
        return;
    }

    if (!serial_open("/dev/ttyS0")) { printf("Serial failed.\n"); return; }

    update_track(&tracks[idx], FREE, -1);  
    send_signal_to_track("GE", id);       

    write_log(current_user->username, "usac16 departure_order");
    printf("Departure order given. Track %d is now FREE.\n", id);

    serial_close();
}

void usac16_assign_track(void) {
    int train_id, track_id;

    printf("Arriving Train ID: ");
    scanf("%d", &train_id);

    printf("Available FREE tracks:\n");
    for (int i = 0; i < num_tracks; i++) {
        if (tracks[i].state == FREE)
            printf(" - Track %d\n", tracks[i].id);
    }

    printf("Select Track ID: ");
    scanf("%d", &track_id);

    int idx = find_track_index_by_id(track_id);
    if (idx < 0 || tracks[idx].state != FREE) {
        printf("Invalid or unavailable track.\n");
        return;
    }

    if (!serial_open("/dev/ttyS0")) {
        printf("Serial failed.\n");
        return;
    }

    update_track(&tracks[idx], ASSIGNED, train_id);
    send_signal_to_track("RE", track_id);

    write_log(current_user->username, "usac16 assign_track");
    printf("Assigned Track %d to Train %d\n", track_id, train_id);

    serial_close();
}




                
/* -------- LOGIN -------- */
                                                    
static User *login(const char *username, const char *password) {
    char encrypted[50];

    for (int i = 0; i < num_users; i++) {
        if (strcmp(users[i].username, username) == 0) {

            if (!encrypt_data((char *)password,
                              users[i].cipher_key,
                              encrypted)) {
                return NULL;
            }

            if (strcmp(encrypted, users[i].password) == 0) {
                return &users[i];
            }
            return NULL;
        }
    }
    return NULL;
}

void manager_get_sensor_data(void) {
    char buffer[256];

        char temp_unit[32];
            char hum_unit[32];
                int temp_value = 0;
                    int hum_value  = 0;

                        if (!serial_open("/dev/ttyS0")) {
                                printf("Failed to open serial port\n");
                                        return;
                                            }

                                                serial_send("GTH");
                                                    sleep(1);

                                                        if (serial_read(buffer, sizeof(buffer)) <= 0) {
                                                                printf("No data received from Arduino\n");
                                                                        serial_close();
                                                                                return;
                                                                                    }

                                                                                        printf("\n[RAW DATA]\n%s\n", buffer);

                                                                                            /* ---- Extract TEMP ---- */
                                                                                                if (extract_data(buffer, "TEMP", temp_unit, &temp_value)) {
                                                                                                        printf("\n[PARSED DATA]\n");
                                                                                                                printf("Temperature: %d %s\n", temp_value, temp_unit);
                                                                                                                    } else {
                                                                                                                            printf("Failed to extract TEMP\n");
                                                                                                                                }

                                                                                                                                    /* ---- Extract HUM ---- */
                                                                                                                                        if (extract_data(buffer, "HUM", hum_unit, &hum_value)) {
                                                                                                                                                printf("Humidity: %d %s\n", hum_value, hum_unit);
                                                                                                                                                    } else {
                                                                                                                                                            printf("Failed to extract HUM\n");
                                                                                                                                                                }

last_temp_value = temp_value;
last_hum_value  = hum_value;
strcpy(last_temp_unit, temp_unit);
strcpy(last_hum_unit, hum_unit);
char log_msg[64];
snprintf(log_msg, sizeof(log_msg),
         "get_sensor_data TEMP=%d HUM=%d",
         temp_value, hum_value);

write_log(current_user->username, log_msg);
                                                                                                                                                               serial_close();
                                                                                                                                                                    }

static int serial_fd = -1;

int serial_open(const char *device) {
    struct termios tty;

    serial_fd = open(device, O_RDWR | O_NOCTTY);
    if (serial_fd < 0) {
        perror("open serial");
        return 0;
    }

    tcgetattr(serial_fd, &tty);

    cfsetispeed(&tty, B9600);
    cfsetospeed(&tty, B9600);

    tty.c_cflag = (tty.c_cflag & ~CSIZE) | CS8;
    tty.c_cflag |= CREAD | CLOCAL;
    tty.c_cflag &= ~PARENB;
    tty.c_cflag &= ~CSTOPB;
    tty.c_cflag &= ~CRTSCTS;

    tty.c_lflag = 0;
    tty.c_iflag = 0;
    tty.c_oflag = 0;

    tcsetattr(serial_fd, TCSANOW, &tty);

    return 1;
}

void serial_close(void) {
    if (serial_fd >= 0)
        close(serial_fd);
}

int serial_send(const char *cmd) {
    if (serial_fd < 0) return 0;

    write(serial_fd, cmd, strlen(cmd));
    write(serial_fd, "\n", 1);
    return 1;
}

int serial_read(char *buf, int size) {
    if (serial_fd < 0) return 0;

    int n = read(serial_fd, buf, size - 1);
    if (n > 0)
        buf[n] = '\0';

    return n;
}

void manager_show_board(void) {

write_log(current_user->username, "show_station_board");

    printf("\n");
    printf("+============================================================+\n");
    printf("|                                                            |\n");
    printf("|   ( )   ( )   ( )   ( )   ( )   ( )                        |\n");
    printf("|    ) (   ) (   ) (   ) (   ) (   )                         |\n");
    printf("|   (   ) (   ) (   ) (   ) (   ) (   )                      |\n");
    printf("|        ____     _________     _________                    |\n");
    printf("|  ____ |LO |____/         \\___/         \\____               |\n");
    printf("| |____||___|    |  ___ ___ |   | ___ ___ |____|             |\n");
    printf("| O-----O--O----O--O---O--O-----O----O--O-O----O-------------|\n");
    printf("|                                                            |\n");
    printf("|                    STATION BOARD                           |\n");
    printf("|                                                            |\n");
    printf("+============================================================+\n");

    /* Sensor info */
    printf("| Temperature : %-4d %-10s                              |\n",
           last_temp_value, last_temp_unit);
    printf("| Humidity    : %-4d %-10s                              |\n",
           last_hum_value, last_hum_unit);

    printf("+============================================================+\n");
    printf("|                        TRACK STATUS                        |\n");
    printf("+--------+------------+--------------------------------------+\n");
    printf("| Track  | State      | Train                                |\n");
    printf("+--------+------------+--------------------------------------+\n");

    for (int i = 0; i < num_tracks; i++) {

        const char *state_str;

        switch (tracks[i].state) {
            case FREE:        state_str = "FREE"; break;
            case BUSY:        state_str = "BUSY"; break;
            case INOPERATIVE: state_str = "INOP"; break;
            default:          state_str = "UNK";  break;
        }

        printf("| %-6d | %-10s | ",
               tracks[i].id,
               state_str);

        if (tracks[i].train_id >= 0)
            printf("Train %-29d |\n", tracks[i].train_id);
        else
            printf("%-34s |\n", "-");
    }

    printf("+--------+------------+--------------------------------------+\n");
}



/* -------- MAIN -------- */
int main(void) {

    /* Load configuration (USAC11) */
    if (!load_setup("data/config.txt")) {
        printf("[ERRO] Falha ao carregar config.txt\n");
        return 1;
    }

    printf("SETUP OK: %d users | %d tracks\n",
           num_users, num_tracks);

    /* Login UI */
    char username[20];
    char password[50];

    ui_login(username, password);

    User *u = login(username, password);

    if (!u) {
        printf("Login falhou\n");
        return 0;
    }

current_user = u;


    printf("Login OK. Bem-vindo %s\n", u->name);

    /* LOG: login */
    write_log(u->username, "login");

    /* -------- ROLE-BASED FLOW -------- */
    if (strcmp(u->username, "admin") == 0) {

        printf("Administrador autenticado.\n");

        /* Admin menu ONLY */
        ui_menu_admin();

    } else {

        printf("Operador autenticado.\n");

        /* Operator menu ONLY */
        ui_menu_operator();
    }

    /* LOG: logout */
    write_log(u->username, "logout");

    printf("Sessao terminada.\n");
    return 0;
}
