.global _bar
.align 4
_bar:
sub sp, sp, #32
str x0, [sp, #8]
str x1, [sp, #0]
ldr x0, [sp, #8]
cmp x0, #0
beq .one
mov x0, #0
b .end
.one:
mov x0, #1
.end:
ret
