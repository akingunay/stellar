package uk.ac.lancaster.turtles.stellar.relational;

public interface LocalHistory {

	Relation query(String queryString);
	void update(String updateString);
	boolean isClosed();
	void close();
	SQLStatementCompiler getSQLStatementCompiler();
	
}
