#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "setup.h"
#include "domain.h"
#include "system_state.h"

extern void encrypt_data(char *str, int key);

static TrackState parse_track_state(const char *s) {
    if (strcmp(s, "FREE") == 0) return FREE;
    if (strcmp(s, "BUSY") == 0) return BUSY;
    return INOPERATIVE;
}

int load_setup(const char *filename) {

    FILE *f = fopen(filename, "r");
    if (!f) {
        printf("Erro ao abrir %s\n", filename);
        return 0;
    }

    char line[256];

    while (fgets(line, sizeof(line), f)) {

        if (line[0] == '#' || line[0] == '\n')
            continue;

        /* -------- SENSOR TEMP -------- */
        if (strncmp(line, "SENSOR_TEMP", 11) == 0) {
            sscanf(line, "SENSOR_TEMP,%d,%d",
                   &temp_sensor.buffer_length,
                   &temp_sensor.window_length);
        }

        /* -------- SENSOR HUM -------- */
        else if (strncmp(line, "SENSOR_HUM", 10) == 0) {
            sscanf(line, "SENSOR_HUM,%d,%d",
                   &hum_sensor.buffer_length,
                   &hum_sensor.window_length);
        }

        /* -------- TRACK -------- */
        else if (strncmp(line, "TRACK", 5) == 0) {

            tracks = realloc(tracks, sizeof(Track) * (num_tracks + 1));
            Track *t = &tracks[num_tracks];

            char state_str[20];
            sscanf(line, "TRACK,%d,%[^,],%d",
                   &t->id, state_str, &t->train_id);

            t->state = parse_track_state(state_str);
            num_tracks++;
        }

        /* -------- USER -------- */
        else if (strncmp(line, "USER", 4) == 0) {

            users = realloc(users, sizeof(User) * (num_users + 1));
            User *u = &users[num_users];

            sscanf(line, "USER,%[^,],%[^,],%[^,],%d",
                   u->name,
                   u->username,
                   u->password,
                   &u->cipher_key);

            encrypt_data(u->password, u->cipher_key);
            num_users++;
        }
    }

    fclose(f);

    printf("SETUP OK: %d users | %d tracks\n", num_users, num_tracks);
    return 1;
}
