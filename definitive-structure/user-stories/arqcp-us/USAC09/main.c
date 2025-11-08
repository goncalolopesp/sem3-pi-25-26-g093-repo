#include <stdio.h>
#include "median.h"

// Função auxiliar para imprimir array
void print_array(int* arr, int length) {
    for(int i = 0; i < length; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");
}

int main() {
    
    // Teste 1: Array ímpar (5 elementos) - Mostra mediana de ímpar
    printf("=== Teste 1: Array impar (5 elementos) ===\n");
    int arr1[] = {5, 2, 8, 1, 9};
    int len1 = 5;
    int med1;
    printf("Antes: ");
    print_array(arr1, len1);
    
    int result1 = median(arr1, len1, &med1);
    
    printf("Depois (ordenado): ");
    print_array(arr1, len1);
    printf("Mediana: %d (esperado: 5)\n", med1);
    printf("Resultado: %d (1=sucesso, 0=falha)\n\n", result1);
    
    // Teste 2: Array par com arredondamento - Mostra arredondamento para cima
    printf("=== Teste 2: Array par - arredondamento para cima ===\n");
    int arr2[] = {1, 2, 3, 4};
    int len2 = 4;
    int med2;
    printf("Antes: ");
    print_array(arr2, len2);
    
    int result2 = median(arr2, len2, &med2);
    
    printf("Depois (ordenado): ");
    print_array(arr2, len2);
    printf("Mediana: %d (esperado: (2+3+1)/2 = 3 arredondado para cima)\n", med2);
    printf("Resultado: %d (1=sucesso, 0=falha)\n\n", result2);
    
    // Teste 3: Array ímpar com negativos - Mostra funcionamento com negativos
    printf("=== Teste 3: Array impar com negativos ===\n");
    int arr3[] = {-10, -5, 0, 5, 10};
    int len3 = 5;
    int med3;
    printf("Antes: ");
    print_array(arr3, len3);
    
    int result3 = median(arr3, len3, &med3);
    
    printf("Depois (ordenado): ");
    print_array(arr3, len3);
    printf("Mediana: %d (esperado: 0)\n", med3);
    printf("Resultado: %d (1=sucesso, 0=falha)\n\n", result3);
    
    return 0;
}
