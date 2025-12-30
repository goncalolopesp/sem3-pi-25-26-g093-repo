    .globl encrypt_data
encrypt_data:
    li t0, 1
    blt a1, t0, fail
    li t0, 26
    bgt a1, t0, fail

loop:
    lbu t1, 0(a0)
    beqz t1, end_ok

    li t2, 'A'
    blt t1, t2, fail
    li t3, 'Z'
    bgt t1, t3, fail

    add t4, t1, a1
    li t5, 'Z'
    ble t4, t5, store
    addi t4, t4, -26

store:
    sb t4, 0(a2)
    addi a0, a0, 1
    addi a2, a2, 1
    j loop

end_ok:
    sb x0, 0(a2)
    li a0, 1
    ret

fail:
    sb x0, 0(a2)
    li a0, 0
    ret
