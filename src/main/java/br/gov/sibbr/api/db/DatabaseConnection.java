package br.gov.sibbr.api.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is responsible to manage jdbc connections to the postgresql
 * database only for read operations.
 * 
 * @author Pedro Guimarães
 *
 */
public class DatabaseConnection {

	private Connection conn = null;

	/**
	 * Default consctructor, connects to default psql database;
	 */
	public DatabaseConnection() {
		connectToDatabaseOrDie();
	}

	/**
	 * Constructor to connect to database given parameters;
	 * 
	 * @param url
	 * @param user
	 * @param password
	 */
	public DatabaseConnection(String url, String user, String password) {
		connectToDatabaseOrDie(url, user, password);
	}

	/**
	 * Singleton to connect to database
	 * 
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 */
	private void connectToDatabaseOrDie(String url, String user, String password) {
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}

	/**
	 * Default setup for postgres connection.
	 * 
	 * @return
	 */
	private void connectToDatabaseOrDie() {
		String url = "jdbc:postgresql://localhost/dataportal";
		String username = "dbadmin";
		String password = "dbadmin";
		connectToDatabaseOrDie(url, username, password);
	}

	/**
	 * Releases connection.
	 */
	public void releaseConnection() {
		try {
			if (!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return conn;
	}

	/**
	 * Fetches records from dataportal schema, returning the auto_id,
	 * decimallatitude and decmiallongitude fields from occurrence table that
	 * match the given scientificname;
	 * 
	 * @param scientificname
	 * @return
	 */
	public ResultSet queryOccurrences(String scientificname) {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery(
					"SELECT auto_id, decimallatitude, decimallongitude FROM occurrence WHERE scientificname = \'"
							+ scientificname + "\'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
}
