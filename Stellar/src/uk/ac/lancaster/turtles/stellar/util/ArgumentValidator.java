package uk.ac.lancaster.turtles.stellar.util;

import java.util.Collection;

public final class ArgumentValidator {

	private ArgumentValidator() {}
	
	public static void validateNotNull(Object argument) {
		if (argument == null) {
			throw new NullPointerException();
		}
	}
	
	public static <E> void validateNotNull(Collection<E> argument) {
		if (argument == null) {
			throw new NullPointerException();
		}
	}
	
	public static void validateNotEmpty(String argument) {
		if (argument.isEmpty()) {
			throw new IllegalArgumentException();
		}		
	}
	
	public static <E> void validateNotEmpty(Collection<E> argument) {
		if (argument.isEmpty()) {
			throw new IllegalArgumentException();
		}		
	}
	
	public static void validateNotNullAndNotEmpty(String argument) {
		validateNotNull(argument);
		validateNotEmpty(argument);
	}

	public static <E> void validateNotNullAndNotEmpty(Collection<E> argument) {
		validateNotNull(argument);
		validateNotEmpty(argument);
	}
	
}
