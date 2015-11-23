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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import br.gov.sibbr.api.db.DatabaseAuth;
import br.gov.sibbr.api.db.Utils;

/**
 * Service class for all authentication related methods
 * @author Pedro Guimarães
 *
 */
public class AuthService {

	public static char[] hexDigit = "0123456789abcdef".toCharArray();
	public static String SHA256 = "SHA-256";
	public static String MD5 = "MD5";

	DatabaseAuth dba = null;

	public AuthService() {
		try {
			dba = new DatabaseAuth();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
			HashMap<String, String> hashMap = processApiUser(rs);
			String databasePassword = hashMap.get("password");
			String databaseSalt = hashMap.get("salt");
			String databaseEmail = hashMap.get("email");
			// The user has been found on the system
			if (databaseEmail != null) {
				// There is valid salt
				if (databaseSalt != null) {
					// Concatenate user provided hashed password with salt:
					String saltedPassword = passwordHash + databaseSalt;
					// Rehash hashed password now concatenated with salt:
					String saltedPasswordHash = hash(saltedPassword, SHA256);
					// Compare the provided hashed password with the database
					// password
					if (databasePassword.equalsIgnoreCase(saltedPasswordHash))
						return null;
					return "Wrong password.";
				}
				// This should not happen, salt should be always created upon
				// new api user
				return "No salt for this email address.";
			}
		}
		return "Invalid e-mail address.";
	}
	
	/**
	 * Check if the provided token is valid and not expired.
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
	public HashMap<String, String> processApiUser(ResultSet rs) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try {
			while (rs.next()) {
				String password = Utils.getString(rs, "password");
				String salt = Utils.getString(rs, "salt");
				String email = Utils.getString(rs, "email");
				hashMap.put("password", password);
				hashMap.put("salt", salt);
				hashMap.put("email", email);
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
	 * generates a new token hash, inserts it into the api_token table and
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
			int updated = dba.updateApiUserToken(email, auto_id);
			// Api user has been updated with the token id
			if (updated != 0) {
				return token;
			}
		}
		return null;
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
}