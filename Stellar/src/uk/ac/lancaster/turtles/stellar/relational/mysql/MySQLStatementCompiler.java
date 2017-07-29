package uk.ac.lancaster.turtles.stellar.relational.mysql;

import java.util.Map;
import java.util.Set;

import uk.ac.lancaster.turtles.stellar.relational.RelationMetadata;
import uk.ac.lancaster.turtles.stellar.relational.SQLStatementCompiler;
import uk.ac.lancaster.turtles.stellar.util.ArgumentValidator;

// TODO make this singleton
public class MySQLStatementCompiler implements SQLStatementCompiler {

	@Override
	public String compileJoinStatement(RelationMetadata firstRelation, RelationMetadata secondRelation) {
		return MySQLJoinStatement.compile(firstRelation, secondRelation);
	}
	
	@Override
	public String compileJoinViewStatement(String newViewName, RelationMetadata firstRelation, RelationMetadata secondRelation) {
		validateArguments(newViewName, firstRelation, secondRelation);
		return new StringBuilder("CREATE VIEW ")
				.append(newViewName)
				.append(" AS ")
				.append(MySQLJoinStatement.compile(firstRelation, secondRelation)).toString();
	}

	@Override
	public String compileLeftJoinViewStatement(String newViewName, RelationMetadata firstRelation, RelationMetadata secondRelation) {
		validateArguments(newViewName, firstRelation, secondRelation);
		return new StringBuilder("CREATE VIEW ")
				.append(newViewName)
				.append(" AS ")
				.append(MySQLLeftJoinStatement.compile(firstRelation, secondRelation)).toString();
	}

	@Override
	public String compileUnionViewStatement(String newViewName, RelationMetadata firstRelation, RelationMetadata secondRelation) {
		validateArguments(newViewName, firstRelation, secondRelation);
		return new StringBuilder("CREATE VIEW ")
				.append(newViewName)
				.append(" AS ")
				.append(MySQLUnionStatement.compile(firstRelation, secondRelation)).toString();
	}
	
	@Override
	public String compileCreateEmptyViewStatement(String newViewName, Set<String> attributes) {
		ArgumentValidator.validateNotNullAndNotEmpty(newViewName);
		ArgumentValidator.validateNotNull(attributes);
		return MySQLEmptyViewStatement.compile(newViewName, attributes);
	}

	@Override
	public String compileDropViewStatement(String viewName) {
		ArgumentValidator.validateNotNullAndNotEmpty(viewName);
		return MySQLDropViewStatement.compile(viewName);
	}

	@Override
	public String compileDropViewStatement(Set<String> viewNames) {
		ArgumentValidator.validateNotNullAndNotEmpty(viewNames);
		return MySQLDropViewStatement.compile(viewNames);
	}
	
	private static void validateArguments(String newViewName, RelationMetadata firstRelation, RelationMetadata secondRelation) {
		ArgumentValidator.validateNotNullAndNotEmpty(newViewName);
		ArgumentValidator.validateNotNull(firstRelation);
		ArgumentValidator.validateNotNull(secondRelation);
	}

	@Override
	public String compileInsertStatement(String relationName, Map<String, String> attributeVlaueBindings) {
		// TODO validate arguments
		return MySQLInsertStatement.compile(relationName, attributeVlaueBindings);
	}

}
