.global _foo
.align 4
_foo:
sub sp, sp, #32
str x0, [sp, #24]
str x1, [sp, #16]
str x2, [sp, #8]
str x3, [sp, #0]
ldr x0, [sp, #24]
mov x1, x0
ldr x2, [sp, #16]
cmp x2, #0
beq .one
mov x2, #0
b .end
.one:
mov x2, #1
.end:
add x1, x1, x2
mov x3, x1
ldr x4, [sp, #8]
mov x5, x4
ldr x6, [sp, #0]
mul x5, x5, x6
sub x3, x3, x5
mov x0, x3
ret
