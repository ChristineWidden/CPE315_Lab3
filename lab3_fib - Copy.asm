# compute fibonacci numbers
# the sixth number is stored in $v0 when the program completes

main:
addi $a0, $0, 6	# input argument
#addi $sp, $0, 4095	# initialize stack pointer
jal fibonacci
j end

fibonacci:	addi $t0, $0, 3
		slt $t1, $a0, $t0
		bne $0, $t1, basecase	# check if argument is less than 3

		addi $sp, $sp, -12
		sw $a0, 0($sp)
		sw $ra, 4($sp)
		addi $a0, $a0, -4
		jal fibonacci		# compute fibonacci(n-1)

		sw $v0, 8($sp)
		lw $a0, 0($sp)
		addi $a0, $a0, -8
		jal fibonacci		# compute fibonacci(n-2)
		lw $t0, 8($sp)
		add $v0, $v0, $t0

		lw $ra, 4($sp)
		addi $sp, $sp, 4
		jr $ra

basecase:	addi $v0, $0, 1
		jr $ra


end: add $0, $0, $0
