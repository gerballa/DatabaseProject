package org.geri;

import org.geri.instructions.InstructionsExecutor;
import org.geri.instructions.InstructionsInterpreter;
import org.geri.instructions.OutputLogger;
import org.geri.simulatedDatabase.Database;


public class Main {
    public static void main(String[] args) {
        System.out.println("Database Simulation project");
        try {
            // clear the output file
            OutputLogger.clearFile();

            Database database = new Database();
            InstructionsInterpreter instructionsInterpreter = new InstructionsInterpreter();
            // this will create the appropriate instruction objects and add them to the list stored
            // in the instructionsInterpreter object
            instructionsInterpreter.readAndValidateInstructions();
            InstructionsExecutor.executeInstructions(database, instructionsInterpreter.getInstructions());
            System.out.println("Instructions executed successfully!");
        } catch (Exception e) {
            System.out.println("Instructions were invalid!");
            OutputLogger.addLineToFile("Invalid operation");
            throw e;
        }
    }

}
