package uk.ac.lancaster.turtles.stellar.util;

public class StringBuilderHelper {

	public static StringBuilder trimStatement(StringBuilder builder, String stringToRemove) {
		return builder.delete(builder.length() - stringToRemove.length(), builder.length());
	}
}
