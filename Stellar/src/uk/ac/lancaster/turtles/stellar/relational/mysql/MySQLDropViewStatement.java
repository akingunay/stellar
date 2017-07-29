package uk.ac.lancaster.turtles.stellar.relational.mysql;

import java.util.Set;

import uk.ac.lancaster.turtles.stellar.util.StringBuilderHelper;

final class MySQLDropViewStatement {

	private MySQLDropViewStatement() {}
	
	
	public static String compile(String viewName) {
		return "DROP VIEW IF EXISTS " + viewName;
	}
	
	public static String compile(Set<String> viewNames) {
		StringBuilder sqlStatement = new StringBuilder("DROP VIEW IF EXISTS ");
		for (String viewName : viewNames) {
			sqlStatement.append(viewName).append(", ");
		}
		return StringBuilderHelper.trimStatement(sqlStatement, ", ").toString(); 
	}
}
