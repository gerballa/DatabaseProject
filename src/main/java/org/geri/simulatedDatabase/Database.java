package org.geri.simulatedDatabase;

import java.util.ArrayList;
import java.util.List;

public class Database {
	private final List<DatabaseTable> tables;

	public Database() {
		this.tables = new ArrayList<>();
	}

	public List<DatabaseTable> getTables() {
		return tables;
	}

	public void addTable(DatabaseTable table) {
		this.tables.add(table);
	}
}
