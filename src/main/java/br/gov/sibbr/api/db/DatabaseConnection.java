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

/**
 * This class is responsible to manage jdbc connections to the postgresql
 * database only for read operations.
 * 
 * @author Pedro Guimarães
 *
 */
public final class DatabaseConnection {

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
		String url = "jdbc:postgresql://localhost/dataportal";
		String username = "dbadmin";
		String password = "dbadmin";
		return getConnection(url, username, password);
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
