package com.company;

import com.company.codeprocessing.Program;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class lab3 {

    private static enum Mode {
        INTERACTIVE,
        SCRIPT
    }
    private static Mode MODE;
    static Program program;

    public static void main(String[] args) throws IOException {
        setup(args);
        program = new Program(args[0]);

        if (MODE.equals(Mode.INTERACTIVE)) {
            InteractiveMode.runInteractiveMode(args, program);
        } else {
            String script = new String(Files.readAllBytes(Paths.get(args[1])), StandardCharsets.UTF_8);
            InteractiveMode.runScriptMode(args, program, script);
        }
    }

    private static void setup(String[] args) {
        if (args.length > 1) {
            MODE = Mode.SCRIPT;
        } else {
            MODE = Mode.INTERACTIVE;
        }
    }

}
