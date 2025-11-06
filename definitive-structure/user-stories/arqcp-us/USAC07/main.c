#include <stdio.h>
#include "move_n_to_array.h"

int main() {
    // Teste 1: Operação normal - remover 3 elementos de um buffer circular
    printf("=== Teste 1: Operacao normal ===\n");
    int buffer1[5] = {10, 20, 30, 40, 50};
    int nelem1 = 5;
    int head1 = 0;
    int tail1 = 0;
    int array1[5];
    int n1 = 3;
    
    printf("Antes: nelem=%d, head=%d\n", nelem1, head1);
    printf("Buffer: ");
    for(int i = 0; i < 5; i++) printf("%d ", buffer1[i]);
    printf("\n");
    
    int result1 = move_n_to_array(buffer1, 5, &nelem1, &tail1, &head1, n1, array1);
    
    printf("Depois: movidos=%d, nelem=%d, head=%d\n", result1, nelem1, head1);
    printf("Array: ");
    for(int i = 0; i < result1; i++) printf("%d ", array1[i]);
    printf("\n\n");
    
    // Teste 2: Pedir mais elementos do que os disponíveis
    printf("=== Teste 2: Pedir mais do que o disponivel ===\n");
    int buffer2[5] = {100, 200, 300, 0, 0};
    int nelem2 = 3;
    int head2 = 0;
    int tail2 = 3;
    int array2[10];
    int n2 = 10;  // Pedir 10 mas apenas 3 disponíveis
    
    printf("Antes: nelem=%d, head=%d, pedidos=%d\n", nelem2, head2, n2);
    printf("Buffer: ");
    for(int i = 0; i < nelem2; i++) printf("%d ", buffer2[i]);
    printf("\n");
    
    int result2 = move_n_to_array(buffer2, 5, &nelem2, &tail2, &head2, n2, array2);
    
    printf("Depois: movidos=%d, nelem=%d, head=%d\n", result2, nelem2, head2);
    printf("Array: ");
    for(int i = 0; i < result2; i++) printf("%d ", array2[i]);
    printf("\n\n");
    
    // Teste 3: Caso de wraparound
    printf("=== Teste 3: Wraparound ===\n");
    int buffer3[5] = {40, 50, 10, 20, 30};
    int nelem3 = 5;
    int head3 = 2;  // Começa no índice 2 (mais antigo é 10)
    int tail3 = 2;
    int array3[5];
    int n3 = 4;
    
    printf("Antes: nelem=%d, head=%d\n", nelem3, head3);
    printf("Buffer (circular, head em %d): ", head3);
    for(int i = 0; i < 5; i++) printf("%d ", buffer3[i]);
    printf("\n");
    
    int result3 = move_n_to_array(buffer3, 5, &nelem3, &tail3, &head3, n3, array3);
    
    printf("Depois: movidos=%d, nelem=%d, head=%d\n", result3, nelem3, head3);
    printf("Array (ordem de remocao): ");
    for(int i = 0; i < result3; i++) printf("%d ", array3[i]);
    printf("\n\n");
    
    // Teste 4: Buffer vazio
    printf("=== Teste 4: Buffer vazio ===\n");
    int buffer4[5] = {0, 0, 0, 0, 0};
    int nelem4 = 0;
    int head4 = 0;
    int tail4 = 0;
    int array4[5];
    int n4 = 3;
    
    printf("Antes: nelem=%d, head=%d, pedidos=%d\n", nelem4, head4, n4);
    
    int result4 = move_n_to_array(buffer4, 5, &nelem4, &tail4, &head4, n4, array4);
    
    printf("Depois: movidos=%d, nelem=%d, head=%d\n", result4, nelem4, head4);
    printf("\n");
    
    // Teste 5: n = 0
    printf("=== Teste 5: Pedir n=0 ===\n");
    int buffer5[5] = {1, 2, 3, 4, 5};
    int nelem5 = 5;
    int head5 = 0;
    int tail5 = 0;
    int array5[5];
    int n5 = 0;
    
    printf("Antes: nelem=%d, head=%d, pedidos=%d\n", nelem5, head5, n5);
    
    int result5 = move_n_to_array(buffer5, 5, &nelem5, &tail5, &head5, n5, array5);
    
    printf("Depois: movidos=%d, nelem=%d, head=%d\n", result5, nelem5, head5);
    printf("\n");

    return 0;
}
