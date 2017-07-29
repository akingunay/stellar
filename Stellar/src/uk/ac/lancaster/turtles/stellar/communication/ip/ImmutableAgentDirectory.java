package uk.ac.lancaster.turtles.stellar.communication.ip;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ImmutableAgentDirectory {

	private final Map<String, InetSocketAddress> directory;
	
	private ImmutableAgentDirectory(Builder builder) {
		this.directory = builder.directory;
	}
	
	public InetSocketAddress getAddressOfAgent(String agentID) throws UnknownHostException {
		if (!directory.containsKey(agentID)) {
			throw new UnknownHostException();
		}
		return directory.get(agentID);
	}
	
	public boolean isAddressOfAgentKnown(String agentID) {
		return directory.containsKey(agentID);
	}
	
	public static class Builder {
		private final Map<String, InetSocketAddress> directory;
		
		public Builder() {
			directory = new HashMap<>();
		}
		
		public Builder setAgentAddress(String agentID, InetSocketAddress address) {
			directory.put(agentID, address);
			return this;
		}
		
		public ImmutableAgentDirectory build() {
			return new ImmutableAgentDirectory(this);
		}
	}
}
