/**
  	SiBBr API - Interface pública de acesso aos registros de ocorrência
    Copyright (C) 2015  SiBBr - Sistema de Informação sobre a Biodiversidade Brasileira

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 */

package br.gov.sibbr.api.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is responsible for providing database access methods to query the
 * authentication database
 * 
 * @author pedro
 *
 */
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

	public static final int USER_EXISTS = -2;

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
	public ResultSet queryToken(String token) {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery(
					"SELECT * FROM " + API_TOKEN_TABLE + " WHERE " + API_TOKEN_TOKEN + " = \'" + token + "\'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
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
			resultSet = statement.executeQuery("SELECT * FROM " + API_USER_TABLE + " WHERE " + API_USER_EMAIL + " = \'" + email + "\'");
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
		String sql = "SELECT " + API_USER_TOKEN_ID + " FROM " + API_USER_TABLE + " WHERE " + API_USER_EMAIL + " = \'"
				+ email + "\'";
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

	/**
	 * Updates the api_user table so that the token_id points to the provided auto_id
	 * @param email the user account
	 * @param auto_id the unique identifier of an api_token record
	 * @return update an integer indicating 0 error or N the amount of updated records
	 */
	public int updateApiUserTokenId(String email, Long auto_id) {
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

	/**
	 * Auxiliary method to create a new user in the database with the given provided paramenters
	 * @param email valid user email account
	 * @param password hashed password
	 * @param salt md5 password hash
	 * @return result an integer indicating 0 error or N the amount of updated records
	 */
	public int createApiUser(String email, String password, String salt) {
		Statement statement = null;
		int result = 0;
		// New user, insert into the database
		try {
			statement = conn.createStatement();
			String sql = "INSERT INTO " + API_USER_TABLE + " (" + API_USER_EMAIL + ", " + API_USER_PASSWORD + ", "
					+ API_USER_SALT + ") values (\'" + email + "\', \'" + password + "\', \'" + salt + "\')";
			result = statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Update an api user record with provided password and salt
	 * @param email valid user email account
	 * @param password hashed password
	 * @param salt md5 password hash
	 * @return result an integer indicating 0 error or N the amount of updated records
	 */
	public int updateApiUser(String email, String password, String salt) {
		Statement statement = null;
		int result = 0;
		// New user, insert into the database
		try {
			statement = conn.createStatement();
			String sql = "UPDATE " + API_USER_TABLE + " SET " + API_USER_PASSWORD + " = \'" + password + "\', "
					+ API_USER_SALT + " = \'" + salt + "\' WHERE " + API_USER_EMAIL + " = \'" + email + "\'";
			result = statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}