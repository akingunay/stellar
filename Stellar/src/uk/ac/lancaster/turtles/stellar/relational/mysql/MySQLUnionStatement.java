package uk.ac.lancaster.turtles.stellar.relational.mysql;

import java.util.Set;

import uk.ac.lancaster.turtles.stellar.relational.RelationMetadata;
import uk.ac.lancaster.turtles.stellar.util.SetOperation;
import uk.ac.lancaster.turtles.stellar.util.StringBuilderHelper;

final class MySQLUnionStatement {

	private MySQLUnionStatement() {}
	
	public static String compile(RelationMetadata firstRelation, RelationMetadata secondRelation) {
		Set<String> newViewAttributes = SetOperation.intersection(firstRelation.getAttributes(), secondRelation.getAttributes());
		// TODO consider how to handle this
		if (newViewAttributes.isEmpty()) {
			throw new IllegalStateException("There is no common attribute for union.");
		}
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append(compileSelectClause(firstRelation.getName(), newViewAttributes)).append(" ");
		sqlStatement.append("FROM ").append(firstRelation.getName());
		sqlStatement.append(" UNION ");
		sqlStatement.append(compileSelectClause(secondRelation.getName(), newViewAttributes)).append(" ");
		sqlStatement.append("FROM ").append(secondRelation.getName());
		return sqlStatement.toString();
	}

	private static String compileSelectClause(String relationName, Set<String> attributes) {
		StringBuilder selectStatement = new StringBuilder("SELECT ");
		for (String parameter : attributes) {
			selectStatement.append(relationName).append(".").append(parameter).append(", ");
		}
		return StringBuilderHelper.trimStatement(selectStatement, ", ").toString();
	}
}
