package org.geri.instructions.model.impl;

import org.geri.instructions.model.Instruction;
import org.geri.instructions.model.InstructionType;


public class DbStateInstruction implements Instruction {
    public DbStateInstruction(String input) {
        this.readInstruction(input);
    }

    public void readInstruction(String input) {
        if (!input.equals("DB_STATE")) {
            throw new IllegalArgumentException("Invalid instruction: " + input);
        }
    }

    public InstructionType getType() {
        return InstructionType.DB_STATE;
    }
}
