package codeprocessing.instructions;

import codeprocessing.Code;

public class RegisterInstruction extends Instruction {
    public int rs;
    public int rt;
    public int rd;
    public int shamt;
    public int funct;

    public RegisterInstruction(int lineNumber, Code code, int rs, int rt, int rd, int shamt, int funct) {
        super(lineNumber, code);
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
        this.shamt = shamt;
        this.funct = funct;
    }
}
