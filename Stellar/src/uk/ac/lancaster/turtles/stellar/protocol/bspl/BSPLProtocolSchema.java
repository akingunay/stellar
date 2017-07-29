package uk.ac.lancaster.turtles.stellar.protocol.bspl;

import java.util.Set;

public interface BSPLProtocolSchema  {
	
	String getProtocolName();
	Set<String> getRolesOfProtocol();
	Set<String> getParametersOfProtocol();
	Set<String> getKeyParametersOfProtocol();
	Set<String> getInParametersOfProtocol();
	Set<String> getOutParametersOfProtocol();
	Set<String> getNilParametersOfProtocol();
	Set<String> getMessageNamesOfProtocol();

	String getSenderRoleOfMessage(String messageName);
	String getReceiverRoleOfMessage(String messageName);
	Set<String> getParametersOfMessage(String messageName);
	Set<String> getKeyParametersOfMessage(String messageName);
	Set<String> getInParametersOfMessage(String messageName);
	Set<String> getOutParametersOfMessage(String messageName);
	Set<String> getNilParametersOfMessage(String messageName);
	
	boolean isMessageNameOfProtocol(String messageName);
	boolean isParameterOfMessage(String parameter, String messageName);
	
	// TODO we should find a proper way to name and use the relation for role bindings
	// 		it should be somewhat configurable and also not available to client
	// TODO update code generation accordingly
	String getRelationNameForRoleBindings();

	// TODO following methods could be useful
//	boolean isInitiatingMessageOfProtocol(String messageName);
//	boolean isSenderRoleOfMessage(String role, String messageName);
//	boolean isReceiverRoleOfMessage(String role, String messageName);
//	boolean isKeyParameterOfMessage(String parameter, String messageName);
//	boolean isInParameterOfMessage(String parameter, String messageName);
//	boolean isOutParameterOfMessage(String parameter, String messageName);
//	boolean isNilParameterOfMessage(String parameter, String messageName);

	
}
