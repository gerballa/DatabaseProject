package org.geri.instructions;

import java.io.*;

public class OutputLogger {

    private static final String FILE_NAME = "db_output.txt";

    public static void clearFile() {
        try {
            PrintWriter writer = new PrintWriter(FILE_NAME);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error occurred while clearing the file: " + e.getMessage());
        }
    }

    public static void addLineToFile(String line) {
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, true);
            PrintWriter writer = new PrintWriter(fileWriter);
            writer.println(line);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error occurred while adding a line to the file: " + e.getMessage());
        }
    }

}
