    .text
    .globl dequeue_value

# int dequeue_value(int* buffer, int length, int *nelem,
#                   int *tail, int *head, int *value)
#
# a0 = buffer
# a1 = length
# a2 = nelem*
# a3 = tail*
# a4 = head*
# a5 = value*

dequeue_value:
    # Carregar *nelem e *tail
    lw      t0, 0(a2)          # t0 = nelem
    lw      t1, 0(a3)          # t1 = tail

    # buffer vazio?
    beqz    t0, fail

    # length inválido?
    li      t2, 1
    blt     a1, t2, fail

    # Ler buffer[tail]
    slli    t3, t1, 2          # tail * 4
    add     t3, t3, a0
    lw      t4, 0(t3)          # valor lido

    sw      t4, 0(a5)          # *value = t4

    # tail = (tail + 1) % length
    addi    t1, t1, 1
    blt     t1, a1, no_wrap
    li      t1, 0
no_wrap:
    sw      t1, 0(a3)

    # nelem--
    addi    t0, t0, -1
    sw      t0, 0(a2)

    # sucesso
    li      a0, 1
    ret

fail:
    li      a0, 0
    ret
