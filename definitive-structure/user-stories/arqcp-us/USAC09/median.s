.globl median

# int median(int* vec, int length, int *me)
# a0 = vec (ponteiro para o array)
# a1 = length (tamanho do array)
# a2 = me (ponteiro para guardar a mediana)
# Retorna: 1 para sucesso, 0 para falha

median:
    # Validações iniciais
    beqz a1, return_fail      # se length == 0, retorna 0 (falha)
    beqz a2, return_fail      # se me == NULL, retorna 0 (falha)
    
    # Se length == 1, a mediana é o próprio elemento
    li t0, 1
    bne a1, t0, do_sort       # se length != 1, ordenar
    lw t0, 0(a0)              # t0 = vec[0]
    sw t0, 0(a2)              # *me = vec[0]
    li a0, 1                  # retorna 1 (sucesso)
    ret
    
do_sort:
    # Ordenar o array usando Bubble Sort (ascendente)
    # Bubble Sort - Loop externo
    addi t3, a1, -1           # t3 = length - 1
    li t1, 0                  # t1 = i = 0
    
outer_loop:
    bge t1, t3, sort_done     # se i >= length-1, terminar ordenação
    
    # Loop interno: j vai de 0 até (length-1-i)
    li t2, 0                  # t2 = j = 0
    sub t4, t3, t1            # t4 = length - 1 - i
    
inner_loop:
    bge t2, t4, outer_inc     # se j >= limit, próxima iteração externa
    
    # Calcular endereços de vec[j] e vec[j+1]
    slli t5, t2, 2            # t5 = j * 4
    add t5, a0, t5            # t5 = endereço de vec[j]
    
    lw t6, 0(t5)              # t6 = vec[j]
    lw a3, 4(t5)              # a3 = vec[j+1]
    
    # Ordem ascendente: trocar se vec[j] > vec[j+1]
    ble t6, a3, no_swap       # se vec[j] <= vec[j+1], não trocar
    
    # Trocar vec[j] e vec[j+1]
    sw a3, 0(t5)              # vec[j] = vec[j+1]
    sw t6, 4(t5)              # vec[j+1] = vec[j] (valor antigo)
    
no_swap:
    addi t2, t2, 1            # j++
    j inner_loop              # continuar loop interno
    
outer_inc:
    addi t1, t1, 1            # i++
    j outer_loop              # continuar loop externo
    
sort_done:
    # Array agora está ordenado
    # Calcular a mediana
    
    # Verificar se length é ímpar ou par
    andi t0, a1, 1            # t0 = length & 1 (bit menos significativo)
    bnez t0, odd_length       # se ímpar, ir para odd_length
    
even_length:
    # Length é par: mediana = (vec[length/2-1] + vec[length/2] + 1) / 2
    # Adicionar 1 antes de dividir faz arredondamento para cima
    srli t1, a1, 1            # t1 = length / 2
    addi t2, t1, -1           # t2 = length/2 - 1 (índice do primeiro central)
    
    # Calcular endereço de vec[length/2-1]
    slli t3, t2, 2            # t3 = (length/2-1) * 4
    add t3, a0, t3            # t3 = endereço de vec[length/2-1]
    lw t4, 0(t3)              # t4 = vec[length/2-1]
    
    # Calcular endereço de vec[length/2]
    slli t3, t1, 2            # t3 = (length/2) * 4
    add t3, a0, t3            # t3 = endereço de vec[length/2]
    lw t5, 0(t3)              # t5 = vec[length/2]
    
    # Calcular média com arredondamento para cima: (t4 + t5 + 1) / 2
    add t6, t4, t5            # t6 = vec[length/2-1] + vec[length/2]
    addi t6, t6, 1            # t6 = soma + 1 (para arredondar para cima)
    srli t6, t6, 1            # t6 = (soma + 1) / 2
    
    # Guardar mediana em *me
    sw t6, 0(a2)              # *me = mediana
    
    # Retornar sucesso
    li a0, 1                  # retorna 1 (sucesso)
    ret
    
odd_length:
    # Length é ímpar: mediana = vec[length/2]
    srli t1, a1, 1            # t1 = length / 2 (índice do meio)
    
    # Calcular endereço de vec[length/2]
    slli t2, t1, 2            # t2 = (length/2) * 4
    add t2, a0, t2            # t2 = endereço de vec[length/2]
    lw t3, 0(t2)              # t3 = vec[length/2]
    
    # Guardar mediana em *me
    sw t3, 0(a2)              # *me = mediana
    
    # Retornar sucesso
    li a0, 1                  # retorna 1 (sucesso)
    ret
    
return_fail:
    li a0, 0                  # retorna 0 (falha)
    ret
