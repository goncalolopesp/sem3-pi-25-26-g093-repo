    .globl decrypt_data
decrypt_data:
    li t0, 0              # resultado = 0

loop:
    lbu t1, 0(a0)         # lê caractere de entrada (*in)
    beqz t1, end_loop     # se for '\0', termina o loop

    li t2, 'A'            # t2 = 'A'
    blt t1, t2, not_upper # se < 'A', não é maiúscula
    li t3, 'Z'            # t3 = 'Z'
    bgt t1, t3, not_upper # se > 'Z', não é maiúscula

    sub t4, t1, a1        # t4 = *in - key  ← diferença principal!
    bge t4, t2, no_wrap   # se >= 'A', não precisa ajustar
    addi t4, t4, 26       # se passou antes de 'A', soma 26
no_wrap:
    sb t4, 0(a2)          # *out = caractere desencriptado
    li t0, 1              # resultado = 1
    j advance_pointers

not_upper:
    sb t1, 0(a2)          # *out = *in  (não era letra maiúscula)

advance_pointers:
    addi a0, a0, 1        # in++
    addi a2, a2, 1        # out++
    j loop                # volta ao início

end_loop:
    sb x0, 0(a2)          # *out = '\0'
    mv a0, t0             # return resultado
    ret
