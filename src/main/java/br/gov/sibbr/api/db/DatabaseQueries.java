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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import br.gov.sibbr.api.utils.TAXONOMIAS;

/**
 * This class is responsible for providing access methods to the database through query implementations
 * @author Pedro Guimarães
 *
 */
@Component
public class DatabaseQueries {

	@Autowired(required=true)
	@Qualifier("ocorrenciasconex")
	private Connection conn;
	
	protected final Logger LOGGER =  LoggerFactory.getLogger(DatabaseQueries.class);

	public static final int RETURN_SOME_FIELDS = 0;
	public static final int RETURN_ALL_FIELDS = 1;

	public static final String OCCURRENCE_TABLE = "occurrence";
	public static final String RESOURCE_TABLE = "dwca_resource";
	public static final String ALL_OCCURRENCE_FIELDS = "auto_id, resourcename, publishername, kingdom, phylum, _class, _order, family, genus, specificepithet, infraspecificepithet, species, scientificname, taxonrank, typestatus, recordedby, eventdate, continent, country, stateprovince, municipality, county, minimumelevationinmeters, maximumelevationinmeters, hascoordinates, decimallatitude, decimallongitude, hasmedia, associatedmedia";
	public static final String SOME_OCCURRENCE_FIELDS = "auto_id, decimallatitude, decimallongitude";
	public static final String OCCURRENCE_TABLE_RESOURCE_ID = "resource_id";
	public static final String RESOURCE_FIELDS = "id, name, archive_url, gbif_package_id, record_count, publisher_fkey";
	public static final String NO_NULL_GIS = "and decimallatitude is not null and decimallatitude is not null";
	
	
	
	// Queries
	private static final String QUERY_SCIENTIFIC_NAMES_RESOURCE = "SELECT distinct(occ.scientificname) FROM  occurrence occ, dwca_resource dcr where upper(occ.taxonrank) in (%) "+
   " and occ.resource_id = dcr.id and dcr.id = ? and occ.scientificname is not null order by occ.scientificname";
	
	private static final String QUERY_CITIES_OCURRENCE_RESOURCE = "SELECT distinct cdd.id, cdd.nome FROM  occurrence occ, cidades cdd where ( unaccent(upper(occ.county)) = cdd.nome_unaccent or "+
	" unaccent(upper(occ.locality)) = cdd.nome_unaccent or unaccent(upper(occ.municipality)) = cdd.nome_unaccent) and occ.resource_id = ? order by cdd.nome";
	
	private static final String QUERY_OCURRENCES_CITY_RESOURCE = "SELECT % FROM  occurrence occ, cidades cdd where ( unaccent(upper(occ.county)) = cdd.nome_unaccent or "+
			" unaccent(upper(occ.locality)) = cdd.nome_unaccent or unaccent(upper(occ.municipality)) = cdd.nome_unaccent) {OTHERAND}  and cdd.id = ? and occ.resource_id = ?";
	
	private static final String QUERY_STATES_OCURRENCE_RESOURCE = "SELECT distinct est.id, est.sigle, est.nome FROM  occurrence occ, estados est where ( unaccent(upper(occ.county)) = est.nome_unaccent or "+
			" unaccent(upper(occ.locality)) = est.nome_unaccent or unaccent(upper(occ.municipality)) = est.nome_unaccent) and occ.resource_id = ? order by est.nome";
	
	
	/**
	 * Fetches records from data portal schema, returning different sets of
	 * fields from occurrence table that match the given scientific name;
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
	 * Fetches records from dataportal schema, returning different sets of
	 * fields from occurrence table that match the given scientificname and
	 * filtering by resource;
	 * 
	 * @param scientificname
	 * @return
	 */
	public ResultSet queryOccurrencesByResource(String scientificname, int limit, int fields, int resourceId) {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			//
			if (scientificname.equalsIgnoreCase("") || scientificname.equalsIgnoreCase("null")) {
				// Avoid limit values out of the appropriate range ( <= 0)
				if (limit > 0) {
					if (fields == RETURN_ALL_FIELDS) {
						resultSet = statement.executeQuery(
								"SELECT " + ALL_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE + " WHERE "
										+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId + " limit " + limit);
					} else if (fields == RETURN_SOME_FIELDS) {
						resultSet = statement.executeQuery(
								"SELECT " + SOME_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE + " WHERE "
										+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId + " limit " + limit);
					}
				}
				// No limits required, return all records
				else {
					if (fields == RETURN_ALL_FIELDS) {
						resultSet = statement.executeQuery("SELECT " + ALL_OCCURRENCE_FIELDS + " FROM "
								+ OCCURRENCE_TABLE + " WHERE " + OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId);
					} else if (fields == RETURN_SOME_FIELDS) {
						resultSet = statement.executeQuery("SELECT " + SOME_OCCURRENCE_FIELDS + " FROM "
								+ OCCURRENCE_TABLE + " WHERE " + OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId);
					}
				}
			} else {
				if (limit > 0) {
					if (fields == RETURN_ALL_FIELDS) {
						resultSet = statement.executeQuery("SELECT " + ALL_OCCURRENCE_FIELDS + " FROM "
								+ OCCURRENCE_TABLE + " WHERE scientificname = \'" + scientificname + "\' AND "
								+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId + " limit " + limit);
					} else if (fields == RETURN_SOME_FIELDS) {
						resultSet = statement.executeQuery("SELECT " + SOME_OCCURRENCE_FIELDS + " FROM "
								+ OCCURRENCE_TABLE + " WHERE scientificname = \'" + scientificname + "\' AND "
								+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId + " limit " + limit);
					}
				}
				// No limits required, return all records
				else {
					if (fields == RETURN_ALL_FIELDS) {
						resultSet = statement.executeQuery("SELECT " + ALL_OCCURRENCE_FIELDS + " FROM "
								+ OCCURRENCE_TABLE + " WHERE scientificname = \'" + scientificname + "\' AND "
								+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId);
					} else if (fields == RETURN_SOME_FIELDS) {
						resultSet = statement.executeQuery("SELECT " + SOME_OCCURRENCE_FIELDS + " FROM "
								+ OCCURRENCE_TABLE + " WHERE scientificname = \'" + scientificname + "\' AND "
								+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId);
					}
				}
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
	 * Fetches records from dataportal schema, returning fields from occurrence
	 * table that match the given scientificname and field set, filtering by
	 * resource;
	 * 
	 * @param scientificname
	 * @return
	 */
	public ResultSet queryOccurrencesIgnoreNullCoordinatesByResource(String scientificname, int limit, int fields,
			int resourceId) {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			if (scientificname.equalsIgnoreCase("") || scientificname.equalsIgnoreCase("null")) {
				// Avoid limit values out of the appropriate range ( <= 0)
				if (limit > 0) {
					if (fields == RETURN_ALL_FIELDS) {
						resultSet = statement
								.executeQuery("SELECT " + ALL_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE
										+ " WHERE decimallatitude is not null and decimallatitude is not null AND "
										+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId + " limit " + limit);
					} else if (fields == RETURN_SOME_FIELDS) {
						resultSet = statement
								.executeQuery("SELECT " + SOME_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE
										+ " WHERE decimallatitude is not null and decimallatitude is not null AND "
										+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId + " limit " + limit);
					}
				}
				// No limits required, return all records
				else {
					if (fields == RETURN_ALL_FIELDS) {
						resultSet = statement
								.executeQuery("SELECT " + ALL_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE
										+ " WHERE decimallatitude is not null and decimallatitude is not null AND "
										+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId);
					} else if (fields == RETURN_SOME_FIELDS) {
						resultSet = statement
								.executeQuery("SELECT " + SOME_OCCURRENCE_FIELDS + " FROM " + OCCURRENCE_TABLE
										+ " WHERE decimallatitude is not null and decimallatitude is not null AND "
										+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId);
					}
				}
			} else {
				// Avoid limit values out of the appropriate range ( <= 0)
				if (limit > 0) {
					if (fields == RETURN_ALL_FIELDS) {
						resultSet = statement.executeQuery("SELECT " + ALL_OCCURRENCE_FIELDS + " FROM "
								+ OCCURRENCE_TABLE + " WHERE scientificname = \'" + scientificname
								+ "\' and decimallatitude is not null and decimallatitude is not null AND "
								+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId + " limit " + limit);
					} else if (fields == RETURN_SOME_FIELDS) {
						resultSet = statement.executeQuery("SELECT " + SOME_OCCURRENCE_FIELDS + " FROM "
								+ OCCURRENCE_TABLE + " WHERE scientificname = \'" + scientificname
								+ "\' and decimallatitude is not null and decimallatitude is not null AND "
								+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId + " limit " + limit);
					}
				}
				// No limits required, return all records
				else {
					if (fields == RETURN_ALL_FIELDS) {
						resultSet = statement.executeQuery("SELECT " + ALL_OCCURRENCE_FIELDS + " FROM "
								+ OCCURRENCE_TABLE + " WHERE scientificname = \'" + scientificname
								+ "\' and decimallatitude is not null and decimallatitude is not null AND "
								+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId);
					} else if (fields == RETURN_SOME_FIELDS) {
						resultSet = statement.executeQuery("SELECT " + SOME_OCCURRENCE_FIELDS + " FROM "
								+ OCCURRENCE_TABLE + " WHERE scientificname = \'" + scientificname
								+ "\' and decimallatitude is not null and decimallatitude is not null AND "
								+ OCCURRENCE_TABLE_RESOURCE_ID + " = " + resourceId);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Fetches records from dwca_resource table
	 * 
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
					+ OCCURRENCE_TABLE + " where taxonrank in ('espécie', 'EspÈcie', 'SPECIES','ESPECIE', 'species')");
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
					+ OCCURRENCE_TABLE + " where taxonrank in ('GENUS', 'GÍnero', 'gênero', 'genus')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Retrieve the taxa data on as specific resource.
	 * 
	 * @return
	 */
	public ResultSet queryScienticNamesInaResource(TAXONOMIAS whichType, Long whichResource) {
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		try {
			statement = conn.prepareStatement(QUERY_SCIENTIFIC_NAMES_RESOURCE.replace("%", preparePlaceHolders(whichType.getSinonimos().length)));
			statement.setLong(whichType.getSinonimos().length+1, whichResource);
			LOGGER.debug("Query com : "+QUERY_SCIENTIFIC_NAMES_RESOURCE.replace("%", preparePlaceHolders(whichType.getSinonimos().length))+" e recurso id :"+whichResource);
			setValues(statement,whichType.getSinonimos());
			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Retrieve the Brazil's Cities with occurrences data on as specific resource.
	 * 
	 * @return
	 */
	public ResultSet queryCitiesWithOcurrencesInaResource(Long whichResource) {
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		LOGGER.debug("Query com : "+QUERY_CITIES_OCURRENCE_RESOURCE.replace("?", String.valueOf(whichResource)));
		try {
			statement = conn.prepareStatement(QUERY_CITIES_OCURRENCE_RESOURCE);
			statement.setLong(1, whichResource);
			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Retrieve the Brazil's States with occurrences data on as specific resource.
	 * 
	 * @return
	 */
	public ResultSet queryStatesWithOcurrencesInaResource(Long whichResource) {
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		LOGGER.debug("Query com : "+QUERY_STATES_OCURRENCE_RESOURCE.replace("?", String.valueOf(whichResource)));
		try {
			statement = conn.prepareStatement(QUERY_STATES_OCURRENCE_RESOURCE);
			statement.setLong(1, whichResource);
			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Retrieve the Brazil's States with occurrences data on as specific resource.
	 * 
	 * @return
	 */
	public ResultSet queryOcurrencesInaResourceInaCity(Long whichResource,Long whichCity,int todosRegistros, Boolean ignoreNullGIS) {
		ResultSet resultSet = null;
		PreparedStatement statement = null;

		LOGGER.debug("Query com : "+QUERY_OCURRENCES_CITY_RESOURCE.replace("%", RETURN_SOME_FIELDS == todosRegistros ? 
				SOME_OCCURRENCE_FIELDS :ALL_OCCURRENCE_FIELDS ).replace("{OTHERAND}", ignoreNullGIS ? NO_NULL_GIS : "" ));
		
		try {
			statement = conn.prepareStatement(QUERY_OCURRENCES_CITY_RESOURCE.replaceAll("%", RETURN_SOME_FIELDS == todosRegistros ? 
					SOME_OCCURRENCE_FIELDS :ALL_OCCURRENCE_FIELDS ).replace("{OTHERAND}", ignoreNullGIS ? NO_NULL_GIS : "" ));
			statement.setLong(1, whichCity);
			statement.setLong(2, whichResource);
			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	
	private static String preparePlaceHolders(int length) {
	    return String.join(",", Collections.nCopies(length, "?"));
	}
	
	private static void setValues(PreparedStatement preparedStatement, String... values) throws SQLException {
	    for (int i = 0; i < values.length; i++) {
	        preparedStatement.setObject(i + 1, values[i]);
	    }
	}
	
	/**
	 * SELECT count(occ.*) FROM  occurrence occ, cidades cdd
where unaccent(upper(occ.county)) = cdd.nome_unaccent;

SELECT COUNT(occ.*) FROM  occurrence occ, cidades cdd
where unaccent(upper(occ.locality)) = cdd.nome_unaccent;

SELECT count( occ.*) FROM  occurrence occ, cidades cdd
where unaccent(upper(occ.municipality)) = cdd.nome_unaccent;
	 */
	
}

