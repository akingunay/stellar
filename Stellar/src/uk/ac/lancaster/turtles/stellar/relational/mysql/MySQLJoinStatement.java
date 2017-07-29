package uk.ac.lancaster.turtles.stellar.relational.mysql;

import java.util.Set;

import uk.ac.lancaster.turtles.stellar.relational.RelationMetadata;
import uk.ac.lancaster.turtles.stellar.util.SetOperation;
import uk.ac.lancaster.turtles.stellar.util.StringBuilderHelper;

final class MySQLJoinStatement {

	private MySQLJoinStatement() {}

	public static String compile(RelationMetadata firstRelation, RelationMetadata secondRelation) {
		Set<String> keysOfNewView = SetOperation.intersection(firstRelation.getAttributes(), secondRelation.getAttributes());
		// TODO consider how to handle this
		if (keysOfNewView.isEmpty()) {
			throw new IllegalStateException("There is no common attribute for join.");
		}
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append(compileSelectClause(firstRelation, secondRelation)).append(" ");
		sqlStatement.append(compileFromClause(firstRelation, secondRelation)).append(" ");
		sqlStatement.append(compileWhereClause(firstRelation, secondRelation));
		return sqlStatement.toString();
	}

	private static String compileSelectClause(RelationMetadata firstRelation, RelationMetadata secondRelation) {
		StringBuilder selectClause = new StringBuilder("SELECT ");
		Set<String> commonAttributes = SetOperation.intersection(firstRelation.getAttributes(), secondRelation.getAttributes());
		for (String attribute : commonAttributes) {
			selectClause.append(firstRelation.getName()).append(".").append(attribute).append(", ");
		}
		for (String attribute : SetOperation.difference(firstRelation.getAttributes(), commonAttributes)) {
			selectClause.append(firstRelation.getName()).append(".").append(attribute).append(", ");
		}
		for (String attribute : SetOperation.difference(secondRelation.getAttributes(), commonAttributes)) {
			selectClause.append(secondRelation.getName()).append(".").append(attribute).append(", ");
		}
		return StringBuilderHelper.trimStatement(selectClause, ", ").toString();
	}
	
	private static String compileFromClause(RelationMetadata firstRelation, RelationMetadata secondRelation) {
		return "FROM " + firstRelation.getName() + ", " + secondRelation.getName();
	}
	
	private static String compileWhereClause(RelationMetadata firstRelation, RelationMetadata secondRelation) {
		StringBuilder whereClause = new StringBuilder("WHERE ");
		for (String attribute : SetOperation.intersection(firstRelation.getAttributes(), secondRelation.getAttributes())) {
			whereClause.append(firstRelation.getName()).append(".").append(attribute).append(" = ").
						append(secondRelation.getName()).append(".").append(attribute).append(" AND ");
		}
		return StringBuilderHelper.trimStatement(whereClause, " AND ").toString();
	}
	
}
