package uk.ac.lancaster.turtles.stellar.communication.ip.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import uk.ac.lancaster.turtles.stellar.protocol.bspl.BSPLMessage;
import uk.ac.lancaster.turtles.stellar.protocol.bspl.BSPLMessageSerializerDeserializer;


public final class BufferedDatagramReceiver implements Runnable {

	protected static final int BUFFER_SIZE = 1024;
	
	private DatagramSocket socket;
	private final BSPLMessageSerializerDeserializer serdes;
	private final BlockingQueue<BSPLMessage> messageBuffer;
	
	public BufferedDatagramReceiver(int port, BSPLMessageSerializerDeserializer serdes) throws SocketException {
		this.socket = new DatagramSocket(port);
		this.serdes = serdes;
		this.messageBuffer = new LinkedBlockingQueue<>();
	}
	
	// TODO ensure proper termination of socket
	@Override
	public void run() {
		byte[] datagramBuffer = new byte[BUFFER_SIZE];
		DatagramPacket datagramPacket = new DatagramPacket(datagramBuffer, datagramBuffer.length);
        while (true) {
            try {
                socket.receive(datagramPacket);
            } catch (IOException e) {
            	break;
            }
            try {
				messageBuffer.put(serdes.deserialize(new String(datagramPacket.getData(), 0, datagramPacket.getLength())));
			} catch (InterruptedException e) {
				break;
			}
        }
	}
	
	public BSPLMessage receive() throws InterruptedException {
		return messageBuffer.take();
	}
	
	public void close() {
		if (socket != null) {
			socket.close();
			socket = null;
		}
	}
}
