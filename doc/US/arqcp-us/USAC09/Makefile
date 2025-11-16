CC=riscv32-linux-gcc
CFLAGS =-Wall -Wextra -fanalyzer -g
ARCHFLAGS=-march=rv32imd -mabi=ilp32d
LIBRV32 =${HOME}/bin/bootlin/riscv32-buildroot-linux-gnu/sysroot

program.elf: main.o median.o
	$(CC) $(CFLAGS) $(ARCHFLAGS) main.o median.o -o program.elf

main.o: main.c median.h
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c main.c

median.o: median.s median.h
	$(CC) $(CFLAGS) $(ARCHFLAGS) -c median.s

clean:
	rm -f *.o *.elf

run: program.elf
	qemu-riscv32 -L $(LIBRV32) ./program.elf

debug: program.elf
	@echo "On other terminal window do:"
	@echo  "riscv32-linux-gdb -tui ./program.elf "
	qemu-riscv32 -L $(LIBRV32) -g 1234 program.elf -S
