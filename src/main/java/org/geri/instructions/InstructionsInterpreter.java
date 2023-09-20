package org.geri.instructions;


import org.geri.instructions.model.Instruction;
import org.geri.instructions.model.impl.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InstructionsInterpreter {

    private List<Instruction> instructions;

    public InstructionsInterpreter() {
        this.instructions = new ArrayList<>();
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void readAndValidateInstructions() {
        List<String> instructionLines = readInstructionsFromFile();
        validateInstructionSyntax(instructionLines);
    }

    private List<String> readInstructionsFromFile() {
        System.out.println("Reading db_instructions.txt...");
        List<String> instructionLines = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/db_instructions.txt"))));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                if (!line.isEmpty()) {
                    instructionLines.add(line);
                }
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return instructionLines;
    }

    private void validateInstructionSyntax(List<String> instructionLines) {
        System.out.println("Validating instructions...");
        instructionLines.forEach(input -> {
            if (input.startsWith("CREATE RESOURCE")) {
                this.instructions.add(new CreateResourceInstruction(input));
            } else if (input.startsWith("ADD TO")) {
                this.instructions.add(new AddToResourceInstruction(input));
            } else if (input.startsWith("REMOVE FROM")) {
                this.instructions.add(new RemoveResourceInstruction(input));
            } else if (input.startsWith("SEARCH")) {
                this.instructions.add(new SearchResourceInstruction(input));
            }else if (input.startsWith("DB_STATE")) {
                this.instructions.add(new DbStateInstruction(input));
            } else {
                throw new IllegalArgumentException("Invalid instruction: " + input);
            }
        });
    }

}
