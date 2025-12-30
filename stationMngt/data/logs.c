#include <stdio.h>
#include <time.h>

void write_log(const char *username, const char *action) {

    FILE *f = fopen("data/system.log", "a");
    if (!f) return;

    time_t now = time(NULL);
    struct tm *t = localtime(&now);

    fprintf(f,
        "%04d-%02d-%02d %02d:%02d:%02d | user=%s | action=%s\n",
        t->tm_year + 1900,
        t->tm_mon + 1,
        t->tm_mday,
        t->tm_hour,
        t->tm_min,
        t->tm_sec,
        username,
        action
    );

    fclose(f);
}
