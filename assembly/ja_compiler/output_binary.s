.global _foo
.align 4
_foo:
sub sp, sp, #32
str x0, [sp, #8]
str x1, [sp, #0]
ldr x0, [sp, #8]
mov x1, x0
ldr x2, [sp, #0]
add x1, x1, x2
mov x0, x1
ret
