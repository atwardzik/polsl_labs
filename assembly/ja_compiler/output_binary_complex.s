.global _car
.align 4
_car:
sub sp, sp, #32
str x0, [sp, #8]
str x1, [sp, #0]
ldr x0, [sp, #0]
mov x1, x0
ldr x2, [sp, #8]
cmp x2, #0
beq .one
mov x2, #0
b .end
.one:
mov x2, #1
.end:
add x1, x1, x2
mov x0, x1
ret
