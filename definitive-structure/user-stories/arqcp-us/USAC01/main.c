#include <stdio.h>
#include "encrpyt_data.h"

int main() {
    char in[] = "HELLO";
    char out[50];
    int key = 3;

    int res = encrpyt_data(in, key, out);
    printf("Input: %s\nOutput: %s\nReturn: %d\n", in, out, res);
    return 0;
}
