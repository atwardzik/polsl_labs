.cpu cortex-m33
.thumb
.syntax unified

.global foo
.align 4
foo:
sub	sp, sp, #32
str	r0, [sp, #24]
str	r1, [sp, #16]
str	r2, [sp, #8]
str	r3, [sp, #0]
ldr	r0, [sp, #24]
mov	r1, r0
ldr	r0, [sp, #16]
cmp	r0, #0
ite	eq
movseq	r0, #1
movsne	r0, #0
add	r1, r1, r0
mov	r0, r1
ldr	r1, [sp, #8]
mov	r2, r1
ldr	r1, [sp, #0]
mul	r2, r2, r1
mov	r1, r2
movs	r2, #2
mul	r1, r1, r2
sub	r0, r0, r1
bx	lr
