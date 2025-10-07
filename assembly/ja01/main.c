#include <stdint.h>
#include <stdio.h>

uint64_t multiply(uint64_t a, uint64_t b);
uint64_t addition_5regs(uint64_t a, uint64_t b, uint64_t c, uint64_t d, uint64_t e);
void cpu_flags_test(void);

int main(void) {
        uint64_t a = 145, b = 10;

        printf("%li\n", multiply(a, b));

        uint64_t ret_from_asm = addition_5regs(1, 1, 2, 3, 5);
        printf("%li\n", ret_from_asm);

        cpu_flags_test();

        return 0;
}
