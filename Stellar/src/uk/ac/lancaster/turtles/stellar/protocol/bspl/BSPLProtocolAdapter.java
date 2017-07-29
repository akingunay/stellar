package uk.ac.lancaster.turtles.stellar.protocol.bspl;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.ac.lancaster.turtles.stellar.communication.PlainStringSerializer;
import uk.ac.lancaster.turtles.stellar.communication.ip.ImmutableAgentDirectory;
import uk.ac.lancaster.turtles.stellar.communication.ip.datagram.BufferedDatagramReceiver;
import uk.ac.lancaster.turtles.stellar.communication.ip.datagram.DatagramSender;
import uk.ac.lancaster.turtles.stellar.relational.LocalHistory;

public abstract class BSPLProtocolAdapter {

	private final BSPLProtocolSchema protocolSchema;
	private final LocalHistory localHistory;
	private final BufferedDatagramReceiver receiver;
	private final BSPLIncomingMessageProcessor incomingMessageProcessor;
	private final Thread incomingMessageProcessorThread;
	private final DatagramSender sender;
	private final BSPLEnabledMessageRetriever enabledMessageRetriever;
	
	protected BSPLProtocolAdapter(BSPLProtocolSchema protocolSchema, LocalHistory localHistory, int port, ImmutableAgentDirectory directory) throws SocketException {
		this.protocolSchema = protocolSchema;
		this.localHistory = localHistory;
		enabledMessageRetriever = new BSPLEnabledMessageRetriever(protocolSchema, localHistory);
		receiver = new BufferedDatagramReceiver(port, new PlainStringSerializer());
		(new Thread(receiver)).start();
		incomingMessageProcessor = new BSPLIncomingMessageProcessor(receiver, protocolSchema, localHistory);
		incomingMessageProcessorThread = new Thread(incomingMessageProcessor);
		incomingMessageProcessorThread.start();
		sender = new DatagramSender(directory, new PlainStringSerializer());
	}
	
	protected final void sendMessage(BSPLMessage message) throws UnknownHostException {
		insertMessageIntoHistory(message);
		if (protocolSchema.getInParametersOfMessage(message.getName()).isEmpty()) {
			updateRoleBindings(message);
		}
		sender.send(message);
	}
	
	private void insertMessageIntoHistory(BSPLMessage message) {
		String insertStatement = localHistory.getSQLStatementCompiler().compileInsertStatement(message.getName(), message.getParameterValueBindings());
		System.out.println(insertStatement);
		localHistory.update(insertStatement);
	}
	
	// TODO this methos is identical to BSPLIncomingMessageProcessor.updateRoleBindings(BSPLMessage message)
	private void updateRoleBindings(BSPLMessage message) {
		Map<String, String> roleBindingsOfMessage = new HashMap<>();
		for (String parameter : protocolSchema.getKeyParametersOfMessage(message.getName())) {
			roleBindingsOfMessage.put(parameter, message.getValueOfParameter(parameter));
		}
		roleBindingsOfMessage.put(protocolSchema.getSenderRoleOfMessage(message.getName()), message.getSender());
		roleBindingsOfMessage.put(protocolSchema.getReceiverRoleOfMessage(message.getName()), message.getReceiver());
		String insertStatement = localHistory.getSQLStatementCompiler().compileInsertStatement(protocolSchema.getRelationNameForRoleBindings(), roleBindingsOfMessage);
		System.out.println(insertStatement);
		localHistory.update(insertStatement);
	}
	
	protected final Set<BSPLMessage> retrieveEnabledMessages(String messageName) {
		return enabledMessageRetriever.retrieve(messageName);
	}
	
	protected final Set<BSPLMessage> retrieveReceivedMessages(String messageName) {
		// TODO support it
		throw new UnsupportedOperationException();
	}
	
	public final void close() {
		sender.close();
		incomingMessageProcessorThread.interrupt();
		try {
			incomingMessageProcessorThread.join();
		} catch (InterruptedException ex) {
			// we should not reach here since the adapter is single threaded
		}
		receiver.close();
		localHistory.close();
	}
	
	abstract public String getAgentID();
	abstract protected void send(BSPLMessage message) throws UnknownHostException;

}
