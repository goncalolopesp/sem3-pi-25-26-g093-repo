#include <stdio.h>
#include "format_command.h"

int main(void) {
    char cmd[20];
    int res;

    char str1[] = " rB ";
    res = format_command(str1, 5, cmd);
    printf("%d: %s\n", res, cmd);   // 1: RB,05

    char str2[] = " Ye ";
    res = format_command(str2, 25, cmd);
    printf("%d: %s\n", res, cmd);   // 1: YE,25

    char str3[] = " Ye ";
    res = format_command(str3, 125, cmd);
    printf("%d: %s\n", res, cmd);   // 0:

    char str4[] = " aaa ";
    res = format_command(str4, 25, cmd);
    printf("%d: %s\n", res, cmd);   // 0:

    char str5[] = " gTh ";
    res = format_command(str5, 123, cmd);  // n é ignorado
    printf("%d: %s\n", res, cmd);   // 1: GTH

    return 0;
}
