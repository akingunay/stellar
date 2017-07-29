package uk.ac.lancaster.turtles.stellar.relational.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import uk.ac.lancaster.turtles.stellar.relational.Relation;
import uk.ac.lancaster.turtles.stellar.relational.RelationElement;
import uk.ac.lancaster.turtles.stellar.relational.RelationMetadata;
import uk.ac.lancaster.turtles.stellar.relational.SQLStatementCompiler;
import uk.ac.lancaster.turtles.stellar.relational.LocalHistory;

// TODO The code handles all SQLExceptions by throwing RunTimeExcpetions. Since the database is within the middleware, 
//		there is probably no good way to handle these exceptions without breaking the execution.  Still we should 
//		investigate whether there is a better way to handle these exceptions.
// TODO Imporve exception messages.
// TODO DriverManager.getConnection(url + "?useSSL=false", serverMetadata.getUser(), serverMetadata.getPassword());
//		We can make this statement more customizable using MySQLServerMetadata.  For instance, "useSSL" can be set
//		according to user preference, which should be specified as part of metadata.
// TODO query and extractRelation methods may be refactored.

public class MySQLLocalHistory implements LocalHistory {
	
	private final static MySQLStatementCompiler statementCompiler = new MySQLStatementCompiler();
	
	private Connection connection;
	private Statement statement;
	
	private boolean closed;
	
	private static final String URL_PREFIX = "jdbc:mysql:";
	
	public static MySQLLocalHistory newMySQLLocalHistory(MySQLServerMetadata serverMetadata) {
		if (serverMetadata == null) {
			throw new NullPointerException();
		}
		String url = URL_PREFIX + "//" + serverMetadata.getHost() + "/" + serverMetadata.getDatabase();
		try {
			Connection connection = DriverManager.getConnection(url + "?useSSL=false", serverMetadata.getUser(), serverMetadata.getPassword());
			Statement statement = connection.createStatement();
			return new MySQLLocalHistory(connection, statement); 
		} catch (SQLException e) { 
			throw new RuntimeException(e);
		}
	}
	
	private MySQLLocalHistory(Connection connection, Statement statement) {
		this.connection = connection;
		this.statement = statement;
		this.closed = false;
	}
	
	@Override
	public Relation query(String queryString) {
		if (closed) {
			throw new IllegalStateException("MySQLRelationalLogManager is closed.");
		}
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(queryString);
			return extractRelation(resultSet);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
		    if (resultSet != null) {
		        try {
		            resultSet.close();
		            resultSet = null;
		        } catch (SQLException e) {
					throw new RuntimeException(e);
		        }
		    }
		}
	}
	
	private Relation extractRelation(ResultSet resultSet) {	
		try {
			String[] attributes = new String[resultSet.getMetaData().getColumnCount()];
			for (int i = 1 ; i <= attributes.length ; i++) {
				attributes[i - 1] = resultSet.getMetaData().getColumnLabel(i);
			}
			Set<RelationElement> elements = new HashSet<>();
			String[] values = new String[resultSet.getMetaData().getColumnCount()];
			while (resultSet.next()) {
				for (int i = 1 ; i <= values.length ; i++) {
					values[i - 1] = resultSet.getString(i);
				}
				elements.add(new RelationElement(attributes, values));
			}
			return Relation.newRelation(new RelationMetadata(RelationMetadata.ANONYMOUS_RELATION_NAME, new HashSet<>(Arrays.asList(attributes))), elements);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public SQLStatementCompiler getSQLStatementCompiler() {
		return statementCompiler;
	}
	
	@Override
	public void update(String updateString) {
		if (closed) {
			throw new IllegalStateException("SimpleMySQLRelationalLogManager is closed.");
		}
		try {
			statement.executeUpdate(updateString);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean isClosed() {
		return closed;
	}
	
	@Override
	public void close() {
		if (!closed) {
			closeStatement();
			closeConnection();
			closed = true;
		}
	}
	
	private void closeStatement() {
		try {
			if (statement != null) {
				statement.close();
				statement = null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
