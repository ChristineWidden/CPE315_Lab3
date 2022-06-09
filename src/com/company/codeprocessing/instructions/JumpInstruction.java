package com.company.codeprocessing.instructions;

import com.company.codeprocessing.Code;

public class JumpInstruction extends Instruction {
    public int address;

    public JumpInstruction(int lineNumber, Code code, int address) {
        super(lineNumber, code);
        this.address = address;
    }
}
