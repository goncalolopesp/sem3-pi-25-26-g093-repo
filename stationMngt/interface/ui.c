#include "ui.h"
#include <stdio.h>
#include <string.h>
#include <ctype.h>

/* -------- Utils -------- */

static void clean_stdin(void) {
    int c;
    while ((c = getchar()) != '\n' && c != EOF);
}

static void read_string(char *buf, int size) {
    if (fgets(buf, size, stdin)) {
        buf[strcspn(buf, "\n")] = '\0';
    } else {
        clean_stdin();
    }
}

/* -------- Login UI -------- */

void ui_login(char *username, char *password) {
    printf("\n=================================\n");
    printf("     STATION MANAGEMENT SYSTEM\n");
    printf("=================================\n");
    printf("Username: ");
    read_string(username, 20);

    printf("Password: ");
    read_string(password, 50);
}

/* -------- MENUS -------- */

void ui_menu_admin(void) {
    int opt;
    do {
        printf("\n--- ADMIN MENU ---\n");
        printf("1. USAC11 – Load Configuration\n");
        printf("2. USAC12 – View Logs\n");
        printf("0. Logout\n");
        printf("Option: ");

        if (scanf("%d", &opt) != 1) {
            clean_stdin();
            opt = -1;
        }

        switch (opt) {
            case 1: printf("[ADMIN] Config already loaded.\n"); break;
            case 2: printf("[ADMIN] Logs feature pending.\n"); break;
            case 3: printf("[ADMIN] Sensor read pending.\n"); break;
            case 4: printf("[ADMIN] Signal control pending.\n"); break;
            case 5: printf("[ADMIN] Messaging pending.\n"); break;
            case 6: printf("[ADMIN] Track management pending.\n"); break;
            case 0: printf("Logout...\n"); break;
            default: printf("Invalid option.\n");
        }
    } while (opt != 0);
}

void ui_menu_operator(void) {
    int opt;
    do {
        printf("\n--- OPERATOR MENU ---\n");
        printf("1. USAC13 – Get Sensor Data\n");
        printf("2. USAC14 – Control Signals\n");
        printf("3. USAC15 – Send Message\n");
        printf("4. USAC16 – Track Manager\n");
        printf("0. Logout\n");
        printf("Option: ");

        if (scanf("%d", &opt) != 1) {
            clean_stdin();
            opt = -1;
        }

        switch (opt) {
case 1:
    manager_get_sensor_data();
    break;
            case 2: manager_control_signal(); break;
case 3:
    manager_show_board();
    break;
case 4:
    manager_manage_tracks();
    break;
            case 0: printf("Logout...\n"); break;
            default: printf("Invalid option.\n");
        }
    } while (opt != 0);
}

