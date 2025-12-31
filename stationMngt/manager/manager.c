#include <stdio.h>
#include <string.h>

#include "system_state.h"
#include "setup.h"
#include "ui.h"
#include "logs.h"

/* ASM encrypt */
extern int encrypt_data(char *, int, char *);

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
