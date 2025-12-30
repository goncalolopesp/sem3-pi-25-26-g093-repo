.globl move_n_to_array

# int move_n_to_array(int* buffer, int length, int *nelem, int *head, int *tail, int n, int* array)
# a0 = buffer (ponteiro para o buffer circular)
# a1 = length (tamanho total do buffer)
# a2 = nelem (ponteiro para o número de elementos válidos)
# a3 = head (ponteiro para o índice do elemento mais antigo - read)
# a4 = tail (ponteiro para o índice de inserção - write)
# a5 = n (número de elementos a remover)
# a6 = array (ponteiro para o array destino)

move_n_to_array:
    # Carregar valores atuais da memória
    lw t0, 0(a2)          # t0 = *nelem (número de elementos válidos)
    lw t1, 0(a3)          # t1 = *head (índice do elemento mais antigo)
    
    # Verificar se o buffer está vazio (nelem == 0)
    beqz t0, return_zero  # se *nelem == 0, retorna 0
    
    # Verificar se n == 0
    beqz a5, return_zero  # se n == 0, retorna 0
    
    # Determinar quantos elementos realmente mover
    # moved = min(n, *nelem)
    # Se n > *nelem, retorna 0 (falha), senão retorna 1 (sucesso)
    mv t2, a5             # t2 = n (elementos pedidos para mover)
    blt t0, t2, return_zero_fail # se *nelem < n, retorna 0 (falha)
    j start_loop          # caso contrário usa n
    
start_loop:
    # t2 agora contém o número de elementos que vamos mover
    # Guardar para o valor de retorno
    mv t3, t2             # t3 = número de elementos a mover
    
    # t4 será o nosso contador do loop (conta de t2 até 0)
    mv t4, t2             # t4 = contador
    
    # t5 vai rastrear a posição atual no array destino
    mv t5, a6             # t5 = ponteiro para posição atual no array
    
copy_loop:
    beqz t4, end_loop     # se contador == 0, sai do loop
    
    # Calcular endereço de memória de buffer[head]
    # endereço = buffer + (head * 4)  ; 4 bytes por int
    slli t6, t1, 2        # t6 = head * 4 (shift left de 2 = multiplicar por 4)
    add t6, a0, t6        # t6 = buffer + (head * 4)
    
    # Carregar elemento do buffer
    lw t6, 0(t6)          # t6 = buffer[head]
    
    # Guardar elemento no array destino
    sw t6, 0(t5)          # *array = buffer[head]
    
    # Avançar head com wraparound
    addi t1, t1, 1        # head++
    blt t1, a1, no_wrap   # se head < length, não precisa wraparound
    li t1, 0              # caso contrário head = 0 (wraparound)
    
no_wrap:
    # Avançar ponteiro do array
    addi t5, t5, 4        # array++ (mover para próximo int, 4 bytes)
    
    # Decrementar contador
    addi t4, t4, -1       # contador--
    
    j copy_loop           # continuar loop
    
end_loop:
    # Atualizar *head na memória
    sw t1, 0(a3)          # *head = valor atualizado de head
    
    # Atualizar *nelem na memória
    sub t0, t0, t3        # *nelem = *nelem - movidos
    sw t0, 0(a2)          # guardar nelem atualizado
    
    # Retornar 1 (sucesso)
    li a0, 1              # valor de retorno = 1 (sucesso)
    ret
    
return_zero:
    li a0, 0              # retorna 0 (buffer vazio ou n=0)
    ret
    
return_zero_fail:
    li a0, 0              # retorna 0 (pediu mais elementos do que disponíveis)
    ret
