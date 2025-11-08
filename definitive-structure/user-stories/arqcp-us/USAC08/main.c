#include <stdio.h>
#include "sort_array.h"

// Função auxiliar para imprimir array
void print_array(int* arr, int length) {
    for(int i = 0; i < length; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");
}

int main() {
    
    // Teste 1: Array normal - Ordem Ascendente
    printf("=== Teste 1: Array desordenado - Ordem Ascendente ===\n");
    int arr1[] = {64, 34, 25, 12, 22, 11, 90};
    int len1 = 7;
    printf("Antes: ");
    print_array(arr1, len1);
    
    int result1 = sort_array(arr1, len1, 1);
    
    printf("Depois (ascendente): ");
    print_array(arr1, len1);
    printf("Resultado: %d (1=sucesso, 0=falha)\n\n", result1);
    
    // Teste 2: Array normal - Ordem Descendente
    printf("=== Teste 2: Array desordenado - Ordem Descendente ===\n");
    int arr2[] = {64, 34, 25, 12, 22, 11, 90};
    int len2 = 7;
    printf("Antes: ");
    print_array(arr2, len2);
    
    int result2 = sort_array(arr2, len2, 0);
    
    printf("Depois (descendente): ");
    print_array(arr2, len2);
    printf("Resultado: %d (1=sucesso, 0=falha)\n\n", result2);
    
    // Teste 3: Array com números negativos - Ascendente (mostra signed)
    printf("=== Teste 3: Array com numeros negativos - Ascendente ===\n");
    int arr3[] = {-5, 10, -20, 0, 15, -3, 8};
    int len3 = 7;
    printf("Antes: ");
    print_array(arr3, len3);
    
    int result3 = sort_array(arr3, len3, 1);
    
    printf("Depois (ascendente): ");
    print_array(arr3, len3);
    printf("Resultado: %d (1=sucesso, 0=falha)\n\n", result3);
    
    return 0;
}
