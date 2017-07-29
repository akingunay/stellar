package uk.ac.lancaster.turtles.stellar.relational.mysql;

//TODO Imporve exception messages.

public final class MySQLServerMetadata {
	
	private final String host;
	private final String database;
	private final String user;
	private final String password;
	
	public final static MySQLServerMetadata newMySQLServerMetadata(String host, String database, String user, String password) {
		if (host == null) {
			throw new NullPointerException();
		}
		if (host.isEmpty()) {
			throw new IllegalArgumentException("Argument 'String host' cannot be empty.");
		}
		if (database == null) {
			throw new NullPointerException();
		}
		if (database.isEmpty()) {
			throw new IllegalArgumentException("Argument 'String database' cannot be empty.");
		}
		if (user == null) {
			throw new NullPointerException();
		} 
		if (user.isEmpty()) { 
			throw new IllegalArgumentException("Argument 'String user' cannot be empty.");
		}
		if (password == null) {
			throw new NullPointerException();
		}	
		if (password.isEmpty()) {
			throw new IllegalArgumentException("Argument 'String password' cannot be null or empty.");
		}
		return new MySQLServerMetadata(host, database, user, password);
	}
	
	private MySQLServerMetadata(String host, String database, String user, String password) {
		this.host = host;
		this.database = database;
		this.user = user;
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public String getDatabase() {
		return database;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

}
