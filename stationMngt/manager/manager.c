#include <stdio.h>
#include <string.h>

#include "domain.h"
#include "system_state.h"
#include "logs.h"

extern void encrypt_data(char *str, int key);

User* login(const char *username, const char *password_input) {

    for (int i = 0; i < num_users; i++) {

        if (strcmp(users[i].username, username) == 0) {

            char tmp[50];
            strcpy(tmp, password_input);

            encrypt_data(tmp, users[i].cipher_key);

            if (strcmp(tmp, users[i].password) == 0) {
                write_log(username, "LOGIN SUCCESS");
                return &users[i];
            }

            write_log(username, "LOGIN FAILED (WRONG PASSWORD)");
            return NULL;
        }
    }

    write_log(username, "LOGIN FAILED (UNKNOWN USER)");
    return NULL;
}

