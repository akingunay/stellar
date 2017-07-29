package uk.ac.lancaster.turtles.stellar.protocol;

import java.util.Set;

public interface ProtocolMetadata {
	
	Set<String> getMessageNames();

	Set<String> getKeyParametersOfProtocol();
	Set<String> getKeyInParametersOfProtocol();
	Set<String> getKeyOutParametersOfProtocol();
	
	String getSenderRoleOfMessage(String messageName);
	String getReceiverRoleOfMessage(String messageName);
	
	Set<String> getKeyParametersOfMessage(String messageName);
	Set<String> getParametersOfMessage(String messageName);
	Set<String> getInParametersOfMessage(String messageName);
	Set<String> getOutParametersOfMessage(String messageName);
	Set<String> getNilParametersOfMessage(String messageName);
	Set<String> getNonkeyInParametersOfMessage(String messageName);
	
	boolean isMessageOfProtocol(String messageName);
	
	boolean isInitiatingMessageOfProtocol(String messageName);
	
	boolean isSenderRoleOfMessage(String role, String messageName);
	boolean isReceiverRoleOfMessage(String role, String messageName);
	
	boolean isParameterOfMessage(String parameter, String messageName);
	boolean isKeyParameterOfMessage(String parameter, String messageName);
	boolean isInParameterOfMessage(String parameter, String messageName);
	boolean isOutParameterOfMessage(String parameter, String messageName);
	boolean isNilParameterOfMessage(String parameter, String messageName);
}
