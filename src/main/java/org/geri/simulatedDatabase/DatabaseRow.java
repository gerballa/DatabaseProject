package org.geri.simulatedDatabase;

import java.util.Map;

public class DatabaseRow {
	private Map<String, String> attributes;

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public String getAttribute(String key) {
		return attributes.get(key);
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
}
