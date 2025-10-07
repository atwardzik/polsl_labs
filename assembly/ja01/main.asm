BITS    64
SECTION .text
global  _start

_start:
    mov rax, 1   ; sys_write
    mov rdi, 1   ; stdout
    lea rsi, msg
    mov rdx, 13
    syscall

    mov rax, 60  ; sys_exit
    xor rdi, rdi
    syscall

SECTION .data
msg: db "Hello World!", 0x0A
