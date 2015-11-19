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
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;

import br.gov.sibbr.api.Application;

/**
 * This class is responsible to manage jdbc connections to the postgresql
 * database only for read operations.
 * 
 * @author Pedro Guimarães
 *
 */
public class DatabaseConnection {

	// DB Connection configuration for data access:
	public static String dataUrl = "jdbc:postgresql://localhost/dataportal";
	public static String dataUser = "dbadmin";
	public static String dataPassword = "dbadmin";
	// DB Connection configuration for authentication access:
	public static String authUrl = "jdbc:postgresql://localhost/dataportal_auth";
	public static String authUser = "dbadmin";
	public static String authPassword = "dbadmin";
	
	/**
	 * Singleton to connect to database
	 * 
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 */
	public static Connection getConnection(String url, String user, String password) {
		Connection conn = null;
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
		return conn;
	}

	/**
	 * Default setup for postgres connection. Default values are hard coded
	 * here.
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		return getConnection(dataUrl, dataUser, dataPassword);
	}

	public static Connection getAuthConnection() {
		return getConnection(authUrl, authUser, authPassword);
	}

	/**
	 * Releases connection.
	 */
	public static void releaseConnection(Connection conn) {
		try {
			if (!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
