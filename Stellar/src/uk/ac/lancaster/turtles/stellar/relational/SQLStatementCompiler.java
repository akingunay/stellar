package uk.ac.lancaster.turtles.stellar.relational;

import java.util.Map;
import java.util.Set;

public interface SQLStatementCompiler {

	String compileJoinStatement(RelationMetadata firstRelation, RelationMetadata secondRelation);
	String compileJoinViewStatement(String newViewName, RelationMetadata firstRelation, RelationMetadata secondRelation);
	String compileLeftJoinViewStatement(String newViewName, RelationMetadata firstRelation, RelationMetadata secondRelation);
	String compileUnionViewStatement(String newViewName, RelationMetadata firstRelation, RelationMetadata secondRelation);
	String compileCreateEmptyViewStatement(String newViewName, Set<String> attributes);
	String compileDropViewStatement(String viewName);
	String compileDropViewStatement(Set<String> viewNames);
	String compileInsertStatement(String relationName, Map<String, String> attributeVlaueBindings);
	
}
