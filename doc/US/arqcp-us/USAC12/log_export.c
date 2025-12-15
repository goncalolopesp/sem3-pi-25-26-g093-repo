#include <stdio.h>
#include <string.h>
#include "log_export.h"

int exportUserActionsToTxt(const char *username, Log *logs, int logCount) {
    char filename[100];
    FILE *file;
    int found = 0;

    /* Nome do ficheiro */
    sprintf(filename, "%s_actions.txt", username);

    file = fopen(filename, "w");
    if (file == NULL) {
        return 0;
    }

    fprintf(file, "User: %s\n\n", username);

    /* Percorrer todos os logs */
    for (int i = 0; i < logCount; i++) {
        if (strcmp(logs[i].username, username) == 0) {
            fprintf(file, "%s - %s\n", logs[i].timestamp, logs[i].action);
            found = 1;
        }
    }

    fclose(file);

    /* Se não houver ações, apaga o ficheiro */
    if (!found) {
        remove(filename);
        return 0;
    }

    return 1;
}
