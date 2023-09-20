package org.geri.simulatedDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DatabaseTable {

    private long currentId = 0;
    private final String name;
    private final String primaryKey;
    private final Set<String> columns;
    private final Map<Long, DatabaseRow> rows = new HashMap<>();

    public DatabaseTable(String name, String primaryKey, Set<String> columns) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.columns = columns;
    }

    public long getNextAvailableId() {
        return currentId + 1;
    }

    public void addToRows(DatabaseRow row, long id) {
        this.rows.put(id, row);
        this.currentId = id;
    }

    public void removeRow(DatabaseRow row) {
        // since values are stored a map its easier to remove by key,
        // so we find the key inside the attributes of the row.
        long key = Long.parseLong(row.getAttribute(this.getPrimaryKey()));
        this.rows.remove(key);
    }

    public String getName() {
        return name;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public Set<String> getColumns() {
        return columns;
    }

    public Map<Long, DatabaseRow> getRows() {
        return rows;
    }
}
