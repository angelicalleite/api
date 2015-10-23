package br.gov.sibbr.api.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseQueries {

	private Connection conn = null;

	public static final int RETURN_SOME_FIELDS = 0;
	public static final int RETURN_ALL_FIELDS = 1;

	public static final String OCCURRENCE_TABLE = "occurrence";
	public static final String RESOURCE_TABLE = "dwca_resource";
	public static final String ALL_OCCURRENCE_FIELDS = "auto_id, resourcename, publishername, kingdom, phylum, _class, _order, family, genus, specificepithet, infraspecificepithet, species, scientificname, taxonrank, typestatus, recordedby, eventdate, continent, country, stateprovince, municipality, county, minimumelevationinmeters, maximumelevationinmeters, hascoordinates, decimallatitude, decimallongitude, hasmedia, associatedmedia";
	public static final String SOME_OCCURRENCE_FIELDS = "auto_id, decimallatitude, decimallongitude";
	public static final String RESOURCE_FIELDS = "id, name, archive_url, gbif_package_id, record_count, publisher_fkey";

	/**
	 * Default class constructor, creates a new connection to the database
	 */
	public DatabaseQueries() throws Exception {
		Connection connection = DatabaseConnection.getConnection();
		if (connection != null) {
			this.conn = connection;
		} else {
			throw new Exception("Failled attempt to connect to database.");
		}
	}

	/** catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	 * Constructor that receives a connection
	 * 
	 * @param connection
	 */
	public DatabaseQueries(Connection connection) throws Exception {
		if (connection != null) {
			this.conn = connection;
		} else {
			throw new Exception("Failled to connect to database. Null connection passed as argument.");
		}
	}

	/**
	 * Fetches records from dataportal schema, returning different sets of fields
	 * from occurrence table that match the given scientificname;
	 * 
	 * @param scientificname
	 * @return
	 */
	public ResultSet queryOccurrences(String scientificname, int limit, int fields) {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			// Avoid limit values out of the appropriate range ( <= 0)
			if (limit > 0) {
				if (fields == RETURN_ALL_FIELDS) {
					resultSet = statement.executeQuery("SELECT " + ALL_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE
							+ " WHERE scientificname = \'" + scientificname + "\' limit " + limit);
				} else if (fields == RETURN_SOME_FIELDS) {
					resultSet = statement.executeQuery("SELECT " + SOME_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE
							+ " WHERE scientificname = \'" + scientificname + "\' limit " + limit);
				}
			}
			// No limits required, return all records
			else {
				if (fields == RETURN_ALL_FIELDS) {
					resultSet = statement.executeQuery("SELECT " + ALL_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE
							+ " WHERE scientificname = \'" + scientificname + "\'");
				} else if (fields == RETURN_SOME_FIELDS) {
					resultSet = statement.executeQuery("SELECT " + SOME_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE
							+ " WHERE scientificname = \'" + scientificname + "\'");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Fetches records from dwca_resource table
	 * @return
	 */
	public ResultSet queryResources() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT " + RESOURCE_FIELDS + " FROM " + RESOURCE_TABLE);
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
	public ResultSet queryOccurrencesIgnoreNullCoordinates(String scientificname, int limit, int fields) {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			// Avoid limit values out of the appropriate range ( <= 0)
			if (limit > 0) {
				if (fields == RETURN_ALL_FIELDS) {
					resultSet = statement.executeQuery("SELECT " + ALL_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE
							+ " WHERE scientificname = \'" + scientificname
							+ "\' and decimallatitude is not null and decimallatitude is not null limit " + limit);
				} else if (fields == RETURN_SOME_FIELDS) {
					resultSet = statement.executeQuery("SELECT " + SOME_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE
							+ " WHERE scientificname = \'" + scientificname
							+ "\' and decimallatitude is not null and decimallatitude is not null limit " + limit);
				}
			}
			// No limits required, return all records
			else {
				if (fields == RETURN_ALL_FIELDS) {
					resultSet = statement.executeQuery("SELECT " + ALL_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE
							+ " WHERE scientificname = \'" + scientificname
							+ "\' and decimallatitude is not null and decimallatitude is not null");
				} else if (fields == RETURN_SOME_FIELDS) {
					resultSet = statement.executeQuery("SELECT " + SOME_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE
							+ " WHERE scientificname = \'" + scientificname
							+ "\' and decimallatitude is not null and decimallatitude is not null");
				}
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
			resultSet = statement.executeQuery("SELECT count(auto_id) as totalrecords FROM " + OCCURRENCE_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Returns the total amount of records available with georeferencial
	 * information.
	 * 
	 * @param scientificname
	 * @return
	 */
	public ResultSet queryTotalGeoRecords() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT count(auto_id) as totalrecords FROM " + OCCURRENCE_TABLE
					+ " where decimallatitude is not null and decimallongitude is not null");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Retrieve amount of repatriated records (includes null coordinate records)
	 * 
	 * @return
	 */
	public ResultSet queryTotalRepatriatedRecords() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT count(auto_id) as totalrecords FROM " + OCCURRENCE_TABLE
					+ " where publishername like 'SiBBr'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Retrieve amount of repatriated records (includes null coordinate records)
	 * 
	 * @return
	 */
	public ResultSet queryTotalPublishers() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT count(auto_id) as totalpublishers FROM publisher");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Retrieve amount of repatriated records (includes null coordinate records)
	 * 
	 * @return
	 */
	public ResultSet queryTotalResources() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT count(id) as totalresources FROM dwca_resource");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Retrieve amount of species, depending on taxon rank information.
	 * 
	 * @return
	 */
	public ResultSet queryTotalSpecies() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT count(distinct(scientificname)) as totalspecies FROM "
					+ OCCURRENCE_TABLE + " where taxonrank in ('espécie', 'EspÈcie', 'SPECIES','ESPECIE')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Retrieve amount of phylum, depending on taxon rank information.
	 * 
	 * @return
	 */
	public ResultSet queryTotalPhylum() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT count(distinct(scientificname)) as totalphylum FROM "
					+ OCCURRENCE_TABLE + " where taxonrank in ('PHYLUM')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Retrieve amount of classes, depending on taxon rank information.
	 * 
	 * @return
	 */
	public ResultSet queryTotalClass() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT count(distinct(scientificname)) as totalclass FROM "
					+ OCCURRENCE_TABLE + " where taxonrank in ('CLASS', 'classe')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Retrieve amount of orders, depending on taxon rank information.
	 * 
	 * @return
	 */
	public ResultSet queryTotalOrder() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT count(distinct(scientificname)) as totalorder FROM "
					+ OCCURRENCE_TABLE + " where taxonrank in ('ORDER', 'ordem')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Retrieve amount of families, depending on taxon rank information.
	 * 
	 * @return
	 */
	public ResultSet queryTotalFamily() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT count(distinct(scientificname)) as totalfamily FROM "
					+ OCCURRENCE_TABLE + " where taxonrank in ('FAMILY', 'FamÌlia', 'família')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Retrieve amount of genders, depending on taxon rank information.
	 * 
	 * @return
	 */
	public ResultSet queryTotalGenus() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT count(distinct(scientificname)) as totalgenus FROM "
					+ OCCURRENCE_TABLE + " where taxonrank in ('GENUS', 'GÍnero', 'gênero')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public void releaseConnection() {
		DatabaseConnection.releaseConnection(conn);
	}
}
