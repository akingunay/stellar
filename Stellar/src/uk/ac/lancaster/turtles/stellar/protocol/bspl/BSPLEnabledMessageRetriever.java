package uk.ac.lancaster.turtles.stellar.protocol.bspl;

import java.util.HashSet;
import java.util.Set;

import uk.ac.lancaster.turtles.stellar.relational.LocalHistory;
import uk.ac.lancaster.turtles.stellar.relational.Relation;
import uk.ac.lancaster.turtles.stellar.relational.RelationElement;
import uk.ac.lancaster.turtles.stellar.relational.RelationMetadata;
import uk.ac.lancaster.turtles.stellar.relational.SQLStatementCompiler;
import uk.ac.lancaster.turtles.stellar.util.SetOperation;

public class BSPLEnabledMessageRetriever {

	private final BSPLProtocolSchema protocolSchema;
	private final SQLStatementCompiler sqlStatementCompiler;
	private final Set<String> viewsForGarbageCollection;
	private final LocalHistory history;
	
	private int viewCounter;
	
	public BSPLEnabledMessageRetriever(BSPLProtocolSchema protocolSchema, LocalHistory history) {
		this.protocolSchema = protocolSchema;
		this.history = history;
		sqlStatementCompiler = history.getSQLStatementCompiler();
		viewsForGarbageCollection = new HashSet<>();
		viewCounter = 0;
	}
	
	public Set<BSPLMessage> retrieve(String messageName) {
		Set<String> keyInParametersOfMessage = SetOperation.intersection(protocolSchema.getKeyParametersOfMessage(messageName), protocolSchema.getInParametersOfMessage(messageName));
		Set<String> keyOutParametersOfMessage = SetOperation.intersection(protocolSchema.getKeyParametersOfMessage(messageName), protocolSchema.getOutParametersOfMessage(messageName));
		if (!keyInParametersOfMessage.isEmpty() && !keyOutParametersOfMessage.isEmpty()) {
			return retrieveEnabledMessagesForMessageSchemaWithBothInKeyAndOutKeyParameters(messageName);
		} else {
			if (keyInParametersOfMessage.isEmpty()) {
				// TODO the current implementation does not reach here since the generated adapter creates a message 
				// 		object automatically when it realizes that all parameters of a message are adorned OUT
				return retrieveEnabledMessagesForMessageSchemaWithOnlyOutKeyParamaters(messageName);
			} else {
				return retrieveEnabledMessagesForMessageSchemaWithOnlyInKeyParameters(messageName);
			}
		}
	}
	
	private Set<BSPLMessage> retrieveEnabledMessagesForMessageSchemaWithBothInKeyAndOutKeyParameters(String messageName) {
		RelationMetadata tuplesToInclude = findTuplesToInclude(messageName);
		Set<BSPLMessage> enabledMessages = createEnabledMessages(messageName, tuplesToInclude);
		viewsForGarbageCollection.add(tuplesToInclude.getName());
		garbageCollectViews();
		return enabledMessages;
	}
	
	private Set<BSPLMessage> retrieveEnabledMessagesForMessageSchemaWithOnlyOutKeyParamaters(String mssageName) {
		// such a message should not involve any IN parameters
		// just generate a unique key for all out parameters
		BSPLMessage enabledMessage = new BSPLMessage();
		enabledMessage.setName(mssageName);
		Set<BSPLMessage> enabledMessages = new HashSet<>();
		enabledMessages.add(enabledMessage);
		return enabledMessages;
	}
	
	private Set<BSPLMessage> retrieveEnabledMessagesForMessageSchemaWithOnlyInKeyParameters(String messageName) {
		RelationMetadata tuplesToInclude = findTuplesToInclude(messageName);
		// TODO refactor the following for loop
		for (String outOrNilParameter : SetOperation.union(protocolSchema.getOutParametersOfMessage(messageName), protocolSchema.getNilParametersOfMessage(messageName))) {
			RelationMetadata knownValuesOfParameter = findKnownValuesOfParameter(outOrNilParameter);
			tuplesToInclude = excludeKnownTuples(tuplesToInclude, knownValuesOfParameter);
		}
		Set<BSPLMessage> enabledMessages = createEnabledMessages(messageName, tuplesToInclude);
		viewsForGarbageCollection.add(tuplesToInclude.getName());
		garbageCollectViews();
		return enabledMessages;
	}

	private RelationMetadata findTuplesToInclude(String messageName) {
		RelationMetadata tuplesToInclude = null;
		for (String inParameter : protocolSchema.getInParametersOfMessage(messageName)) {
			RelationMetadata knownValuesOfParameter = findKnownValuesOfParameter(inParameter);
			// TODO if there is no known value of parameter, return empty set (i.e., at least one in parameter has no bound value)
			if (tuplesToInclude == null) {
				tuplesToInclude = knownValuesOfParameter;
			} else {
				tuplesToInclude = joinRelations(tuplesToInclude, knownValuesOfParameter);
			}
		}
		return tuplesToInclude;
	}
	
	private RelationMetadata findKnownValuesOfParameter(String parameter) {
		Set<String> determinantOfParameter = findDeterminantOfParameter(parameter);
		RelationMetadata knownValuesOfParameter = createEmptyView(SetOperation.union(determinantOfParameter, SetOperation.createSingleton(parameter)));
		for (String message : protocolSchema.getMessageNamesOfProtocol()) {
			if (protocolSchema.isParameterOfMessage(parameter, message)) {
				knownValuesOfParameter = addValues(knownValuesOfParameter, message);
			}
		}
		return knownValuesOfParameter;
	}
	
	private RelationMetadata addValues(RelationMetadata relation, String message) {
		String nextFreeViewName = getNextFreeViewName();
		RelationMetadata relationOfMessage = new RelationMetadata(message, protocolSchema.getParametersOfMessage(message));
		String unionViewStatement = sqlStatementCompiler.compileUnionViewStatement(nextFreeViewName, relation, relationOfMessage);
		history.update(unionViewStatement);
		System.out.println(unionViewStatement);	// TODO logger
		viewsForGarbageCollection.add(relation.getName());
		return new RelationMetadata(nextFreeViewName, relation.getAttributes());
	}
	
	private Set<String> findDeterminantOfParameter(String parameter) {
		Set<String> determinantParameters = protocolSchema.getKeyParametersOfProtocol();
		// TODO if the following for-if pattern is common, we may have a method getMessagesWithParameter() in protocol metadata
		for (String message : protocolSchema.getMessageNamesOfProtocol()) {
			if (protocolSchema.isParameterOfMessage(parameter, message)) {
				determinantParameters = SetOperation.intersection(determinantParameters, protocolSchema.getKeyParametersOfMessage(message));
			}
		}
		return determinantParameters;
	}
	
	private RelationMetadata createEmptyView(Set<String> attributes) {
		String nextFreeViewName = getNextFreeViewName();
		String emptyViewStatement = sqlStatementCompiler.compileCreateEmptyViewStatement(nextFreeViewName, attributes);
		history.update(emptyViewStatement);
		System.out.println(emptyViewStatement);	// TODO logger
		return new RelationMetadata(nextFreeViewName, attributes);
	}
	
	private RelationMetadata joinRelations(RelationMetadata firstRelation, RelationMetadata secondRelation) {
		String nextFreeViewName = getNextFreeViewName();
		String joinViewStatement = sqlStatementCompiler.compileJoinViewStatement(nextFreeViewName, firstRelation, secondRelation);
		history.update(joinViewStatement);
		System.out.println(joinViewStatement);	// TODO logger
		Set<String> newViewAttributes = SetOperation.union(firstRelation.getAttributes(), secondRelation.getAttributes());
		viewsForGarbageCollection.add(firstRelation.getName());
		viewsForGarbageCollection.add(secondRelation.getName());
		return new RelationMetadata(nextFreeViewName, newViewAttributes);
	}
	
	private RelationMetadata excludeKnownTuples(RelationMetadata firstRelation, RelationMetadata secondRelation) {
		String nextFreeViewName = getNextFreeViewName();
		String leftJoinViewStatement = sqlStatementCompiler.compileLeftJoinViewStatement(nextFreeViewName, firstRelation, secondRelation);
		history.update(leftJoinViewStatement);
		System.out.println(leftJoinViewStatement);	// TODO logger
		Set<String> newViewAttributes = firstRelation.getAttributes();
		viewsForGarbageCollection.add(firstRelation.getName());
		viewsForGarbageCollection.add(secondRelation.getName());
		return new RelationMetadata(nextFreeViewName, newViewAttributes);
	}
	
	private Set<BSPLMessage> createEnabledMessages(String messageName, RelationMetadata tuplesToInclude) {
		System.out.println(sqlStatementCompiler.compileJoinStatement(tuplesToInclude, getRelationMetadataForRoleBindings(messageName))); // TODO logger
		Relation tuples = history.query(sqlStatementCompiler.compileJoinStatement(tuplesToInclude, getRelationMetadataForRoleBindings(messageName)));
		Set<BSPLMessage> enabledMessages = new HashSet<>();
		for (RelationElement tuple : tuples) {
			BSPLMessage enabledMessage = new BSPLMessage();
			enabledMessage.setName(messageName);
			enabledMessage.setSender(tuple.getAttributeValue(protocolSchema.getSenderRoleOfMessage(messageName)));
			enabledMessage.setReceiver(tuple.getAttributeValue(protocolSchema.getReceiverRoleOfMessage(messageName)));
			for (String attribute : SetOperation.difference(tuple.getAttributes(), getRoleAttributes(messageName))) {
				enabledMessage.bindParameterToValue(attribute, tuple.getAttributeValue(attribute));
			}
			enabledMessages.add(enabledMessage);
		}
		return enabledMessages;
	}
	
	private RelationMetadata getRelationMetadataForRoleBindings(String messageName) {
		Set<String> attributes = new HashSet<>(protocolSchema.getKeyParametersOfProtocol());
		attributes.addAll(getRoleAttributes(messageName));
		return new RelationMetadata(protocolSchema.getRelationNameForRoleBindings(), attributes);
	}
	
	private Set<String> getRoleAttributes(String messageName) {
		Set<String> roles = new HashSet<>();
		roles.add(protocolSchema.getSenderRoleOfMessage(messageName));
		roles.add(protocolSchema.getReceiverRoleOfMessage(messageName));
		return roles;
	}
	
	private void garbageCollectViews() {
		String dropViewStatement = sqlStatementCompiler.compileDropViewStatement(viewsForGarbageCollection);
		history.update(dropViewStatement);
		System.out.println(dropViewStatement);	// TODO logger
		viewsForGarbageCollection.clear();
		viewCounter = 0;
	}
	
	private String getNextFreeViewName() {
		return "v" + ++viewCounter;
	}
}
