.global _main
.align 4
_main:
	stp  x29, x30, [sp, #-32]!
	sub  sp, sp, #32

	adr  x0, DataString
	str  x0, [sp, #8]
	mov  x1, 'J'
	str  x1, [sp]
	bl   _find_char
	str  x0, [sp, #16]

	adr  x0, msg
	bl   _printf

	mov  x0, #0
	mov  x16, #1
	svc  #0

DataString: .string "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
msg:	    .string "Position of letter '%c' in string \"%s\" is %i\n"

/*
 * int find_char(char *ptr, char c);
 * @returns position of specified character or -1 if not found
 */
.global _find_char
.align 4
_find_char:
	mov  x10, #0
	mov  x11, #0x0
	.cmpLoop:
		ldrb w12, [x0, x10]
		cmp  x1, x12
		beq  .foundChar
		cmp  x11, x12
		beq  .notFoundChar
		add  x10, x10, #1
		b    .cmpLoop

	.foundChar:
		mov  x0, x10
		ret

	.notFoundChar:
		mov  x0, #-1
		ret






