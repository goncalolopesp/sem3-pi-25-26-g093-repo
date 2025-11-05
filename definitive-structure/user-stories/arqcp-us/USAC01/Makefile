CC=riscv32-linux-gcc
CFLAGS =-Wall -Wextra -fanalyzer -g
ARCHFLAGS=-march=rv32imd -mabi=ilp32d
LIBRV32 =${HOME}/bin/bootlin/riscv32-buildroot-linux-gnu/sysroot
program.elf: main.o encrpyt_data.o
	$(CC) $(CFLAGS) $(ARCHFLAGS) main.c encrpyt_data.s -o program.elf
main.o: main.c
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c main.c
encrpyt_data.o: encrpyt_data.s encrpyt_data.h
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c encrpyt_data.s
clean:
	rm -f *.o *.elf

run: program.elf
	qemu-riscv32 -L $(LIBRV32) ./program.elf
debug: program.elf
	@echo "On other terminal window do:"
	@echo  "riscv32-linux-gdb-tui./program.elf "
	qemu-riscv32 -L $(LIBRV32) -g 1234 program.elf -S
