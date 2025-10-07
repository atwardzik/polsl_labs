.PHONY: clean

CC ?= gcc-14
AC ?= nasm
GCC_ARGS = -Wall -Wextra -Wpedantic -Werror -Wno-unused-variable -Wno-unused-parameter -std=gnu2x -c -g -O0
AC_ARGS = -f elf64 -g
LD_ARGS = -z noexecstack

all: main.o test.o a.out

clean:
	@rm -f *.o a.out

test.o: test.s
	$(AC) $(AC_ARGS) test.s -o test.o

main.o: main.c
	$(CC) $(GCC_ARGS) main.c -o main.o


a.out:
	$(CC) $(LD_ARGS) test.o main.o -o a.out

