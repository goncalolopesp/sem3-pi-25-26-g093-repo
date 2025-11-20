#include <stdio.h>
#include "enqueue_value.h"

int main(void) {
    int buffer[3] = {0, 0, 0};
    int length = 3;
    int nelem = 0;
    int tail = 0;
    int head = 0;
    int res;

    // 1º insert
    res = enqueue_value(buffer, length, &nelem, &tail, &head, 10);
    printf("res=%d nelem=%d head=%d tail=%d  [%d,%d,%d]\n",
           res, nelem, head, tail, buffer[0], buffer[1], buffer[2]);

    // 2º insert
    res = enqueue_value(buffer, length, &nelem, &tail, &head, 20);
    printf("res=%d nelem=%d head=%d tail=%d  [%d,%d,%d]\n",
           res, nelem, head, tail, buffer[0], buffer[1], buffer[2]);

    // 3º insert (fica cheio, deve devolver 1)
    res = enqueue_value(buffer, length, &nelem, &tail, &head, 30);
    printf("res=%d nelem=%d head=%d tail=%d  [%d,%d,%d]\n",
           res, nelem, head, tail, buffer[0], buffer[1], buffer[2]);

    // 4º insert (buffer cheio: apaga o mais antigo e continua cheio)
    res = enqueue_value(buffer, length, &nelem, &tail, &head, 40);
    printf("res=%d nelem=%d head=%d tail=%d  [%d,%d,%d]\n",
           res, nelem, head, tail, buffer[0], buffer[1], buffer[2]);

    return 0;
}
