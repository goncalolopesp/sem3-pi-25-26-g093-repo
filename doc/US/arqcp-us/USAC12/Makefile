CC = gcc
CFLAGS = -Wall -Wextra -std=c11

TARGET = log_export

OBJS = main.o log_export.o

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) -o $(TARGET) $(OBJS)

main.o: main.c log_export.h
	$(CC) $(CFLAGS) -c main.c

log_export.o: log_export.c log_export.h
	$(CC) $(CFLAGS) -c log_export.c

clean:
	rm -f $(OBJS) $(TARGET) *_actions.txt

run: all
	./$(TARGET)
