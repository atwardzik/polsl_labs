####################################################
# Makefile and file structure generated by pset.sh #
#        Copyright (c) 2025 Artur Twardzik         #
#                                                  #
# Find file on: gist.github.com/atwardzik          #
# File name:    cset.sh                            #
####################################################
.PHONY: uf2
.PHONY: clean

CC ?= clang
CXX ?= clang++
AS ?= clang
LINKER ?= clang

CC_ARGS = -Wall -Wextra -Wpedantic -Werror -Wno-unused-variable -std=c23 -c -g -O0
CXX_ARGS = -Wall -Wextra -Wpedantic -Werror -Wno-unused-variable -std=c++23 -c -g -O0
AS_ARGS = -Wall -Wextra -Wpedantic -Werror -Wno-unused-variable -c -g
LINK_ARGS = 


all: main.o debug

clean:
	@rm -f *.o *.elf debug

main.o: main.c
	$(CC) $(CC_ARGS) main.c -o main.o

debug: main.o
	$(LINKER) $(LINK_ARGS) *.o -o debug

