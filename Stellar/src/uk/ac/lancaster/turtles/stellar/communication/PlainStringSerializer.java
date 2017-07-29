package uk.ac.lancaster.turtles.stellar.communication;

import uk.ac.lancaster.turtles.stellar.protocol.bspl.BSPLMessage;
import uk.ac.lancaster.turtles.stellar.protocol.bspl.BSPLMessageSerializerDeserializer;

// <name>&<sender>&<receiver>&<parameter>=<value>&<parameter>=<value>&...
// TODO validation
public class PlainStringSerializer implements BSPLMessageSerializerDeserializer {

	private static final int NAME_INX = 0;
	private static final int SENDER_INX = 1;
	private static final int RECEIVER_INX = 2;
	private static final int PARAMETER_INX = 3;
	private static final String MESSAGE_ELEMENT_DELIMITER = "&";
	private static final String ASSIGNMENT = "=";
	
	@Override
	public String serialize(BSPLMessage bsplMessage) {
		StringBuilder stringMessage = new StringBuilder(bsplMessage.getName()).append(MESSAGE_ELEMENT_DELIMITER);
		stringMessage.append(bsplMessage.getSender()).append(MESSAGE_ELEMENT_DELIMITER);
		stringMessage.append(bsplMessage.getReceiver()).append(MESSAGE_ELEMENT_DELIMITER);
		for (String parameter : bsplMessage.getParameters()) {
			stringMessage.append(parameter).append(ASSIGNMENT).append(bsplMessage.getValueOfParameter(parameter)).append(MESSAGE_ELEMENT_DELIMITER);
		}
		System.out.println("serializing: " + stringMessage.toString());
		return stringMessage.delete(stringMessage.length() - MESSAGE_ELEMENT_DELIMITER.length(), stringMessage.length()).toString();
	}
	
	@Override
	public BSPLMessage deserialize(String stringMessage) {
		System.out.println("deserializing: " + stringMessage.toString());
		BSPLMessage bsplMessage = new BSPLMessage();
		String[] messageElements = stringMessage.split(MESSAGE_ELEMENT_DELIMITER);
		bsplMessage.setName(messageElements[NAME_INX]);
		bsplMessage.setSender(messageElements[SENDER_INX]);
		bsplMessage.setReceiver(messageElements[RECEIVER_INX]);
		for (int eInx = PARAMETER_INX ; eInx < messageElements.length ; eInx++) {
			String[] parameterValueBinding = messageElements[eInx].split(ASSIGNMENT);
			if (parameterValueBinding.length == 1) {
				bsplMessage.bindParameterToValue(parameterValueBinding[0], "");
			} else {
				bsplMessage.bindParameterToValue(parameterValueBinding[0], parameterValueBinding[1]);
			}		
		}
		return bsplMessage;
	}
}
