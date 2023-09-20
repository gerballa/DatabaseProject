package org.geri.instructions.model.impl;

import org.geri.instructions.model.Instruction;
import org.geri.instructions.model.InstructionType;

import java.util.HashMap;
import java.util.Map;

public class AddToResourceInstruction implements Instruction {

    private String resourceName;
    private final Map<String, String> values = new HashMap<>();

    public AddToResourceInstruction(String input) {
        readInstruction(input);
    }

    public void readInstruction(String input) {
        if (!input.startsWith("ADD TO")) {
            throw new IllegalArgumentException("Invalid instruction: " + input);
        }
        input = input.replace("ADD TO", "").trim();
        // we are extracting the resource name which will be from the beginning of the string to the first occurrence of a '('
        this.resourceName = input.substring(0, input.indexOf("(")).trim();
        if (resourceName.isEmpty()) {
            throw new IllegalArgumentException("Instruction missing resource name argument: " + input);
        }
        // we remove the resource name from the input string and the ';' at the end,
        // leaving us just with the column key value pairs
        input = input.replace(this.resourceName, "").replace(";", "").trim();
        String[] pairs = input.split("\\) \\(");
        for (String pair : pairs) {
            // Separating the pairs form this string: "(key,value)" to an array: [key, value]
            String[] keyValue = pair
                    .replace("(", "")
                    .replace(")", "")
                    .split(",");
            if (keyValue.length != 2)
                throw new IllegalArgumentException("Instruction missing arguments: " + input);
            // adding the key and value to our values map
            this.values.put(keyValue[0], keyValue[1]);
        }
    }

    public String getResourceName() {
        return resourceName;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public InstructionType getType() {
        return InstructionType.ADD;
    }
}
