#include <stdio.h>
#include "log_export.h"

int main() {
    Log logs[] = {
        {1, "admin", "Assigned train 12 to track 3", "2025-11-20 10:15"},
        {2, "admin", "Set track 3 to busy", "2025-11-20 10:18"},
        {3, "joao", "Emergency stop issued", "2025-11-20 11:00"},
        {4, "admin", "Gave departure order to train 12", "2025-11-20 10:30"},
        {5, "maria", "Login", "2025-11-20 09:50"}
    };

    int logCount = sizeof(logs) / sizeof(Log);

    if (exportUserActionsToTxt("admin", logs, logCount)) {
        printf("Ficheiro criado com sucesso.\n");
    } else {
        printf("Erro ou utilizador sem ações.\n");
    }

    return 0;
}
