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
	 * Default setup for postgres connection. Default values are hard coded
	 * here.
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
	public ResultSet queryOccurrences(String scientificname, int limit) {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			// Avoid limit values out of the appropriate range ( <= 0)
			if (limit > 0) {
				resultSet = statement
						.executeQuery("SELECT auto_id, decimallatitude, decimallongitude FROM occurrence WHERE scientificname = \'"
								+ scientificname + "\' limit " + limit);
			} else {
				resultSet = statement
						.executeQuery("SELECT auto_id, decimallatitude, decimallongitude FROM occurrence WHERE scientificname = \'"
								+ scientificname + "\'");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Fetches records from dataportal schema, returning the auto_id,
	 * decimallatitude and decmiallongitude fields from occurrence table that
	 * match the given scientificname;
	 * 
	 * @param scientificname
	 * @return
	 */
	public ResultSet queryOccurrencesIgnoreNullCoordinates(
			String scientificname, int limit) {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			// Avoid limit values out of the appropriate range ( <= 0)
			if (limit > 0) {
				resultSet = statement
						.executeQuery("SELECT auto_id, decimallatitude, decimallongitude FROM occurrence WHERE scientificname = \'"
								+ scientificname
								+ "\' and decimallatitude is not null and decimallatitude is not null limit "
								+ limit);
			} else {
				resultSet = statement
						.executeQuery("SELECT auto_id, decimallatitude, decimallongitude FROM occurrence WHERE scientificname = \'"
								+ scientificname
								+ "\' and decimallatitude is not null and decimallatitude is not null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Returns the total amount of records available.
	 * 
	 * @param scientificname
	 * @return
	 */
	public ResultSet queryTotalRecords() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement
					.executeQuery("SELECT count(auto_id) as totalrecords FROM occurrence");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Returns the total amount of records available with georeferencial information.
	 * 
	 * @param scientificname
	 * @return
	 */
	public ResultSet queryTotalGeoRecords() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement
					.executeQuery("SELECT count(auto_id) as totalrecords FROM occurrence where decimallatitude is not null and decimallongitude is not null");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Retrieve amount of repatriated records (includes null coordinate records)
	 * @return
	 */
	public ResultSet queryTotalRepatriatedRecords() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement
					.executeQuery("SELECT count(auto_id) as totalrecords FROM occurrence where publishername like 'SiBBr'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Retrieve amount of repatriated records (includes null coordinate records)
	 * @return
	 */
	public ResultSet queryTotalPublishers() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement
					.executeQuery("SELECT count(auto_id) as totalpublishers FROM publisher");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Retrieve amount of repatriated records (includes null coordinate records)
	 * @return
	 */
	public ResultSet queryTotalResources() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement
					.executeQuery("SELECT count(id) as totalresources FROM dwca_resource");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Retrieve amount of species, depending on taxon rank information.
	 * @return
	 */
	public ResultSet queryTotalSpecies() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement
					.executeQuery("SELECT count(distinct(scientificname)) as totalspecies FROM occurrence where taxonrank in ('espécie', 'EspÈcie', 'SPECIES','ESPECIE')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Retrieve amount of phylum, depending on taxon rank information.
	 * @return
	 */
	public ResultSet queryTotalPhylum() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement
					.executeQuery("SELECT count(distinct(scientificname)) as totalphylum FROM occurrence where taxonrank in ('PHYLUM')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Retrieve amount of classes, depending on taxon rank information.
	 * @return
	 */
	public ResultSet queryTotalClass() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement
					.executeQuery("SELECT count(distinct(scientificname)) as totalclass FROM occurrence where taxonrank in ('CLASS', 'classe')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Retrieve amount of orders, depending on taxon rank information.
	 * @return
	 */
	public ResultSet queryTotalOrder() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement
					.executeQuery("SELECT count(distinct(scientificname)) as totalorder FROM occurrence where taxonrank in ('ORDER', 'ordem')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Retrieve amount of families, depending on taxon rank information.
	 * @return
	 */
	public ResultSet queryTotalFamily() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement
					.executeQuery("SELECT count(distinct(scientificname)) as totalfamily FROM occurrence where taxonrank in ('FAMILY', 'FamÌlia', 'família')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Retrieve amount of genders, depending on taxon rank information.
	 * @return
	 */
	public ResultSet queryTotalGenus() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement
					.executeQuery("SELECT count(distinct(scientificname)) as totalgenus FROM occurrence where taxonrank in ('GENUS', 'GÍnero', 'gênero')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
}
