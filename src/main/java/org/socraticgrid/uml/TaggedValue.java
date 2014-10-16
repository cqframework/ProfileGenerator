package org.socraticgrid.uml;

/**
 * Represents a Name-Value pair used to further
 * annotate a UML component.
 * 
 * @author cnanjo
 *
 */
public class TaggedValue {
	
	public String key;
	public String value;

	public TaggedValue(String key) {
		this.key = key;
	}
	
	public TaggedValue(String key, String value) {
		this(key);
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder(key);
		if(value != null) {
			builder.append("=").append(value);
		}
		return builder.toString();
	}
}
