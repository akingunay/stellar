package uk.ac.lancaster.turtles.stellar.relational;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class RelationElement {

	public static final RelationElement EMPTY_RELATION_ELEMENT = new RelationElement(new String[0], new String[0]);
	
	private final Map<String, String> valuesOfAttributes;
	
	public RelationElement(String[] attributes, String[] values) {
		// TODO argument validation
		valuesOfAttributes = new HashMap<>();
		for (int i = 0 ; i < attributes.length ; i++) {
			valuesOfAttributes.put(attributes[i], values[i]);
		}
	}
	
	public String getAttributeValue(String attribute) {
		// TODO argument validataion
		return valuesOfAttributes.get(attribute);
	}
	
	public Set<String> getAttributes() {
		return new HashSet<>(valuesOfAttributes.keySet());
	}
	
	public int getAttributeCount() {
		return valuesOfAttributes.size();
	}
}
