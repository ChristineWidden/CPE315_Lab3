package com.company;

import com.company.codeprocessing.instructions.ImmediateInstruction;
import com.company.codeprocessing.instructions.Instruction;
import com.company.codeprocessing.instructions.JumpInstruction;
import com.company.codeprocessing.instructions.RegisterInstruction;

import java.util.Arrays;

public class InstructionExecutor {
    private static int[] memory;
    private static int[] registers;
    public static int execute(Instruction inst, int pc, int[] mem, int[] regs) {
        memory = mem;
        int[] testthing = Arrays.copyOfRange(memory, 4070, 4096);
        registers = regs;
        switch (inst.getOpcode().getFormat()) {
            case I:
                pc = executeImmediateInstruction((ImmediateInstruction) inst, pc);
                break;
            case J:
                pc = executeJumpInstruction((JumpInstruction) inst, pc);
                break;
            case R:
                pc = executeRegisterInstruction((RegisterInstruction) inst, pc);
                break;
        }
        return pc;
    }

    private static int executeRegisterInstruction(RegisterInstruction inst, int pc) {
        int rs = inst.rs;
        int rt = inst.rt;
        int rd = inst.rd;
        int shamt = inst.shamt;
        int funct = inst.funct;

        switch (inst.getOpcode()) {
            case and:
                registers[rd] = registers[rs] & registers[rt];
                break;
            case or:
                registers[rd] = registers[rs] | registers[rt];
                break;
            case add:
                registers[rd] = registers[rs] + registers[rt];
                break;
            case sll:
                registers[rd] = registers[rt] << shamt;
                break;
            case sub:
                registers[rd] = registers[rs] - registers[rt];
                break;
            case slt:
                registers[rd] = (registers[rs] < registers[rt]) ? 1 : 0;
                break;
            case jr:
                pc = registers[rs] - 1;
                break;
        }
        return pc;
    }

    private static int executeImmediateInstruction(ImmediateInstruction inst, int pc) {
        int rs = inst.rs;
        int rt = inst.rt;
        int imm = inst.immediate;
        switch (inst.getOpcode()) {
            case addi:
                registers[rt] = registers[rs] + imm;
                break;
            case beq:
                if (registers[rs] == registers[rt]) {
                    pc = pc + imm;
                }
                break;
            case bne:
                if (registers[rs] != registers[rt]) {
                    pc = pc + imm;
                }
                break;
            case lw:
                registers[rt] = memory[registers[rs] + imm];
                break;
            case sw:
                memory[registers[rs] + imm] = registers[rt];
                break;
        }
        return pc;
    }

    private static int executeJumpInstruction(JumpInstruction inst, int pc) {
        switch (inst.getOpcode()) {
            case j:
                pc = inst.address - 1;
                break;
            case jal:
                registers[26] = pc + 1;
                pc = inst.address - 1;
                break;
        }
        return pc;
    }
}


