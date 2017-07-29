package uk.ac.lancaster.turtles.stellar.communication.ip.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import uk.ac.lancaster.turtles.stellar.communication.ip.ImmutableAgentDirectory;
import uk.ac.lancaster.turtles.stellar.protocol.bspl.BSPLMessage;
import uk.ac.lancaster.turtles.stellar.protocol.bspl.BSPLMessageSerializerDeserializer;

public final class DatagramSender {

	protected static final int BUFFER_SIZE = 1024;
	
	private DatagramSocket socket;
	private BSPLMessageSerializerDeserializer serdes;
	private ImmutableAgentDirectory directory;
	
	public DatagramSender(ImmutableAgentDirectory directory, BSPLMessageSerializerDeserializer serdes) throws SocketException {
		this.socket = new DatagramSocket();
		this.serdes = serdes;
		this.directory = directory;
	}

	public void send(BSPLMessage bsplMessage) throws UnknownHostException  {
		if (socket == null) {
			throw new IllegalStateException();
		}
		if (directory.isAddressOfAgentKnown(bsplMessage.getReceiver())) {
			try {
				byte[] buffer = serdes.serialize(bsplMessage).getBytes();
				if (BUFFER_SIZE < buffer.length) {
					throw new IOException("Length of the message (" + buffer.length + " bytes) exceeds current maximum length limit of " +
							BUFFER_SIZE + " bytes. Increase maximum message length.");
				}
				socket.send(new DatagramPacket(buffer, buffer.length, directory.getAddressOfAgent(bsplMessage.getReceiver())));
			} catch (IOException e) {
				// simply terminate
			}
		} else {
			throw new UnknownHostException();
		}
		
	}

	public void close() {
		if (socket != null) {
			socket.close();
			socket = null;
		}
	}

}
