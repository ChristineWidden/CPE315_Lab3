package codeprocessing;

import codeprocessing.instructions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;

public class CodeProcessor {

    private static final HashMap<String, Integer> labels = new HashMap<>();
    private static int programCounter = 0;

    private static HashMap<String, Integer> registerMap;

    public static ArrayList<Instruction> processAssemblyFile(HashMap<String, Integer> rm, String assemblyFile) throws IOException {
        ArrayList<Instruction> instructions = new ArrayList<>();
        registerMap = rm;

        String content = new String(Files.readAllBytes(Paths.get(assemblyFile)), StandardCharsets.UTF_8);

        content = content.replaceAll("\t", " ")
                .replaceAll(",", " ")
                .replaceAll("\\$", Matcher.quoteReplacement(" $"))
                .replaceAll("( )+", " ");


        Scanner scanner = new Scanner(content);

        String line;

        while(scanner.hasNextLine()) {
            line = scanner.nextLine();

            int hashIndex = line.indexOf("#");
            if (hashIndex != -1) {
                line = line.substring(0, hashIndex);
            }

            line = line.trim();

            if (line.length() > 0) {

                String afterLabel = line;

                int labelIndex = line.indexOf(":");

                if (labelIndex != -1) {
                    afterLabel = line.substring(labelIndex+1).trim();
                    labels.put(line.substring(0, labelIndex), programCounter);
                }

                if (afterLabel.length() > 0) programCounter++;

            }

        }

        programCounter = 0;
        scanner = new Scanner(content);
        while(scanner.hasNextLine()) {
            line = scanner.nextLine().trim();

            if (line.length() > 0) {

                int hashIndex = line.indexOf("#");
                if (hashIndex != -1) {
                    line = line.substring(0, hashIndex);
                }

                int labelIndex = line.indexOf(":");
                if (labelIndex != -1) {
                    line = line.substring(labelIndex+1);
                }

                line = line.trim();


                if (line.length() > 0) {
                    String[] splitLine = line.split(" ");

                    Code commandCode = Code.valueOf(splitLine[0]);
                    Instruction inst = idk(programCounter, commandCode, splitLine);
                    instructions.add(inst);


                    programCounter++;
                }


            }
        }

        return instructions;
    }

    private static Instruction idk(int pc, Code commandCode, String[] tokens) {
        String machineCodeLine = "";

        int rs;
        int rt;
        int rd;
        int shamt;
        int imm;

        Instruction instruction = null;

        switch (commandCode.format) {
            case R:
                // opcode rs rt rd shamt funct
                //machineCodeLine += commandCode.code1 + " ";

                rs = rt = rd = shamt = 0;

                switch (commandCode.name()){
                    case "add":
                    case "and":
                    case "or":
                    case "sub":
                    case "slt":
                        //rd rs rt
                        rd = registerMap.get(tokens[1]);
                        rs = registerMap.get(tokens[2]);
                        rt = registerMap.get(tokens[3]);
                        break;
                    case "sll":
                        //rd rt shamt
                        rd = registerMap.get(tokens[1]);
                        rt = registerMap.get(tokens[2]);
                        shamt = registerMap.get(tokens[3]);
                        break;

                    case "jr":
                        //rs
                        rs = registerMap.get(tokens[1]);
                        break;
                }

                instruction = new RegisterInstruction(pc, commandCode, rs, rt, rd, shamt, 0);

                break;
            case I:
                // opcode rs rt immediate


                if (!tokens[3].contains("$")) {
                    //imm = Integer.parseInt(tokens[3]);
                    try {
                        imm = Integer.parseInt(tokens[3]);
                    } catch (NumberFormatException e) {
                        imm = 0;
                        for (String label :
                                labels.keySet())  {
                            if (label.equals(tokens[3])) {
                                imm = labels.get(label) - programCounter - 1;
                            }
                        }
                    }

                    rt = registerMap.get(tokens[1]);
                    rs = registerMap.get(tokens[2]);
                } else {
                    rt = registerMap.get(tokens[1]);
                    tokens[2] = tokens[2].replace("(", "");
                    tokens[3] = tokens[3].replace(")", "");

                    rs = registerMap.get(tokens[3]);
                    imm = Integer.parseInt(tokens[2]);
                }


                instruction = new ImmediateInstruction(pc, commandCode, rs, rt, imm);

                break;
            case J:
                //opcode address
                for (String label :
                        labels.keySet())  {
                    if (label.equals(tokens[1])) {
                        tokens[1] = Integer.toString(labels.get(label));
                    }
                }

                int address = Integer.parseInt(tokens[1]);
                instruction = new JumpInstruction(pc, commandCode, address);
                break;
        }

        return instruction;
    }
}
