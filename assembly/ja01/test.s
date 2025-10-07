BITS    64
SECTION .text

global  multiply

multiply:
    mov  rax, rdi
    imul rax, rsi
    ret


global addition_5regs

addition_5regs:
    xor rax, rax

    add rax, rdi
    add rax, rsi
    add rax, rdx
    add rax, rcx
    add rax, r8

    ret


global cpu_flags_test

cpu_flags_test:
        xor rax, rax    ; zero flag

        mov al, 14      
        add al, 1       ; parity (four ones)

        mov al, 127
        add al, 1       ; overflow + sign

        mov al, 255
        add al, 1       ; carry

        ret
