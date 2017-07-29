package uk.ac.lancaster.turtles.stellar.relational.mysql;

import java.util.Set;

import uk.ac.lancaster.turtles.stellar.util.StringBuilderHelper;

final class MySQLEmptyViewStatement {

	private MySQLEmptyViewStatement() {}
	
	static String compile(String newViewName, Set<String> attributes) {
		StringBuilder sqlStatement = new StringBuilder("CREATE VIEW ");
		sqlStatement.append(newViewName).append(" AS ");
		sqlStatement.append(compileSelectClause(attributes)).append(" ");
		return sqlStatement.append(" FROM DUAL WHERE FALSE").toString();
	}
	
	private static String compileSelectClause(Set<String> attributes) {
		StringBuilder selectClause = new StringBuilder("SELECT ");
		for (String attribute : attributes) {
			selectClause.append("'' AS ").append(attribute).append(", ");
		}
		return StringBuilderHelper.trimStatement(selectClause, ", ").toString();
	}
}
