package uk.ac.lancaster.turtles.stellar.protocol.bspl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.ac.lancaster.turtles.stellar.util.ArgumentValidator;

public class BSPLMessage {

	private String name;
	private String sender;
	private String receiver;
	private Map<String, String> parameterValueBindings;
	
	public BSPLMessage() {
		name = null;
		sender = null;
		receiver = null;
		parameterValueBindings = new HashMap<String, String>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getReceiver() {
		return receiver;
	}
	
	public String getValueOfParameter(String parameter) {
		return parameterValueBindings.get(parameter);
	}
	
	public void setName(String name) {
		ArgumentValidator.validateNotNullAndNotEmpty(name);
		this.name = name;
	}

	public void setSender(String sender) {
		ArgumentValidator.validateNotNullAndNotEmpty(sender);
		this.sender = sender;
	}

	public void setReceiver(String receiver) {
		ArgumentValidator.validateNotNullAndNotEmpty(receiver);
		this.receiver = receiver;
	}

	public void bindParameterToValue(String parameter, String value) {
		ArgumentValidator.validateNotNullAndNotEmpty(parameter);
		parameterValueBindings.put(parameter, value);
	}

	public Set<String> getParameters() {
		return new HashSet<String>(parameterValueBindings.keySet());
	}
	
	Map<String, String> getParameterValueBindings() {
		return new HashMap<String, String>(parameterValueBindings);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parameterValueBindings == null) ? 0 : parameterValueBindings.hashCode());
		result = prime * result + ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BSPLMessage other = (BSPLMessage) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parameterValueBindings == null) {
			if (other.parameterValueBindings != null)
				return false;
		} else if (!parameterValueBindings.equals(other.parameterValueBindings))
			return false;
		if (receiver == null) {
			if (other.receiver != null)
				return false;
		} else if (!receiver.equals(other.receiver))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		return true;
	}

}
