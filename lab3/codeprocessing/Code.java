package codeprocessing;

public enum Code {
    and(Format.R),
    or(Format.R),
    add(Format.R),
    addi(Format.I),
    sll(Format.R),
    sub(Format.R),
    slt(Format.R),
    beq(Format.I),
    bne(Format.I),
    lw(Format.I),
    sw(Format.I),
    j(Format.J),
    jr(Format.R),
    jal(Format.J);

    final Format format;

    Code(Format f) {
        this.format = f;
    }

    public Format getFormat() {
        return this.format;
    }
}
