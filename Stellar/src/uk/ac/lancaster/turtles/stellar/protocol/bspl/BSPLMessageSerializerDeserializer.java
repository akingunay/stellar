package uk.ac.lancaster.turtles.stellar.protocol.bspl;

public interface BSPLMessageSerializerDeserializer {

	public String serialize(BSPLMessage bsplMessage);
	public BSPLMessage deserialize(String stringMessage);
}
