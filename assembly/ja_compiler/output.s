.global _foo
.align 4
_foo:
sub	sp, sp, #32
str	x0, [sp, #24]
str	x1, [sp, #16]
str	x2, [sp, #8]
str	x3, [sp, #0]
ldr	x0, [sp, #24]
mov	x1, x0
ldr	x0, [sp, #16]
cmp	x0, #0
ite	eq
movseq	x0, #1
movsne	x0, #0
add	x1, x1, x0
mov	x0, x1
ldr	x1, [sp, #8]
mov	x2, x1
ldr	x1, [sp, #0]
mul	x2, x2, x1
mov	x1, x2
movs	x2, #2
mul	x1, x1, x2
sub	x0, x0, x1
ret
