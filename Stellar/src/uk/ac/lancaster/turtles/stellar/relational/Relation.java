package uk.ac.lancaster.turtles.stellar.relational;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class Relation implements Iterable<RelationElement> {
	
	private final RelationMetadata metadata;
	private final Set<RelationElement> elements;
	
	public static Relation newRelation(RelationMetadata metadata, Set<RelationElement> elements) {
		validateArguments(metadata, elements);
		return new Relation(metadata, new HashSet<>(elements));
	}

	private static void validateArguments(RelationMetadata metadata, Set<RelationElement> elements) {
		if (metadata == null) {
			throw new NullPointerException();
		}
		if (elements == null) {
			throw new NullPointerException();
		}
		// FIXME equals does not work as intended, hence it is disabled
//		for (RelationElement element : elements) {
//			// TODO this is a quite expensive operation!
//			if (element.getAttributes().equals(metadata.getAttributes())) {
//				throw new InputMismatchException();
//			}
//		}
	}
	
	private Relation(RelationMetadata metadata, Set<RelationElement> elements) {
		this.metadata = metadata;
		this.elements = elements;
	}
	
	public Set<String> getAttributes() {
		return metadata.getAttributes();
	}
	
	public int getAttributeCount() {
		return metadata.getAttributeCount();
	}
	
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	public int getElementCount() {
		return elements.size();
	}
	
	public RelationElement getSingleElement() {
		if (elements.isEmpty()) {
			return RelationElement.EMPTY_RELATION_ELEMENT;
		} else {
			return elements.iterator().next();
		}
	}
	
	// TODO test this for mutability
	public Iterator<RelationElement> iterator() {
		return Collections.unmodifiableSet(elements).iterator();
	}

}
