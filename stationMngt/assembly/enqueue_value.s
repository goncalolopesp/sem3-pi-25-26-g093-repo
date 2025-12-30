    .text
    .globl enqueue_value

# int enqueue_value(int* buffer, int length, int* nelem,
#                   int* tail, int* head, int value)
# a0 = buffer
# a1 = length
# a2 = nelem*
# a3 = tail*
# a4 = head*
# a5 = value
enqueue_value:
    # Carregar nelem, tail e head
    lw      t0, 0(a2)          # t0 = nElem
    lw      t1, 0(a3)          # t1 = tail
    lw      t2, 0(a4)          # t2 = head

    # Se length <= 0, não faz nada, retorna 0
    li      t3, 1
    blt     a1, t3, len_invalid   # if (length < 1) -> erro

    # Verificar se buffer está cheio: nElem == length
    beq     t0, a1, case_full

# -------- Caso NÃO cheio --------
case_not_full:
    # buffer[head] = value;
    slli    t5, t2, 2          # t5 = head * 4
    add     t5, t5, a0         # &buffer[head]
    sw      a5, 0(t5)

    # head = (head + 1) % length;
    addi    t2, t2, 1
    blt     t2, a1, no_wrap_head_nf
    li      t2, 0
no_wrap_head_nf:

    # nElem++
    addi    t0, t0, 1

    # flag cheio depois da inserção? (nElem == length)
    li      t4, 0
    bne     t0, a1, write_back
    li      t4, 1
    j       write_back

# -------- Caso cheio --------
case_full:
    # buffer[head] = value;  (sobrescreve o mais antigo após mover tail)
    slli    t5, t2, 2          # t5 = head * 4
    add     t5, t5, a0
    sw      a5, 0(t5)

    # head = (head + 1) % length;
    addi    t2, t2, 1
    blt     t2, a1, no_wrap_head_f
    li      t2, 0
no_wrap_head_f:

    # tail = (tail + 1) % length;  (anda com o mais antigo)
    addi    t1, t1, 1
    blt     t1, a1, no_wrap_tail_f
    li      t1, 0
no_wrap_tail_f:

    # nElem continua == length
    li      t4, 1              # cheio depois da inserção

    j       write_back

# -------- length inválido --------
len_invalid:
    li      a0, 0
    ret

# -------- Escrever resultados e retornar --------
write_back:
    sw      t0, 0(a2)          # *nelem = t0
    sw      t1, 0(a3)          # *tail = t1
    sw      t2, 0(a4)          # *head = t2

    mv      a0, t4             # return flag (0 ou 1)
    ret
