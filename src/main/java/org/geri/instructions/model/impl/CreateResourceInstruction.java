package org.geri.instructions.model.impl;

import org.geri.instructions.model.Instruction;
import org.geri.instructions.model.InstructionType;
import org.geri.utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateResourceInstruction implements Instruction {

    private String resourceName;
    private String primaryKey;
    private Set<String> resourceColumns;

    public CreateResourceInstruction(String input) {
        readInstruction(input);
    }

    public void readInstruction(String input) {
        if (!input.startsWith("CREATE RESOURCE")) {
            throw new IllegalArgumentException("Invalid instruction: " + input);
        }
        input = input.replace("CREATE RESOURCE", "").trim();
        String[] tokens = input.split(" ");
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Instruction missing arguments: " + input);
        }
        if (!tokens[1].startsWith("(") ||
                !tokens[1].endsWith(");") || // resource arguments should start with '(' and end with ');'
                !tokens[0].matches("^[A-Za-z_]+$") // resource name should contain only letters and underscores
        ) {
            throw new IllegalArgumentException("Invalid syntax for CREATE RESOURCE instruction: " + input);
        }
        this.resourceName = tokens[0];
        // remove '(' and ');' from resource arguments and split them by comma, cast them to List
        List<String> columns = Arrays.stream(
                tokens[1].replace("(", "").replace(");", "").split(",")
        ).filter(col -> !col.equals("")).toList();
        if (columns.isEmpty())
            throw new IllegalArgumentException("At least 1 resource column needed: " + input);
        if (!Utils.containsUniqueElements(columns))
            throw new IllegalArgumentException("Resource columns should be unique: " + input);
        if (columns.stream().filter(res -> res.endsWith("_PK")).count() != 1)
            throw new IllegalArgumentException("Resource columns should have (only one) primary key with the suffix '_PK': " + input);
        this.resourceColumns = columns.stream()
                .map(col -> {
                    // here we are finding the primary key, removing the suffix "_PK" because we don't need it anymore,
                    // and saving the primary key in a separate value for easier access.
                    if (col.endsWith("_PK")) {
                        col = col.replace("_PK", "");
                        this.primaryKey = col;
                    }
                    // the rest of the columns are returned as they are
                    return col;
                }).collect(Collectors.toSet());
    }

    public InstructionType getType() {
        return InstructionType.CREATE;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public Set<String> getResourceColumns() {
        return resourceColumns;
    }
}
