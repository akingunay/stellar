package uk.ac.lancaster.turtles.stellar.util;

import java.util.HashSet;
import java.util.Set;

public final class SetOperation {
	
	private SetOperation() {}
	
	public static <T> Set<T> intersection(Set<T> firstSet, Set<T> secondSet) {
		Set<T> intersection = new HashSet<T>(firstSet);
		intersection.retainAll(secondSet);
		return intersection;
	}

	public static <T> Set<T> union(Set<T> firstSet, Set<T> secondSet) {
		Set<T> union = new HashSet<>(firstSet);
		union.addAll(secondSet);
		return union;
	}
	
	public static <T> Set<T> difference(Set<T> firstSet, Set<T> secondSet) {
		Set<T> difference = new HashSet<>(firstSet);
		difference.removeAll(secondSet);
		return difference;
	}
	
	public static <T> Set<T> createSingleton(T element) {
		Set<T> singletonSet = new HashSet<>();
		singletonSet.add(element);
		return singletonSet;
	}
}
