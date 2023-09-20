package org.geri.instructions.model.impl;

import org.geri.instructions.model.Instruction;
import org.geri.instructions.model.InstructionType;

public class SearchResourceInstruction implements Instruction {

    private String resourceName;
    private String searchColumn;
    private String searchValue;

    public SearchResourceInstruction(String input) {
        readInstruction(input);
    }

    public void readInstruction(String input) {
        if (!input.startsWith("SEARCH")) {
            throw new IllegalArgumentException("Invalid instruction: " + input);
        }
        input = input.replace("SEARCH", "").trim();
        // we split the remaining input into 'resourceName' and 'column=value'
        String[] tokens = input.split(" with ");
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Instruction missing arguments: " + input);
        }
        this.resourceName = tokens[0];
        // separate 'column=value' into 'column' and 'value'
        String[] keyValuePair = tokens[1].split("=");
        if (keyValuePair.length != 2) {
            throw new IllegalArgumentException("Instruction missing arguments: " + input);
        }
        this.searchColumn = keyValuePair[0];
        this.searchValue = keyValuePair[1];
    }

    public InstructionType getType() {
        return InstructionType.SEARCH;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getSearchColumn() {
        return searchColumn;
    }

    public String getSearchValue() {
        return searchValue;
    }
}
