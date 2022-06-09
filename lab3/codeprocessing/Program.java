package codeprocessing;

//import codeprocessing.Program.InstructionExecutor;
import codeprocessing.instructions.ImmediateInstruction;
import codeprocessing.instructions.Instruction;
import codeprocessing.instructions.JumpInstruction;
import codeprocessing.instructions.RegisterInstruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Program {
    private int[] memory = new int[8192];
    private int[] registers = new int[27];
    public int programCounter = 0;


    private final String[] registerList = {"$0", "$v0", "$v1", "$a0",
                                    "$a1", "$a2", "$a3", "$t0",
                                    "$t1", "$t2", "$t3", "$t4",
                                    "$t5", "$t6", "$t7", "$s0",
                                    "$s1", "$s2", "$s3", "$s4",
                                    "$s5", "$s6", "$s7", "$t8",
                                    "$t9", "$sp", "$ra"};

    private HashMap<String, Integer> registerMap = new HashMap<>();
    private ArrayList<Instruction> instructions = new ArrayList<>();
    private HashMap<Integer, Instruction> instructionLines = new HashMap<>();

    public Program(String assemblyFile) throws IOException {
        for (int i = 0; i < 27; i++) {
            registerMap.put(registerList[i], i);
        }

        instructions = CodeProcessor.processAssemblyFile(registerMap, assemblyFile);

        for (Instruction inst:
             instructions) {
            instructionLines.put(inst.getLineNumber(), inst);
        }

        System.out.print("");
    }

    public void executeProgram() {
        while(programCounter < instructions.size()) {
            programCounter = InstructionExecutor.execute(instructionLines.get(programCounter), programCounter, memory, registers);
            programCounter++;
        }
    }

    public void executeLine() {
        programCounter = InstructionExecutor.execute(instructionLines.get(programCounter), programCounter, memory, registers);
        programCounter++;
    }

    public void setRegister(String register, int value) {
        registers[registerMap.get(register)] = value;
    }

    public int getRegister(String register) {
        return registers[registerMap.get(register)];
    }

    public int[] getAllRegisters() {
        return registers.clone();
    }

    public int[] getAllMemory() {
        return memory.clone();
    }

    public String[] getRegisterList() {
        return registerList.clone();
    }

    public void clearAll() {
        memory = new int[8192];
        registers = new int[32];
        programCounter = 0;
    }

    public static class InstructionExecutor {
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
}
