     .globl extract_data
    .type  extract_data, @function

extract_data:
    sw      x0, 0(a3)
    sb      x0, 0(a2)

    addi    t0, a0, 0
    addi    t1, a1, 0

find_token:
    lbu     t2, 0(t0)
    beq     t2, x0, not_found
    addi    t3, t0, 0
    addi    t4, t1, 0

token_loop:
    lbu     t5, 0(t4)
    beq     t5, x0, token_found
    lbu     t6, 0(t3)
    beq     t6, x0, advance_str
    bne     t6, t5, advance_str
    addi    t3, t3, 1
    addi    t4, t4, 1
    j       token_loop

advance_str:
    addi    t0, t0, 1
    j       find_token

token_found:
    addi    t3, t0, 0

find_unit:
    lbu     t5, 0(t3)
    beq     t5, x0, not_found
    addi    t6, x0, 38
    bne     t5, t6, next_unit
    lbu     t5, 1(t3)
    addi    t6, x0, 117
    bne     t5, t6, next_unit
    lbu     t5, 2(t3)
    addi    t6, x0, 110
    bne     t5, t6, next_unit
    lbu     t5, 3(t3)
    addi    t6, x0, 105
    bne     t5, t6, next_unit
    lbu     t5, 4(t3)
    addi    t6, x0, 116
    bne     t5, t6, next_unit

    addi    t3, t3, 5

skip_spaces_before_colon_unit:
    lbu     t5, 0(t3)
    addi    t6, x0, 32
    bne     t5, t6, check_colon_unit
    addi    t3, t3, 1
    j       skip_spaces_before_colon_unit

check_colon_unit:
    lbu     t5, 0(t3)
    addi    t6, x0, 58
    bne     t5, t6, next_unit
    addi    t3, t3, 1

skip_spaces_after_colon_unit:
    lbu     t5, 0(t3)
    addi    t6, x0, 32
    bne     t5, t6, copy_unit
    addi    t3, t3, 1
    j       skip_spaces_after_colon_unit

copy_unit:
    lbu     t5, 0(t3)
    beq     t5, x0, end_unit
    addi    t6, x0, 38
    beq     t5, t6, end_unit
    addi    t6, x0, 35
    beq     t5, t6, end_unit
    sb      t5, 0(a2)
    addi    a2, a2, 1
    addi    t3, t3, 1
    j       copy_unit

end_unit:
    sb      x0, 0(a2)

find_value:
    lbu     t5, 0(t3)
    beq     t5, x0, not_found
    addi    t6, x0, 38
    bne     t5, t6, next_val

    lbu     t5, 1(t3)
    addi    t6, x0, 118
    bne     t5, t6, next_val
    lbu     t5, 2(t3)
    addi    t6, x0, 97
    bne     t5, t6, next_val
    lbu     t5, 3(t3)
    addi    t6, x0, 108
    bne     t5, t6, next_val
    lbu     t5, 4(t3)
    addi    t6, x0, 117
    bne     t5, t6, next_val
    lbu     t5, 5(t3)
    addi    t6, x0, 101
    bne     t5, t6, next_val

    addi    t3, t3, 6

skip_spaces_before_colon_val:
    lbu     t5, 0(t3)
    addi    t6, x0, 32
    bne     t5, t6, check_colon_val
    addi    t3, t3, 1
    j       skip_spaces_before_colon_val

check_colon_val:
    lbu     t5, 0(t3)
    addi    t6, x0, 58
    bne     t5, t6, next_val
    addi    t3, t3, 1

skip_spaces_before_digits:
    lbu     t5, 0(t3)
    addi    t6, x0, 32
    bne     t5, t6, read_number
    addi    t3, t3, 1
    j       skip_spaces_before_digits

read_number:
    addi    t4, x0, 0

parse_loop:
    lbu     t5, 0(t3)
    beq     t5, x0, success
    addi    t6, x0, 48
    blt     t5, t6, success
    addi    a4, x0, 57
    blt     a4, t5, success

    slli    t1, t4, 3
    slli    t2, t4, 1
    add     t4, t1, t2
    addi    t5, t5, -48
    add     t4, t4, t5
    addi    t3, t3, 1
    j       parse_loop

success:
    sw      t4, 0(a3)
    addi    a0, x0, 1
    ret

next_unit:
    addi    t3, t3, 1
    j       find_unit

next_val:
    addi    t3, t3, 1
    j       find_value

not_found:
    sw      x0, 0(a3)
    sb      x0, 0(a2)
    addi    a0, x0, 0
    ret