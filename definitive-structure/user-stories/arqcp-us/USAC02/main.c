#include <stdio.h>
#include "decrypt_data.h"

int main() {
    char encrypted[] = "KHOOR";  // texto cifrado
    char decrypted[100];
    int key = 3;
    int result;

    printf("Encrypted: %s\n", encrypted);
    printf("Key: %d\n", key);

    result = decrypt_data(encrypted, key, decrypted);

    printf("Decrypted: %s (return = %d)\n", decrypted, result);

    return 0;
}
