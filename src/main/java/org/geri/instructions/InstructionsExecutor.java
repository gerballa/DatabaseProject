package org.geri.instructions;

import org.geri.instructions.model.Instruction;
import org.geri.instructions.model.impl.AddToResourceInstruction;
import org.geri.instructions.model.impl.CreateResourceInstruction;
import org.geri.instructions.model.impl.RemoveResourceInstruction;
import org.geri.instructions.model.impl.SearchResourceInstruction;
import org.geri.simulatedDatabase.Database;
import org.geri.simulatedDatabase.DatabaseRow;
import org.geri.simulatedDatabase.DatabaseTable;

import java.util.List;

public class InstructionsExecutor {

    public static void executeInstructions(Database database, List<Instruction> instructions) {
        instructions.forEach(instruction -> {
            switch (instruction.getType()) {
                case CREATE -> executeCreateResourceInstruction(database, (CreateResourceInstruction) instruction);
                case ADD -> executeAddToResourceInstruction(database, (AddToResourceInstruction) instruction);
                case REMOVE -> executeRemoveResourceInstruction(database, (RemoveResourceInstruction) instruction);
                case SEARCH -> executeSearchResourceInstruction(database, (SearchResourceInstruction) instruction);
                case DB_STATE -> executeDbState(database);
            }
        });
    }

    private static void executeDbState(Database database) {
        for (DatabaseTable table : database.getTables()) {
            OutputLogger.addLineToFile("Resource:" + table.getName() + " " + table.getRows().size() + " rows");
            StringBuilder log = new StringBuilder();
            table.getRows().values().forEach(row -> {
                row.getAttributes().forEach((key, value) -> {
                    log.append(key).append(":").append(value).append(" ");
                });
                log.append(" | ");
            });
            OutputLogger.addLineToFile(log.toString());
        }
    }

    private static void executeCreateResourceInstruction(Database database, CreateResourceInstruction instruction) {
        // Check if table name is taken
        database.getTables().forEach(table -> {
            if (table.getName().equals(instruction.getResourceName())) {
                throw new IllegalArgumentException("Table with name '" + instruction.getResourceName() + "' already exists!");
            }
        });
        // Create the new table
        DatabaseTable table = new DatabaseTable(
                instruction.getResourceName(),
                instruction.getPrimaryKey(),
                instruction.getResourceColumns()
        );
        // Add it to the database
        database.addTable(table);
        // Add the output log
        OutputLogger.addLineToFile("Created resource:" + table.getName() + " in the database");
    }

    private static void executeAddToResourceInstruction(Database database, AddToResourceInstruction instruction) {
        // filter the database to find the table we are interested in, and put null if not found
        DatabaseTable requestedTable = database.getTables().stream()
                .filter(table -> table.getName().equals(instruction.getResourceName()))
                .findFirst().orElse(null);
        if (requestedTable == null) {
            throw new IllegalArgumentException("Table with name '" + instruction.getResourceName() + "' does not exist!");
        }
        // check if the columns of the table match the columns we want to add
        if (!requestedTable.getColumns().containsAll(instruction.getValues().keySet())) {
            throw new IllegalArgumentException("Instruction has columns that do not exist on table '" + requestedTable.getName() + "'!");
        }
        // Logic to calculate the primaryKey value
        long id = 0;
        // check if the instruction contains the primary key
        if (instruction.getValues().containsKey(requestedTable.getPrimaryKey())) {
            id = Long.parseLong(instruction.getValues().get(requestedTable.getPrimaryKey()));
        } else {
            // the command did not provide an id, so we provide the next available one.
            id = requestedTable.getNextAvailableId();
        }
        DatabaseRow databaseRow = new DatabaseRow();
        databaseRow.setAttributes(instruction.getValues());
        requestedTable.addToRows(databaseRow, id);
        // Add the output log
        OutputLogger.addLineToFile("Added row to resource:" + requestedTable.getName());
    }

    private static void executeRemoveResourceInstruction(Database database, RemoveResourceInstruction instruction) {
        // filter the database to find the table we are interested in, and put null if not found
        DatabaseTable requestedTable = database.getTables().stream()
                .filter(table -> table.getName().equals(instruction.getResourceName()))
                .findFirst().orElse(null);
        if (requestedTable == null) {
            throw new IllegalArgumentException("Table with name '" + instruction.getResourceName() + "' does not exist!");
        }
        if (!requestedTable.getColumns().contains(instruction.getSearchColumn())) {
            throw new IllegalArgumentException("Column with name '" + instruction.getSearchColumn() + "' does not exist on this table!");
        }
        DatabaseRow rowToRemove = requestedTable.getRows().values().stream()
                .filter(row -> row.getAttribute(instruction.getSearchColumn()).equals(instruction.getSearchValue()))
                .findFirst()
                .orElse(null);
        if (rowToRemove != null) {
            requestedTable.removeRow(rowToRemove);
        }
        // Add the output log
        OutputLogger.addLineToFile("Remove operation finished at resource:" + requestedTable.getName());
    }

    private static void executeSearchResourceInstruction(Database database, SearchResourceInstruction instruction) {
        // filter the database to find the table we are interested in, and put null if not found
        DatabaseTable requestedTable = database.getTables().stream()
                .filter(table -> table.getName().equals(instruction.getResourceName()))
                .findFirst().orElse(null);
        if (requestedTable == null) {
            throw new IllegalArgumentException("Table with name '" + instruction.getResourceName() + "' does not exist!");
        }
        if (!requestedTable.getColumns().contains(instruction.getSearchColumn())) {
            throw new IllegalArgumentException("Column with name '" + instruction.getSearchColumn() + "' does not exist on this table!");
        }
        List<DatabaseRow> foundRows = requestedTable.getRows().values().stream()
                .filter(row -> row.getAttribute(instruction.getSearchColumn()).equals(instruction.getSearchValue()))
                .toList();
        StringBuilder log = new StringBuilder();
        foundRows.forEach(row -> {
            row.getAttributes().forEach((key, value) -> {
                log.append(key).append(":").append(value).append(" ");
            });
            log.append(" | ");
        });
        OutputLogger.addLineToFile(log.toString());
    }
}
