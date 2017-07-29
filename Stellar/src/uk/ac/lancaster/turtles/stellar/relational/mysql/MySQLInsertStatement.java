package uk.ac.lancaster.turtles.stellar.relational.mysql;

import java.util.Map;

import uk.ac.lancaster.turtles.stellar.util.StringBuilderHelper;

class MySQLInsertStatement {

	private MySQLInsertStatement() {}

	public static String compile(String relationName, Map<String, String> attributeVlaueBindings) {
		StringBuilder sqlStatement = new StringBuilder("INSERT INTO ").append(relationName).append("(");
		// We need a fixed ordering of attributes to compile a proper sql statement where the ordering of attributes
		// and corresponding values match.
		String[] attributes = attributeVlaueBindings.keySet().toArray(new String[attributeVlaueBindings.keySet().size()]);
		for (String attribute : attributes) {
			sqlStatement.append(attribute).append(", ");
		}
		StringBuilderHelper.trimStatement(sqlStatement, ", ").append(") VALUES (");
		for (String attribute : attributes) {
			sqlStatement.append("\"").append(attributeVlaueBindings.get(attribute)).append("\"").append(", ");
		}
		return StringBuilderHelper.trimStatement(sqlStatement, ", ").append(")").toString();
	}
}
