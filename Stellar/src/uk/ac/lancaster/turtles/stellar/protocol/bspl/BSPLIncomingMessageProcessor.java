package uk.ac.lancaster.turtles.stellar.protocol.bspl;

import java.util.HashMap;
import java.util.Map;

import uk.ac.lancaster.turtles.stellar.communication.ip.datagram.BufferedDatagramReceiver;
import uk.ac.lancaster.turtles.stellar.relational.LocalHistory;

public class BSPLIncomingMessageProcessor implements Runnable {

	private final BufferedDatagramReceiver receiver;
	private final BSPLProtocolSchema schema;
	private final LocalHistory history;
	
	public BSPLIncomingMessageProcessor(BufferedDatagramReceiver receiver, BSPLProtocolSchema schema, LocalHistory history) {
		this.receiver = receiver;
		this.schema = schema;
		this.history = history;
	}
	
	@Override
	public void run() {
		BSPLMessage message = null;
		while (true) {
			try {
				message = receiver.receive();
			} catch (InterruptedException e) {
				break;
			}
			if (schema.getInParametersOfMessage(message.getName()).isEmpty()) {
				updateRoleBindings(message);
			}
			String insertStatement = history.getSQLStatementCompiler().compileInsertStatement(message.getName(), message.getParameterValueBindings());
			history.update(insertStatement);
		}
	}
	
	// TODO this methos is identical to BSPLProtocolAdapter.updateRoleBindings(BSPLMessage message)
	private void updateRoleBindings(BSPLMessage message) {
		Map<String, String> roleBindingsOfMessage = new HashMap<>();
		for (String parameter : schema.getKeyParametersOfMessage(message.getName())) {
			roleBindingsOfMessage.put(parameter, message.getValueOfParameter(parameter));
		}
		roleBindingsOfMessage.put(schema.getSenderRoleOfMessage(message.getName()), message.getSender());
		roleBindingsOfMessage.put(schema.getReceiverRoleOfMessage(message.getName()), message.getReceiver());
		String insertStatement = history.getSQLStatementCompiler().compileInsertStatement(schema.getRelationNameForRoleBindings(), roleBindingsOfMessage);
		System.out.println(insertStatement);
		history.update(insertStatement);
	}
}
