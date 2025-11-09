# Toolchain (usa o teu riscv32-linux-gcc do Bootlin)
CC= riscv32-linux-gcc
ARCHFLAGS= -march=rv32i -mabi=ilp32
CFLAG= -Wall -Wextra -g $(ARCHFLAGS)

# sysroot do QEMU (ajusta se precisares)
LIBRV32= $(HOME)/bin/bootlin/riscv32-buildroot-linux-gnu/sysroot

# Alvo por omissão
all: program.elf

program.elf: main.o extract_data.o
	$(CC) $(CFLAGS) $^ -o $@

main.o: main.c extract_data.h
	$(CC) $(CFLAGS) -c $< -o $@

extract_data.o: extract_data.s extract_data.h
	$(CC) $(CFLAGS) -c $< -o $@

run: program.elf
	qemu-riscv32 -L $(LIBRV32) ./program.elf

clean:
	rm -f *.o program.elf

.PHONY: all run clean
