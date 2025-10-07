#include <stdio.h>
#include <stdint.h>

uint64_t parameter_multiplication(uint64_t rdi, uint64_t rsi, uint64_t rdx, uint64_t rcx, uint64_t r8, uint64_t r9, uint64_t stack) {
        uint64_t var = rdi + rsi;
        uint64_t var1 = rdx + rcx;
        uint64_t r = r8 + r9;

        uint64_t res = var * var1 + r + stack;

        return res;
}
