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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PreDestroy;

import br.gov.sibbr.api.db.DatabaseQueries;
import br.gov.sibbr.api.db.Utils;
import br.gov.sibbr.api.model.OccurrenceExpanded;
import br.gov.sibbr.api.model.OccurrenceReduced;
import br.gov.sibbr.api.model.StatsResult;

/**
 * This class is responsible for communicating with the database to fetch
 * records, structure data in objects, and deliver these populated objects back
 * to the controller
 * 
 * @author Pedro Guimarães
 *
 */
public class Service {

	DatabaseQueries dbq = null;

	/**
	 * Default constructor, starts up a new connection to the database;
	 */
	public Service() {
		try {
			dbq = new DatabaseQueries();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Hits the db querying over table occurrence in search of matches for the
	 * provided scientificname, returning a list of populated occurrence
	 * objects. If the resultSet is null, the method returns null.
	 * 
	 * @param scientificname
	 * @return
	 */
	public ArrayList<?> fetchOccurrences(String scientificname, boolean ignoreNullCoordinates, int limit,
			int fields) {
		ArrayList<?> occurrences = null;
		ResultSet rs = null;
		if (ignoreNullCoordinates) {
			rs = dbq.queryOccurrencesIgnoreNullCoordinates(scientificname, limit, fields);
		} else {
			rs = dbq.queryOccurrences(scientificname, limit, fields);
		}
		if (rs != null) {
			occurrences = processOccurrenceResultSet(rs, fields);
		}
		// Close resultSet after being used:
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return occurrences;
	}

	public StatsResult fetchStats() {
		ResultSet resultSet = null;
		StatsResult statsResult = null;
		// Total records:
		resultSet = dbq.queryTotalRecords();
		int totalRecords = processTotalRecords(resultSet);
		// Total georeferenced records:
		resultSet = dbq.queryTotalGeoRecords();
		int totalGeoRecords = processTotalGeoRecords(resultSet);
		// Total repatriated records:
		resultSet = dbq.queryTotalRepatriatedRecords();
		int totalRepatriatedRecords = processTotalRepatriatedRecords(resultSet);
		// Total publishers:
		resultSet = dbq.queryTotalPublishers();
		int totalPublishers = processTotalPublishers(resultSet);
		// Total resources:
		resultSet = dbq.queryTotalResources();
		int totalResources = processTotalResources(resultSet);
		// Total species:
		resultSet = dbq.queryTotalSpecies();
		int totalSpecies = processTotalSpecies(resultSet);
		// Total phylum:
		resultSet = dbq.queryTotalPhylum();
		int totalPhylum = processTotalPhylum(resultSet);
		// Total classes:
		resultSet = dbq.queryTotalClass();
		int totalClasses = processTotalClasses(resultSet);
		// Total orders:
		resultSet = dbq.queryTotalOrder();
		int totalOrders = processTotalOrders(resultSet);
		// Total families:
		resultSet = dbq.queryTotalFamily();
		int totalFamilies = processTotalFamilies(resultSet);
		// Total genus:
		resultSet = dbq.queryTotalGenus();
		int totalGenus = processTotalGenus(resultSet);
		statsResult = new StatsResult(totalRecords, totalGeoRecords, totalRepatriatedRecords, totalPublishers,
				totalResources, totalSpecies, totalPhylum, totalClasses, totalOrders, totalFamilies, totalGenus);
		return statsResult;
	}

	/**
	 * Processes a given resultset into an arraylist of occurrence objects.
	 * 
	 * @param rs
	 * @return
	 */
	public ArrayList<?> processOccurrenceResultSet(ResultSet rs, int complete) {
		ArrayList<OccurrenceExpanded> occurrencesExpanded = new ArrayList<OccurrenceExpanded>();
		ArrayList<OccurrenceReduced> occurrencesReduced = new ArrayList<OccurrenceReduced>();
		if (complete == DatabaseQueries.RETURN_SOME_FIELDS) {
			try {
				while (rs.next()) {
					Integer auto_id = Integer.parseInt(Utils.getString(rs, "auto_id"));
					Double decimallatitude = Utils.getDouble(rs, "decimallatitude");
					Double decimallongitude = Utils.getDouble(rs, "decimallongitude");
					OccurrenceReduced occurrence = new OccurrenceReduced(auto_id, decimallatitude, decimallongitude);
					occurrencesReduced.add(occurrence);
				}
				return occurrencesReduced;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (complete == DatabaseQueries.RETURN_ALL_FIELDS) {
			try {
				while (rs.next()) {
					Integer auto_id = Integer.parseInt(Utils.getString(rs, "auto_id"));
					String resourcename = Utils.getString(rs, "resourcename");
					String publishername = Utils.getString(rs, "publishername");
					String kingdom = Utils.getString(rs, "kingdom");
					String phylum = Utils.getString(rs, "phylum");
					String _class = Utils.getString(rs, "_class");
					String _order = Utils.getString(rs, "_order");
					String family = Utils.getString(rs, "family");
					String genus = Utils.getString(rs, "genus");
					String specificepithet = Utils.getString(rs, "specificepithet");
					String infraspecificepithet = Utils.getString(rs, "infraspecificepithet");
					String species = Utils.getString(rs, "species");
					String scientificname = Utils.getString(rs, "scientificname");
					String taxonrank = Utils.getString(rs, "taxonrank");
					String typestatus = Utils.getString(rs, "typestatus");
					String recordedby = Utils.getString(rs, "recordedby");
					String eventdate = Utils.getString(rs, "eventdate");
					String continent = Utils.getString(rs, "continent");
					String country = Utils.getString(rs, "country");
					String stateprovince = Utils.getString(rs, "stateprovince");
					String municipality = Utils.getString(rs, "municipality");
					String county = Utils.getString(rs, "county");
					Double minimumelevationinmeters = Utils.getDouble(rs, "minimumelevationinmeters");
					Double maximumelevationinmeters = Utils.getDouble(rs, "maximumelevationinmeters");
					Boolean hascoordinates = rs.getBoolean("hascoordinates");
					Double decimallatitude = Utils.getDouble(rs, "decimallatitude");
					Double decimallongitude = Utils.getDouble(rs, "decimallongitude");
					Boolean hasmedia = rs.getBoolean("hasmedia");
					String associatedmedia = Utils.getString(rs, "associatedmedia");
					OccurrenceExpanded occurrence = new OccurrenceExpanded(auto_id, resourcename, publishername, kingdom, phylum,
							_class, _order, family, genus, specificepithet, infraspecificepithet, species,
							scientificname, taxonrank, typestatus, recordedby, eventdate, continent, country,
							stateprovince, municipality, county, minimumelevationinmeters, maximumelevationinmeters,
							hascoordinates, decimallatitude, decimallongitude, hasmedia, associatedmedia);
					occurrencesExpanded.add(occurrence);
				}
				return occurrencesExpanded;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Integer processTotalRecords(ResultSet resultSet) {
		Integer totalRecords = null;
		try {
			while (resultSet.next()) {
				totalRecords = Integer.parseInt(resultSet.getString("totalrecords"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalRecords;
	}

	public Integer processTotalGeoRecords(ResultSet resultSet) {
		Integer totalGeoRecords = null;
		try {
			while (resultSet.next()) {
				totalGeoRecords = Integer.parseInt(resultSet.getString("totalrecords"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalGeoRecords;
	}

	/**
	 * Process the amount of total repatriated records.
	 * 
	 * @param resultSet
	 * @return
	 */
	public Integer processTotalRepatriatedRecords(ResultSet resultSet) {
		Integer totalRepatriatedRecords = null;
		try {
			while (resultSet.next()) {
				totalRepatriatedRecords = Integer.parseInt(resultSet.getString("totalrecords"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalRepatriatedRecords;
	}

	/**
	 * Process amount of publishers.
	 * 
	 * @param resultSet
	 * @return
	 */
	public Integer processTotalPublishers(ResultSet resultSet) {
		Integer totalPublishers = null;
		try {
			while (resultSet.next()) {
				totalPublishers = Integer.parseInt(resultSet.getString("totalpublishers"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalPublishers;
	}

	/**
	 * Process amount of resources.
	 * 
	 * @param resultSet
	 * @return
	 */
	public Integer processTotalResources(ResultSet resultSet) {
		Integer totalResources = null;
		try {
			while (resultSet.next()) {
				totalResources = Integer.parseInt(resultSet.getString("totalresources"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalResources;
	}

	/**
	 * Process amount of species.
	 * 
	 * @param resultSet
	 * @return
	 */
	public Integer processTotalSpecies(ResultSet resultSet) {
		Integer totalSpecies = null;
		try {
			while (resultSet.next()) {
				totalSpecies = Integer.parseInt(resultSet.getString("totalspecies"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalSpecies;
	}

	/**
	 * Process amount of phylum.
	 * 
	 * @param resultSet
	 * @return
	 */
	public Integer processTotalPhylum(ResultSet resultSet) {
		Integer totalPhylum = null;
		try {
			while (resultSet.next()) {
				totalPhylum = Integer.parseInt(resultSet.getString("totalphylum"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalPhylum;
	}

	/**
	 * Process amount of classes.
	 * 
	 * @param resultSet
	 * @return
	 */
	public Integer processTotalClasses(ResultSet resultSet) {
		Integer totalClass = null;
		try {
			while (resultSet.next()) {
				totalClass = Integer.parseInt(resultSet.getString("totalclass"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalClass;
	}

	/**
	 * Process amount of orders.
	 * 
	 * @param resultSet
	 * @return
	 */
	public Integer processTotalOrders(ResultSet resultSet) {
		Integer totalOrders = null;
		try {
			while (resultSet.next()) {
				totalOrders = Integer.parseInt(resultSet.getString("totalorder"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalOrders;
	}

	/**
	 * Process amount of families.
	 * 
	 * @param resultSet
	 * @return
	 */
	public Integer processTotalFamilies(ResultSet resultSet) {
		Integer totalFamilies = null;
		try {
			while (resultSet.next()) {
				totalFamilies = Integer.parseInt(resultSet.getString("totalfamily"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalFamilies;
	}

	/**
	 * Process amount of genus.
	 * 
	 * @param resultSet
	 * @return
	 */
	public Integer processTotalGenus(ResultSet resultSet) {
		Integer totalFamilies = null;
		try {
			while (resultSet.next()) {
				totalFamilies = Integer.parseInt(resultSet.getString("totalgenus"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalFamilies;
	}

	/**
	 * Release database connection once the application stops running
	 */
	@PreDestroy
	private void destroy() {
		dbq.releaseConnection();
		System.out.println("*** Destroying database connection [Clean up]");
	}
}