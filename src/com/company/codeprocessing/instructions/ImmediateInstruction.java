package com.company.codeprocessing.instructions;

import com.company.codeprocessing.Code;

public class ImmediateInstruction extends Instruction {
    public int rs;
    public int rt;
    public int immediate;


    public ImmediateInstruction(int lineNumber, Code code, int rs, int rt, int immediate) {
        super(lineNumber, code);
        this.rs = rs;
        this.rt = rt;
        this.immediate = immediate;
    }
}
