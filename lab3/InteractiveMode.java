import codeprocessing.Program;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;


public class InteractiveMode {

    private enum Command {
        h,
        d,
        s,
        r,
        m,
        c,
        q
    }

    private static Program program;

    public static void runInteractiveMode(String[] args, Program p) {
        program = p;
        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        Command currentCommand;

        while (running) {
            System.out.print("mips> ");
            String[] scanned = scanner.nextLine().split(" ");
            currentCommand = Command.valueOf(scanned[0].toLowerCase(Locale.ROOT));
            running = run(currentCommand, scanned);
        }
    }

    public static void runScriptMode(String[] args, Program p, String script) {
        program = p;
        boolean running = true;
        Scanner scanner = new Scanner(script);
        Command currentCommand;

        while (running) {
            System.out.print("mips> ");
            String scannedLine = scanner.nextLine();
            String[] scanned = scannedLine.split(" ");
            if(!Objects.equals(scanned[0], "q")) {
                System.out.println(scannedLine);
            } else {
                System.out.print(scannedLine);
            }

            currentCommand = Command.valueOf(scanned[0].toLowerCase(Locale.ROOT));
            running = run(currentCommand, scanned);
        }
    }

    private static boolean run(Command currentCommand, String[] scanned) {
        boolean running = true;
        switch (currentCommand) {
            case h:
                helpCommand();
                break;
            case d:
                dumpCommand();
                break;
            case s:
                try {
                    stepCommand(Integer.parseInt(scanned[1]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    stepCommand(1);
                }
                break;
            case r:
                runCommand();
                break;
            case m:
                memoryCommand(Integer.parseInt(scanned[1]), Integer.parseInt(scanned[2]));
                break;
            case c:
                clearCommand();
                break;
            case q:
                running = false;
                break;
        }

        return running;
    }

    private static void stepCommand(int n) {
        for (int i = 0; i < n; i++) {
            program.executeLine();
        }
        System.out.println("        "+n+" instruction(s) executed");
    }

    private static void runCommand() {
        program.executeProgram();
    }

    private static void memoryCommand(int start, int end) {
        System.out.println();
        int[] memory = program.getAllMemory();
        for (int i = start; i <= end; i++) {
            System.out.println(String.format("[%d] = %d", i, memory[i]));
        }
        System.out.println();

    }

    private static void clearCommand() {
        program.clearAll();
        System.out.println("        Simulator reset");
        System.out.println();
    }


    private static void dumpCommand() {

        int[] registers = program.getAllRegisters();
        int pc = program.programCounter;
        String[] registerList = program.getRegisterList();

        System.out.println();

        System.out.println("pc = " + pc);
        for (int i = 0; i < 7; i++) {
            if (i==0) {
                int index = 4*i;
                //System.out.print(stringthing(registerList[index], registers[index],false));
                System.out.print(registerList[index] + " = " + registers[index] + "          ");
                for (int j = 1; j < 4; j++) {
                    index = 4*i + j;
                    System.out.print(stringthing(registerList[index], registers[index], (j==3)));
                    //System.out.print(registerList[index] + " = " + registers[index] + "         ");
                }
                System.out.println();
            }
            else if (i < 6) {
                for (int j = 0; j < 4; j++) {
                    int index = 4*i + j;
                    System.out.print(stringthing(registerList[index], registers[index], (j==3)));
                    //System.out.print(registerList[index] + " = " + registers[index] + "         ");
                }
                System.out.println();
            } else {
                for (int j = 0; j < 3; j++) {
                    int index = 4*i + j;
                    System.out.print(stringthing(registerList[index], registers[index], (j==2)));
                    //System.out.print(registerList[index] + " = " + registers[index] + "         ");
                }
            }
        }
        System.out.println();
        System.out.println();
    }

    private static String stringthing(String register, int registerVal, boolean isLast) {
        String s = register+ " = " + registerVal;
        int spaceCount = 17 - s.length();
        if(spaceCount < 7) spaceCount += 8; else spaceCount = 9;
        if(isLast) spaceCount = 0;
        for (int i = 0; i < spaceCount; i++) {
            s += " ";
        }
        return s;
    }

    private static void helpCommand() {
        System.out.println();
        System.out.println("h = show help\n" +
                "d = dump register state\n" +
                "s = single step through the program (i.e. execute 1 instruction and stop)\n" +
                "s num = step through num instructions of the program\n" +
                "r = run until the program ends\n" +
                "m num1 num2 = display data memory from location num1 to num2\n" +
                "c = clear all registers, memory, and the program counter to 0\n" +
                "q = exit the program");
        System.out.println();
    }

    private static void setProgram(Program p) {
        program = p;
    }

}
