# ===============================
# Toolchain
# ===============================
CC      = riscv32-linux-gcc
QEMU    = qemu-riscv32

# ===============================
# Flags
# ===============================
CFLAGS  = -Wall -Wextra -fanalyzer -g \
          -march=rv32imd -mabi=ilp32d \
          -Igeneral -Imanager -Iinterface -Idata

QEMU_SYSROOT = /home/guest/bin/bootlin/riscv32-buildroot-linux-gnu/sysroot

# ===============================
# Files
# ===============================
C_SRC = \
	manager/manager.c \
	manager/setup.c \
	general/system_state.c \
	data/logs.c \
	interface/ui.c

ASM_SRC = \
	assembly/encrypt_data.s \
	assembly/extract_data.s \
	assembly/update_track.s

OBJ = $(C_SRC:.c=.o) $(ASM_SRC:.s=.o)

TARGET = stationMngt.elf

# ===============================
# Rules
# ===============================

all: $(TARGET)

$(TARGET): $(OBJ)
	$(CC) $(CFLAGS) $^ -o $@

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

%.o: %.s
	$(CC) -c $< -o $@

run: $(TARGET)
	$(QEMU) -L $(QEMU_SYSROOT) ./$(TARGET)

clean:
	rm -f $(OBJ) $(TARGET)

.PHONY: all clean run

		
