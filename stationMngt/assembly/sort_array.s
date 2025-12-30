.globl sort_array

# int sort_array(int* vec, int length, char order)
# a0 = vec (ponteiro para o array)
# a1 = length (tamanho do array)
# a2 = order (1 = ascendente, 0 = descendente)
# Retorna: 1 para sucesso, 0 para falha

sort_array:
    # Validações iniciais
    beqz a1, return_fail      # se length == 0, retorna 0 (falha)
    li t0, 1
    beq a1, t0, return_success # se length == 1, já está ordenado, retorna 1
    
    # Verificar se order é válido (0 ou 1)
    li t0, 1
    bgt a2, t0, return_fail   # se order > 1, inválido
    bltz a2, return_fail      # se order < 0, inválido
    
    # Bubble Sort - Loop externo
    # t1 = i (contador do loop externo, vai de 0 até length-1)
    # t2 = j (contador do loop interno)
    # t3 = length - 1 (limite)
    
    addi t3, a1, -1           # t3 = length - 1
    li t1, 0                  # t1 = i = 0
    
outer_loop:
    bge t1, t3, end_sort      # se i >= length-1, terminar
    
    # Loop interno: j vai de 0 até (length-1-i)
    li t2, 0                  # t2 = j = 0
    sub t4, t3, t1            # t4 = length - 1 - i (limite do loop interno)
    
inner_loop:
    bge t2, t4, outer_loop_inc # se j >= limit, ir para próxima iteração externa
    
    # Calcular endereços de vec[j] e vec[j+1]
    slli t5, t2, 2            # t5 = j * 4
    add t5, a0, t5            # t5 = endereço de vec[j]
    
    lw t6, 0(t5)              # t6 = vec[j]
    lw a3, 4(t5)              # a3 = vec[j+1]
    
    # Verificar a ordem e decidir se trocar
    beqz a2, descending_order # se order == 0, ordem descendente
    
ascending_order:
    # Ordem ascendente: trocar se vec[j] > vec[j+1]
    ble t6, a3, no_swap       # se vec[j] <= vec[j+1], não trocar
    j do_swap                 # senão, trocar
    
descending_order:
    # Ordem descendente: trocar se vec[j] < vec[j+1]
    bge t6, a3, no_swap       # se vec[j] >= vec[j+1], não trocar
    
do_swap:
    # Trocar vec[j] e vec[j+1]
    sw a3, 0(t5)              # vec[j] = vec[j+1]
    sw t6, 4(t5)              # vec[j+1] = vec[j] (valor antigo)
    
no_swap:
    # Incrementar j
    addi t2, t2, 1            # j++
    j inner_loop              # continuar loop interno
    
outer_loop_inc:
    # Incrementar i
    addi t1, t1, 1            # i++
    j outer_loop              # continuar loop externo
    
end_sort:
    # Retornar sucesso
    li a0, 1                  # retorna 1 (sucesso)
    ret
    
return_fail:
    li a0, 0                  # retorna 0 (falha)
    ret
    
return_success:
    li a0, 1                  # retorna 1 (sucesso)
    ret
