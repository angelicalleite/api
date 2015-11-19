package br.gov.sibbr.api.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAuth {
	
	private Connection conn = null;
	
	public static final String API_USER = "api_user";
	public static final String API_TOKEN = "api_token";
	public static final String API_USER_EMAIL = "email";
	public static final String API_USER_PASSWORD = "password";
	public static final String API_USER_SALT = "salt";
	
	/**
	 * Default class constructor, creates a new connection to the database
	 */
	public DatabaseAuth() throws Exception {
		Connection connection = DatabaseConnection.getAuthConnection();
		if (connection != null) {
			this.conn = connection;
		} else {
			throw new Exception("Failled attempt to connect to authentication database.");
		}
	}
	
	/**
	 * Fetches records from dataportal schema, returning different sets of
	 * fields from occurrence table that match the given scientificname;
	 * 
	 * @param scientificname
	 * @return
	 */
	public ResultSet queryApiUser(String email, String passwordHash) {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT " + API_USER_PASSWORD + ", " + API_USER_EMAIL + ", " + API_USER_SALT + " FROM " + API_USER + " WHERE " + API_USER_EMAIL + " = \'" + email + "\'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
}