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

package br.gov.sibbr.api.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.sibbr.api.db.DatabaseAuth;
import br.gov.sibbr.api.db.Utils;
import br.gov.sibbr.api.model.ApiUser;
import br.gov.sibbr.api.model.ApiUserResult;

/**
 * Service class for all authentication related methods
 * 
 * @author Pedro Guimarães
 *
 */
@Component
public class AuthService {

	public static char[] hexDigit = "0123456789abcdef".toCharArray();
	public static String SHA256 = "SHA-256";
	public static String MD5 = "MD5";

	// This is the default e-mail user for the system administer
	public static String ADMIN_EMAIL = "admin@sibbr.gov.br";

	@Autowired(required=true)
	DatabaseAuth dba;

	/**
	 * Turn the plain text phrase into a hash, given the hash algorithm.
	 * 
	 * @param phrase
	 * @param algorithm
	 * @return
	 */
	public String hash(String phrase, String algorithm) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			/* SHA-256 should be supported on all devices. */
			throw new RuntimeException(e);
		}
		return bytesToHex(md.digest(phrase.getBytes()));
	}

	/**
	 * Convert from bytes to a String hash.
	 * 
	 * @param bytes
	 * @return
	 */
	public String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; ++i) {
			int b = bytes[i] & 0xFF;
			hexChars[i * 2] = hexDigit[b >>> 4];
			hexChars[i * 2 + 1] = hexDigit[b & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Receives the provided form password, hash it, concatenate it with the
	 * salt from the database, hash it once more and check with the database
	 * password.
	 * 
	 * @param email
	 *            user email to be provided to database query
	 * @param password
	 *            user non-hashed password provided by form
	 * @return true case the password is valid, false otherwise
	 */
	public String checkPassword(String email, String password) {
		// Turn the provided form password into a hash
		String passwordHash = hash(password, SHA256);
		ResultSet rs = dba.queryApiUser(email);
		// Check if there is at least one result
		if (rs != null) {
			HashMap<String, Object> hashMap = processApiUser(rs);
			String databasePassword = (String) hashMap.get("password");
			String databaseSalt = (String) hashMap.get("salt");
			String databaseEmail = (String) hashMap.get("email");
			Boolean authorized = (Boolean) hashMap.get("authorized");
			// The user has been found on the system
			if (databaseEmail != null) {
				// The user is authorized
				if (authorized) {
					// There is valid salt
					if (databaseSalt != null) {
						// Concatenate user provided hashed password with salt:
						String saltedPassword = passwordHash + databaseSalt;
						// Rehash hashed password now concatenated with salt:
						String saltedPasswordHash = hash(saltedPassword, SHA256);
						// Compare the provided hashed password with the
						// database
						// password
						if (databasePassword.equalsIgnoreCase(saltedPasswordHash))
							return null;
						return "Wrong password.";
					}
					// This should not happen, salt should be always created
					// upon
					// new api user
					return "No salt for this email address.";
				}
				return "Unauthorized user. API admin must authorize your account in order to enable your access to tokens.";
			}
		}
		return "Invalid e-mail address.";
	}

	/**
	 * Check if the provided token is valid and not expired.
	 * 
	 * @param token
	 * @return error messages or null if the token is valid.
	 */
	public String checkToken(String token) {
		ResultSet rs = dba.queryToken(token);
		if (rs != null) {
			HashMap<String, Object> hashMap = processApiToken(rs);
			if (hashMap.size() > 0) {
				token = (String) hashMap.get("token");
				// Get date string:
				Timestamp tokenTime = (Timestamp) hashMap.get("created_at");
				// Get current date:
				Date currentTime = Calendar.getInstance().getTime();
				// Check if the token has more than 7 days from creation to
				// now, being therefore, expired
				long diff = Math.abs(currentTime.getTime() - tokenTime.getTime());
				long diffInMinutes = diff / (60 * 1000);
				// Amount of minutes in one week
				long weekInMinutes = 60 * 24 * 7;
				// If the token is one week + old, generate a new token
				if (diffInMinutes > weekInMinutes) {
					return "Expired token. Please login and fetch a new token.";
				}
				// Valid and non expired token:
				return null;
			}
		}
		return "Invalid token.";
	}

	/**
	 * Check if the provided token is a valid admin token
	 * 
	 * @param token
	 * @return Error strings or null if the token is valid
	 */
	public String checkTokenAdmin(String token) {
		String message = checkToken(token);
		// This is a valid token
		if (message == null) {
			// Check if it belongs to the admin
			String databaseToken = fetchToken(ADMIN_EMAIL);
			if (token.equalsIgnoreCase(databaseToken)) {
				message = null;
			}
			// Provided token does not match the current valid admin token
			else {
				message = "Invalid token. Make sure you have an unexpired admin token.";
			}
		}
		return message;
	}

	/**
	 * Returns the user a valid token. If the current token is expired,
	 * generates a new one and updates the database. It supposes the email is a
	 * valid user.
	 * 
	 * @param email
	 * @return
	 */
	public String fetchToken(String email) {
		ResultSet rs = null;
		String token = null;
		rs = dba.queryApiUserTokenId(email);
		long token_id = processTokenId(rs);
		// If this query is null, the long equals 0. if that is the case, there
		// was no token found to be associated to this user and, therefore, a
		// new token should be generated.
		if (token_id != 0L) {
			rs = dba.queryTokenById(token_id);
			// If there is a valid token:
			if (rs != null) {
				HashMap<String, Object> hashMap = processApiToken(rs);
				if (hashMap.size() > 0) {
					token = (String) hashMap.get("token");
					// Get date string:
					Timestamp tokenTime = (Timestamp) hashMap.get("created_at");
					// Get current date:
					Date currentTime = Calendar.getInstance().getTime();
					// Check if the token has more than 7 days from creation to
					// now, being therefore, expired
					long diff = Math.abs(currentTime.getTime() - tokenTime.getTime());
					long diffInMinutes = diff / (60 * 1000);
					// Amount of minutes in one week
					long weekInMinutes = 60 * 24 * 7;
					// If the token is one week + old, generate a new token
					if (diffInMinutes > weekInMinutes) {
						token = generateNewToken(email);
					}
				}
			}
		}
		// There is no generated token to the user (first access)
		else {
			token = generateNewToken(email);
		}
		return token;
	}

	/**
	 * Processes resultSet to retrieve password and salt information from query
	 * in the form of a hashmap
	 * 
	 * @param rs
	 *            database query from DatabaseAuth.queryApiUser()
	 * @return
	 */
	public HashMap<String, Object> processApiUser(ResultSet rs) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		try {
			while (rs.next()) {
				String password = Utils.getString(rs, "password");
				String salt = Utils.getString(rs, "salt");
				String email = Utils.getString(rs, "email");
				Long token_id = rs.getLong("token_id");
				Boolean authorized = rs.getBoolean("authorized");
				hashMap.put("password", password);
				hashMap.put("salt", salt);
				hashMap.put("email", email);
				hashMap.put("token_id", token_id);
				hashMap.put("authorized", authorized);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hashMap;
	}

	/**
	 * Processes resultSet to retrieve password and salt information from query
	 * in the form of a hashmap
	 * 
	 * @param rs
	 *            database query from DatabaseAuth.queryApiUser()
	 * @return
	 */
	public long processTokenId(ResultSet rs) {
		long token_id = 0;
		try {
			while (rs.next()) {
				token_id = rs.getLong("token_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return token_id;
	}

	/**
	 * Processes resultSet to retrieve password and salt information from query
	 * in the form of a hashmap
	 * 
	 * @param rs
	 *            database query from DatabaseAuth.queryApiUser()
	 * @return
	 */
	public HashMap<String, Object> processApiToken(ResultSet rs) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		try {
			while (rs.next()) {
				String auto_id = Utils.getString(rs, "auto_id");
				String token = Utils.getString(rs, "token");
				Date createdAt = Utils.getTimestamp(rs, "created_at");
				hashMap.put("auto_id", auto_id);
				hashMap.put("token", token);
				hashMap.put("created_at", createdAt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hashMap;
	}

	/**
	 * Generates a new token hash, inserts it into the api_token table and
	 * updates the api_user table to point to the token record's foreign key
	 * 
	 * @param email
	 * @return
	 */
	public String generateNewToken(String email) {
		long random = new Random().nextLong();
		String token = hash(Long.toString(System.currentTimeMillis()) + Long.toString(random), SHA256);
		// Inserts the new token to the api_token table
		ResultSet rs = dba.generateNewToken(email, token);
		if (rs != null) {
			long auto_id = processTokenAutoId(rs);
			int updated = dba.updateApiUserTokenId(email, auto_id);
			// Api user has been updated with the token id
			if (updated != 0) {
				return token;
			}
		}
		return null;
	}

	/**
	 * Generates a new md5 hash to be the user account password salteign key
	 * 
	 * @param email
	 * @return
	 */
	public String generateSalt() {
		long random = new Random().nextLong();
		// Generate a pseudorandom hash for the salt
		String hash = hash(Long.toString(System.currentTimeMillis()) + Long.toString(random), MD5);
		return hash;
	}

	/**
	 * Receives a resultset to fetch and return the auto_id from a api_token
	 * record
	 * 
	 * @param rs
	 * @return
	 */
	public Long processTokenAutoId(ResultSet rs) {
		Long auto_id = null;
		if (rs != null) {
			try {
				while (rs.next()) {
					auto_id = rs.getLong("auto_id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return auto_id;
	}

	public String createAccount(String email, String password) {
		// Check if user already exists in the database:
		ResultSet rs = dba.queryApiUser(email);
		if (rs != null) {
			HashMap<String, Object> hashMap = processApiUser(rs);
			String databaseEmail = (String) hashMap.get("email");
			if (databaseEmail != null) {
				if (databaseEmail.equalsIgnoreCase(email)) {
					return "User already registered to the database.";
				}
			}
		}
		// New valid email user, carry on:
		// Generate salt for the user account
		String salt = generateSalt();
		// Turn the provided form password into a hash
		String passwordHash = hash(password, SHA256);
		// Concatenate user provided hashed password with salt:
		String saltedPassword = passwordHash + salt;
		// Rehash hashed password now concatenated with salt:
		String saltedPasswordHash = hash(saltedPassword, SHA256);
		int result = dba.createApiUser(email, saltedPasswordHash, salt);
		if (result == 0) {
			return "Error inserting user to the database.";
		}
		return "New account created successfully to the user " + email
				+ ". Once the admin authorizes your account you will receive a valid token upon login.";
	}

	/**
	 * This method is responsible for hashing the admin password, generating a
	 * new salt for the admin account and updating the admin information in the
	 * database
	 * 
	 * @param password
	 *            the non hashed new password provided by the user for the admin
	 *            account
	 * @return Error message or null if everything went well.
	 */
	public String updateAdminPassword(String password) {
		String newSalt = generateSalt();
		String passwordHash = hash(password, SHA256);
		// Concatenate user provided hashed password with salt:
		String saltedPassword = passwordHash + newSalt;
		// Rehash hashed password now concatenated with salt:
		String saltedPasswordHash = hash(saltedPassword, SHA256);
		int result = dba.updateApiUser(ADMIN_EMAIL, saltedPasswordHash, newSalt);
		if (result == 0) {
			return "Error updating user information.";
		}
		return "Password successfully updated.";
	}

	/**
	 * Responsible for fetching user list and returning as an ApiUserResult
	 * 
	 * @return ApiUserResult with the list of users
	 */
	public ApiUserResult fetchApiUsers() {
		ResultSet rs = dba.queryApiUsers();
		ArrayList<ApiUser> users = new ArrayList<ApiUser>();
		try {
			while (rs.next()) {
				Long auto_id = rs.getLong("auto_id");
				String email = Utils.getString(rs, "email");
				Boolean authorized = rs.getBoolean("authorized");
				users.add(new ApiUser(auto_id, email, authorized));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ApiUserResult(users);
	}

	/**
	 * Responsible for fetching user list and removing all unauthorized users,
	 * leaving only the authorized userlist as an ApiUserResult
	 * 
	 * @return ApiUserResult with the list of users
	 */
	public ApiUserResult fetchApiAuthorizedUsers() {
		ApiUserResult users = fetchApiUsers();
		ArrayList<ApiUser> filteredUsers = new ArrayList<ApiUser>();
		for (ApiUser u : users.getUsers()) {
			// User is not authorized
			if (u.getAuthorized()) {
				// Remove from the user list
				filteredUsers.add(u);
			}
		}
		// Set the new subset of users
		users.setUsers(filteredUsers);
		// Return result
		return users;
	}

	/**
	 * Responsible for fetching user list and removing all authorized users,
	 * leaving only the unauthorized userlist as an ApiUserResult
	 * 
	 * @return ApiUserResult with the list of users
	 */
	public ApiUserResult fetchApiUnauthorizedUsers() {
		ApiUserResult users = fetchApiUsers();
		ArrayList<ApiUser> filteredUsers = new ArrayList<ApiUser>();
		for (ApiUser u : users.getUsers()) {
			// User is not authorized
			if (!u.getAuthorized()) {
				// Remove from the user list
				filteredUsers.add(u);
			}
		}
		// Set the new subset of users
		users.setUsers(filteredUsers);
		// Return result
		return users;
	}

	/**
	 * This method calls the database to update the authorized field of a given
	 * user to true.
	 * 
	 * @param id
	 *            the auto_id of the api user to be updated
	 * @return Error message or success message
	 */
	public String habilitateApiUser(Long id) {
		int result = dba.habilitateApiUser(id);
		if (result == 0) {
			return "Could not habilitate user.";
		}
		return "User successfully authorized.";
	}

	public String checkApiUser(Long id) {
		ResultSet rs = dba.queryApiUser(id);
		if (rs != null) {
			try {
				while (rs.next()) {
					String email = Utils.getString(rs, "email");
					return email;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}