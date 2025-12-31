#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "setup.h"
#include "domain.h"
#include "system_state.h"

// encrypt_data(in, key, out)
extern int encrypt_data(const char *in, int key, char *out);

int load_setup(const char *filename) {

    FILE *f = fopen(filename, "r");
    if (!f) {
        printf("Erro ao abrir %s\n", filename);
        return 0;
    }

    char line[256];

    num_users = 0;
    num_tracks = 0;

    while (fgets(line, sizeof(line), f)) {

        if (line[0] == '#' || line[0] == '\n')
            continue;

        /* ---------- SENSOR TEMP ---------- */
        if (strncmp(line, "SENSOR_TEMP", 11) == 0) {
            int buf, win;
            sscanf(line, "SENSOR_TEMP,%d,%d", &buf, &win);
            // se quiseres guardar depois, fazes isso noutro módulo
        }

        /* ---------- SENSOR HUM ---------- */
        else if (strncmp(line, "SENSOR_HUM", 10) == 0) {
            int buf, win;
            sscanf(line, "SENSOR_HUM,%d,%d", &buf, &win);
        }

       /* ---------- TRACK ---------- */
else if (strncmp(line, "TRACK", 5) == 0) {
    int id, train;
    char state_str[20];

    if (sscanf(line, "TRACK,%d,%[^,],%d", &id, state_str, &train) == 3) {

        tracks[num_tracks].id = id;
        tracks[num_tracks].train_id = train;

        if (strcmp(state_str, "FREE") == 0) {
            tracks[num_tracks].state = FREE;
        } else if (strcmp(state_str, "BUSY") == 0) {
            tracks[num_tracks].state = BUSY;
        } else {
            // estado desconhecido → ignora este track
            continue;
        }

        num_tracks++;
    }
}


        /* ---------- USER ---------- */
        else if (strncmp(line, "USER", 4) == 0) {

            char name[50];
            char username[20];
            char password_plain[50];
            int key;

            if (sscanf(line, "USER,%[^,],%[^,],%[^,],%d",
                       name, username, password_plain, &key) == 4) {

                char password_enc[50];

                if (!encrypt_data(password_plain, key, password_enc)) {
                    continue;
                }

                strcpy(users[num_users].name, name);
                strcpy(users[num_users].username, username);
                strcpy(users[num_users].password, password_enc);
                users[num_users].cipher_key = key;

                num_users++;
            }
        }
    }

    fclose(f);

printf("\n--- DEBUG USERS ---\n");
for (int i = 0; i < num_users; i++) {
    printf("USER[%d] username=%s key=%d pass(encrypted)=%s\n",
           i, users[i].username, users[i].cipher_key, users[i].password);
}
printf("-------------------\n\n");
    return 1;
}
