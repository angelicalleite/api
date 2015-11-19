package br.gov.sibbr.api.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import br.gov.sibbr.api.db.DatabaseAuth;
import br.gov.sibbr.api.db.Utils;

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
		ResultSet rs = dba.queryApiUser(email, password);
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
				// This should not happen, salt should be always created upon new api user
				return "No salt for this email address.";
			}
		}
		return "Invalid e-mail address.";
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
}