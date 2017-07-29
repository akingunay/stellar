package uk.ac.lancaster.turtles.stellar.relational;

import java.util.HashSet;
import java.util.Set;

public final class RelationMetadata {

	public static final String ANONYMOUS_RELATION_NAME = "";
	
	private String name;
	private Set<String> attributes;
	
	public RelationMetadata(String name, Set<String> attributes) {
		if (name == null) {
			throw new NullPointerException();
		}
		if (attributes == null) {
			throw new NullPointerException();
		}
		this.name = name;
		this.attributes = attributes;
	}
	
	public String getName() {
		return name;
	}
	
	public Set<String> getAttributes() {
		return new HashSet<>(attributes);
	}
	
	public int getAttributeCount() {
		return attributes.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelationMetadata other = (RelationMetadata) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
