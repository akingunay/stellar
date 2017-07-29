package uk.ac.lancaster.turtles.stellar.relational.mysql;

import java.util.Set;

import uk.ac.lancaster.turtles.stellar.relational.RelationMetadata;
import uk.ac.lancaster.turtles.stellar.util.SetOperation;
import uk.ac.lancaster.turtles.stellar.util.StringBuilderHelper;

final class MySQLLeftJoinStatement {
	
	private MySQLLeftJoinStatement() {}
	
	public static String compile(RelationMetadata firstRelation, RelationMetadata secondRelation) {
		Set<String> keysOfNewView = SetOperation.intersection(firstRelation.getAttributes(), secondRelation.getAttributes());
		// TODO consider how to handle this
		if (keysOfNewView.isEmpty()) {
			throw new IllegalStateException("There is no common attribute for join.");
		}
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append(compileSelectClause(firstRelation)).append(" ");
		sqlStatement.append(compileFromClause(firstRelation)).append(" ");
		sqlStatement.append(compileLeftJoinClause(firstRelation, secondRelation)).append(" ");
		sqlStatement.append(compileWhereClause(secondRelation));
		return sqlStatement.toString();
	}
	
	private static String compileSelectClause(RelationMetadata relation) {
		StringBuilder selectClause = new StringBuilder("SELECT ");
		for (String attribute : relation.getAttributes()) {
			selectClause.append(relation.getName()).append(".").append(attribute).append(", ");
		}
		return StringBuilderHelper.trimStatement(selectClause, ", ").toString();
	}

	private static String compileFromClause(RelationMetadata relation) {
		return "FROM " + relation.getName();
	}

	private static String compileLeftJoinClause(RelationMetadata firstRelation, RelationMetadata secondRelation) {
		StringBuilder leftJoinClause = new StringBuilder("LEFT JOIN ");
		leftJoinClause.append(secondRelation.getName()).append(" ON ");
		for (String attribute : SetOperation.intersection(firstRelation.getAttributes(), secondRelation.getAttributes())) {
			leftJoinClause.append(firstRelation.getName()).append(".").append(attribute).append(" = ").
						append(secondRelation.getName()).append(".").append(attribute).append(" AND ");
		}
		return StringBuilderHelper.trimStatement(leftJoinClause, " AND ").toString();
	}
	
	private static String compileWhereClause(RelationMetadata relation) {
		StringBuilder whereClause = new StringBuilder("WHERE ");
		for (String attribute : relation.getAttributes()) {
			whereClause.append(relation.getName()).append(".").append(attribute).append(" IS NULL AND ");
		}
		return StringBuilderHelper.trimStatement(whereClause, " AND ").toString();
	}
}

