package br.gov.sibbr.api.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAuth {

	private Connection conn = null;

	public static final String API_USER_TABLE = "api_user";
	public static final String API_USER_EMAIL = "email";
	public static final String API_USER_PASSWORD = "password";
	public static final String API_USER_SALT = "salt";
	public static final String API_USER_TOKEN_ID = "token_id";

	public static final String API_TOKEN_TABLE = "api_token";
	public static final String API_TOKEN_AUTO_ID = "auto_id";
	public static final String API_TOKEN_TOKEN = "token";

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
	public ResultSet queryApiUser(String email) {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT " + API_USER_PASSWORD + ", " + API_USER_EMAIL + ", "
					+ API_USER_SALT + " FROM " + API_USER_TABLE + " WHERE " + API_USER_EMAIL + " = \'" + email + "\'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Fetch user token_id from user email
	 * 
	 * @param email
	 * @return
	 */
	public ResultSet queryApiUserTokenId(String email) {
		ResultSet resultSet = null;
		Statement statement = null;
		String sql = "SELECT " + API_USER_TOKEN_ID + " FROM " + API_USER_TABLE + " WHERE "
				+ API_USER_EMAIL + " = \'" + email + "\'";
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Fetch token from api_user by user email
	 * 
	 * @param email
	 * @return
	 */
	public ResultSet queryTokenById(Long token_id) {
		ResultSet resultSet = null;
		Statement statement = null;
		String sql = "SELECT * FROM " + API_TOKEN_TABLE + " WHERE " + API_TOKEN_AUTO_ID + " = " + token_id.toString();
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Generates new token to user and updates api_user table with token id
	 * 
	 * @param email
	 * @param token
	 * @return the ResultSet that carries the new token's auto_id
	 */
	public ResultSet generateNewToken(String email, String token) {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			int inserted = statement.executeUpdate("INSERT INTO " + API_TOKEN_TABLE + " (token, created_at) values (\'"
					+ token + "\', current_timestamp)");
			// If the record was inserted properly, update api_user with token
			// id and return
			if (inserted != 0) {
				statement = conn.createStatement();
				resultSet = statement.executeQuery("SELECT " + API_TOKEN_AUTO_ID + " FROM " + API_TOKEN_TABLE
						+ " WHERE " + API_TOKEN_TOKEN + " = \'" + token + "\'");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public int updateApiUserToken(String email, Long auto_id) {
		Statement statement = null;
		int update = 0;
		try {
			statement = conn.createStatement();
			update = statement.executeUpdate("UPDATE " + API_USER_TABLE + " SET " + API_USER_TOKEN_ID + " = " + auto_id
					+ " WHERE " + API_USER_EMAIL + " = \'" + email + "\'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return update;
	}
}