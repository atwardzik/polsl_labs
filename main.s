.text
.global main
main:
        ldr x1, =msg
        mov x2, #13
        mov x0, #1              // stdout
        mov w8, #64             // syscall write
        svc #0

        mov x0, #0
        mov w8, #93             // syscall exit
        svc #0



msg: .ascii "Hello world!\n"
