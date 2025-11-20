    .text
    .globl format_command

# int format_command(char *op, int n, char *cmd)
# a0 = op
# a1 = n
# a2 = cmd
format_command:
    addi    sp, sp, -16        # espaço no stack
    sw      t0, 0(sp)          # guardar t0
    sw      t1, 4(sp)          # guardar t1
    sw      t2, 8(sp)          # guardar t2
    sw      t3, 12(sp)         # guardar t3

    # t0 = ponteiro para op (vai andar para a frente para fazer trim left)
    mv      t0, a0

# ---- TRIM LEFT: saltar espaços, tabs e newlines ----
trim_left_loop:
    lb      t1, 0(t0)          # t1 = *t0
    beqz    t1, trim_done      # fim da string => acaba trim
    li      t2, ' '            # espaço
    beq     t1, t2, trim_skip
    li      t2, 9              # '\t'
    beq     t1, t2, trim_skip
    li      t2, 10             # '\n'
    beq     t1, t2, trim_skip
    j       trim_done

trim_skip:
    addi    t0, t0, 1
    j       trim_left_loop

trim_done:
    # Agora t0 aponta para o 1º char "útil" do comando

    # Vamos copiar até 3 chars para buffer temporário em t1/t2 e a6
    # e já converter para maiúsculas.
    li      t1, 0              # len = 0
    li      t2, 0              # guarda 1º char
    li      t3, 0              # guarda 2º char
    li      a6, 0              # guarda 3º char

copy_loop:
    lb      a3, 0(t0)          # a3 = *t0
    beqz    a3, copy_end       # fim da string -> pára
    li      a4, ' '
    beq     a3, a4, copy_end   # espaço -> pára
    li      a4, 9
    beq     a3, a4, copy_end   # '\t' -> pára
    li      a4, 10
    beq     a3, a4, copy_end   # '\n' -> pára

    # converter para maiúsculas se [a-z]
    li      a4, 'a'
    blt     a3, a4, not_lower
    li      a5, 'z'
    bgt     a3, a5, not_lower
    addi    a3, a3, -32        # a3 -= 32

not_lower:
    # guardar no "buffer" em registos, até 3 chars
    li      a4, 0
    beq     t1, a4, store_first
    li      a4, 1
    beq     t1, a4, store_second
    li      a4, 2
    beq     t1, a4, store_third
    # se já temos 3 chars, ignorar o resto
    j       skip_inc

store_first:
    mv      t2, a3
    j       inc_len
store_second:
    mv      t3, a3
    j       inc_len
store_third:
    mv      a6, a3             # 3º char guardado em a6 (já em maiúsculas)
    j       inc_len

inc_len:
    addi    t1, t1, 1          # len++
skip_inc:
    addi    t0, t0, 1
    j       copy_loop

copy_end:
    # len em t1, chars em:
    #  t2 = 1º, t3 = 2º, a6 = 3º
    # (se len < 2 ou > 3 pode ser inválido)

    # Se len == 0 => inválido
    beqz    t1, error_return

# ---- Identificar comando ----
    # Primeiro: comandos de 2 letras: RE, YE, GE, RB
    li      a4, 2
    bne     t1, a4, check_gth  # se len != 2, ir ver GTH

    # len == 2. Ver se (t2,t3) é um de RE, YE, GE, RB
    # if (t2=='R' && t3=='E') OK
    li      a4, 'R'
    bne     t2, a4, check_YE
    li      a4, 'E'
    bne     t3, a4, check_RB_or_GE
    j       cmd_2letter_ok     # "RE"

check_YE:
    li      a4, 'Y'
    bne     t2, a4, check_GE
    li      a4, 'E'
    bne     t3, a4, error_return
    j       cmd_2letter_ok     # "YE"

check_GE:
    li      a4, 'G'
    bne     t2, a4, check_RB
    li      a4, 'E'
    bne     t3, a4, error_return
    j       cmd_2letter_ok     # "GE"

check_RB:
    li      a4, 'R'
    bne     t2, a4, error_return
    li      a4, 'B'
    bne     t3, a4, error_return
    j       cmd_2letter_ok     # "RB"

# só chega aqui se for "RE" e falhar a parte 'E' (mas já tratámos)
check_RB_or_GE:
    # aqui t2=='R' mas t3 != 'E', apenas "RB" é possível
    li      a4, 'B'
    bne     t3, a4, error_return
    j       cmd_2letter_ok     # "RB"

cmd_2letter_ok:
    # Comando válido com 2 letras, usa número n
    j       check_number_and_build

check_gth:
    # len != 2, ver se é 'GTH' (3 letras)
    li      a4, 3
    bne     t1, a4, error_return   # se len != 3, erro

    # t2 = 1º, t3 = 2º, a6 = 3º (já em maiúsculas)
    li      a4, 'G'
    bne     t2, a4, error_return
    li      a4, 'T'
    bne     t3, a4, error_return
    li      a4, 'H'
    bne     a6, a4, error_return

    # É "GTH", ignora n -> construir string "GTH"
    j       build_gth

# ---- Verificar número e construir string CMD,xx ----
check_number_and_build:
    # n em a1, tem de estar entre 0 e 99
    bltz    a1, error_return      # se n < 0 -> erro
    li      a4, 99
    bgt     a1, a4, error_return  # se n > 99 -> erro

    # cmd[0] = 1º char (t2)
    sb      t2, 0(a2)
    # cmd[1] = 2º char (t3)
    sb      t3, 1(a2)
    # cmd[2] = ','
    li      a4, ','
    sb      a4, 2(a2)

    # Calcular dezenas e unidades: tens = n / 10; units = n % 10
    li      a4, 10
    div     a5, a1, a4        # a5 = n / 10
    rem     a6, a1, a4        # a6 = n % 10

    # cmd[3] = '0' + tens
    li      a4, '0'
    add     a5, a5, a4
    sb      a5, 3(a2)

    # cmd[4] = '0' + units
    li      a4, '0'
    add     a6, a6, a4
    sb      a6, 4(a2)

    # cmd[5] = '\0'
    li      a4, 0
    sb      a4, 5(a2)

    li      a0, 1             # return 1 (sucesso)
    j       restore_and_ret

# ---- Construir string "GTH" ----
build_gth:
    li      a4, 'G'
    sb      a4, 0(a2)
    li      a4, 'T'
    sb      a4, 1(a2)
    li      a4, 'H'
    sb      a4, 2(a2)
    li      a4, 0
    sb      a4, 3(a2)

    li      a0, 1             # return 1
    j       restore_and_ret

# ---- Caso de erro: cmd vazia, retorno 0 ----
error_return:
    li      a4, 0
    sb      a4, 0(a2)         # cmd[0] = '\0'
    li      a0, 0             # return 0

restore_and_ret:
    lw      t0, 0(sp)
    lw      t1, 4(sp)
    lw      t2, 8(sp)
    lw      t3, 12(sp)
    addi    sp, sp, 16
    ret
