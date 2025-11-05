    .globl encrpyt_data
encrpyt_data:
    li t0, 0

loop:
    lbu t1, 0(a0)
    beqz t1, end_loop

    li t2, 'A'
    blt t1, t2, not_upper
    li t3, 'Z'
    bgt t1, t3, not_upper

    add t4, t1, a1
    li t5, 'Z'
    ble t4, t5, no_wrap
    addi t4, t4, -26
no_wrap:
    sb t4, 0(a2)
    li t0, 1
    j advance_pointers

not_upper:
    sb t1, 0(a2)

advance_pointers:
    addi a0, a0, 1
    addi a2, a2, 1
    j loop

end_loop:
    sb x0, 0(a2)
    mv a0, t0
    ret
