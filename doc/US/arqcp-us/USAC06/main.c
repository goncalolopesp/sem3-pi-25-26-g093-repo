#include <stdio.h>

#include "dequeue_value.h"

int main(void) {
    int buffer[3] = {10,20,30};
    int length = 3;
    int nelem = 3;
    int tail = 0;
    int head = 0;
    int value;
    int res;

    res = dequeue_value(buffer, length, &nelem, &tail, &head, &value);
    printf("res=%d value=%d nelem=%d tail=%d head=%d\n",
           res, value, nelem, tail, head);

    return 0;
}
