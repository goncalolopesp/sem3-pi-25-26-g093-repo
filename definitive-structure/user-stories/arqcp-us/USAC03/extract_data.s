    .globl extract_data
    .type  extract_data, @function
# int extract_data(char* str, char* token, char* unit, int* value)
# a0=str, a1=token, a2=unit, a3=&value
extract_data:
    # limpar saídas
    sw      x0, 0(a3)            # *value = 0
    sb      x0, 0(a2)            # unit[0] = '\0'

    addi    t0, a0, 0            # t0 = str
    addi    t1, a1, 0            # t1 = token

# -------- procurar token (substring) --------
find_token:
    lbu     t2, 0(t0)
    beq     t2, x0, not_found

    addi    t3, t0, 0            # cursor str
    addi    t4, t1, 0            # cursor token

token_loop:
    lbu     t5, 0(t4)            # token[j]
    beq     t5, x0, token_found  # fim do token => match
    lbu     t6, 0(t3)            # str[i+j]
    beq     t6, x0, advance_str
    bne     t6, t5, advance_str
    addi    t3, t3, 1
    addi    t4, t4, 1
    jal     x0, token_loop

advance_str:
    addi    t0, t0, 1
    jal     x0, find_token

token_found:
    addi    t3, t0, 0            # t3 = início do token em str

# -------- procurar "&unit" --------
find_unit:
    lbu     t5, 0(t3)
    beq     t5, x0, not_found

    addi    t6, x0, 38           # '&'
    bne     t5, t6, next_unit
    # "&unit"
    lbu     t5, 1(t3)
    addi    t6, x0, 117          # 'u'
    bne     t5, t6, next_unit
    lbu     t5, 2(t3)
    addi    t6, x0, 110          # 'n'
    bne     t5, t6, next_unit
    lbu     t5, 3(t3)
    addi    t6, x0, 105          # 'i'
    bne     t5, t6, next_unit
    lbu     t5, 4(t3)
    addi    t6, x0, 116          # 't'
    bne     t5, t6, next_unit

    addi    t3, t3, 5            # agora em "&unit" no 't'
    addi    t3, t3, 1            # avançar para depois de "unit"

    # espaços opcionais antes de ':'
skip_spaces_before_colon_unit:
    lbu     t5, 0(t3)
    addi    t6, x0, 32           # ' '
    bne     t5, t6, check_colon_unit
    addi    t3, t3, 1
    jal     x0, skip_spaces_before_colon_unit

check_colon_unit:
    lbu     t5, 0(t3)
    addi    t6, x0, 58           # ':'
    bne     t5, t6, next_unit

    addi    t3, t3, 1            # depois de ':'

    # também tolerar espaços antes do conteúdo da unit
skip_spaces_after_colon_unit:
    lbu     t5, 0(t3)
    addi    t6, x0, 32
    bne     t5, t6, copy_unit
    addi    t3, t3, 1
    jal     x0, skip_spaces_after_colon_unit

    jal     x0, copy_unit

next_unit:
    addi    t3, t3, 1
    jal     x0, find_unit

# -------- copiar unit até '&' ou '#' ou '\0' --------
copy_unit:
    lbu     t5, 0(t3)
    beq     t5, x0, end_unit
    addi    t6, x0, 38           # '&'
    beq     t5, t6, end_unit
    addi    t6, x0, 35           # '#'
    beq     t5, t6, end_unit
    sb      t5, 0(a2)
    addi    a2, a2, 1
    addi    t3, t3, 1
    jal     x0, copy_unit

end_unit:
    sb      x0, 0(a2)

# -------- procurar "&value" --------
find_value:
    lbu     t5, 0(t3)
    beq     t5, x0, not_found

    addi    t6, x0, 38           # '&'
    bne     t5, t6, next_val
    # "&value"
    lbu     t5, 1(t3)
    addi    t6, x0, 118          # 'v'
    bne     t5, t6, next_val
    lbu     t5, 2(t3)
    addi    t6, x0, 97           # 'a'
    bne     t5, t6, next_val
    lbu     t5, 3(t3)
    addi    t6, x0, 108          # 'l'
    bne     t5, t6, next_val
    lbu     t5, 4(t3)
    addi    t6, x0, 117          # 'u'
    bne     t5, t6, next_val
    lbu     t5, 5(t3)
    addi    t6, x0, 101          # 'e'
    bne     t5, t6, next_val

    addi    t3, t3, 6            # depois de "value"

    # espaços opcionais antes de ':'
skip_spaces_before_colon_val:
    lbu     t5, 0(t3)
    addi    t6, x0, 32           # ' '
    bne     t5, t6, check_colon_val
    addi    t3, t3, 1
    jal     x0, skip_spaces_before_colon_val

check_colon_val:
    lbu     t5, 0(t3)
    addi    t6, x0, 58           # ':'
    bne     t5, t6, next_val

    addi    t3, t3, 1            # depois de ':'

    # tolerar espaços antes do número
skip_spaces_before_digits:
    lbu     t5, 0(t3)
    addi    t6, x0, 32           # ' '
    bne     t5, t6, read_number
    addi    t3, t3, 1
    jal     x0, skip_spaces_before_digits

    jal     x0, read_number

next_val:
    addi    t3, t3, 1
    jal     x0, find_value

# -------- ler número (atoi simplificado, só positivos) --------
read_number:
    addi    t4, x0, 0            # acumulador = 0
parse_loop:
    lbu     t5, 0(t3)
    beq     t5, x0, success
    addi    t6, x0, 48           # '0'
    blt     t5, t6, success
    addi    a4, x0, 57           # '9'  (usar a4 em vez de t7; RV32I não tem t7)
    addi    t1, a4, 1            # '9'+1
    blt     t1, t5, success      # se t5 > '9', sai

    # t4 = t4*10 + (t5 - '0')
    slli    t1, t4, 3            # *8
    slli    t2, t4, 1            # *2
    add     t4, t1, t2           # *10
    addi    t5, t5, -48          # - '0'
    add     t4, t4, t5

    addi    t3, t3, 1
    jal     x0, parse_loop

success:
    sw      t4, 0(a3)
    addi    a0, x0, 1
    ret

not_found:
    sw      x0, 0(a3)
    sb      x0, 0(a2)
    addi    a0, x0, 0
    ret
