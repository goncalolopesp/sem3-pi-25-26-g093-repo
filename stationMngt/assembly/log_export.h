#ifndef LOG_EXPORT_H
#define LOG_EXPORT_H

/* Estrutura de um registo de log */
typedef struct {
    int id;
    char username[50];
    char action[100];
    char timestamp[30];
} Log;

/*
 Exporta todas as ações de um utilizador para um ficheiro .txt
 Retorna 1 em sucesso, 0 em erro ou se o utilizador não tiver ações
*/
int exportUserActionsToTxt(const char *username, Log *logs, int logCount);

#endif
