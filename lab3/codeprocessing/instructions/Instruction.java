package codeprocessing.instructions;

import codeprocessing.Code;

public class Instruction {

    int lineNumber;
    Code opcode;

    public Instruction(int lineNumber, Code code) {
        this.lineNumber = lineNumber;
        this.opcode = code;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public Code getOpcode() {
        return opcode;
    }
}
