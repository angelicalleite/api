package br.gov.sibbr.api.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PreDestroy;

import org.codehaus.jackson.JsonGenerationException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.sibbr.api.db.DatabaseConnection;
import br.gov.sibbr.api.db.Utils;
import br.gov.sibbr.api.model.Occurrence;

/**
 * This class is responsible for communicating with the database to fetch
 * records, structure data in objects, and deliver these populated objects back
 * to the controller
 * 
 * @author Pedro Guimarães
 *
 */
public class Service {

	DatabaseConnection dbc = null;

	/**
	 * Default constructor, starts up a new connection to the database;
	 */
	public Service() {
		dbc = new DatabaseConnection();
	}

	/**
	 * Hits the db querying over table occurrence in search of matches for the
	 * provided scientificname, returning a list of populated occurrence
	 * objects. If the resultSet is null, the method returns null.
	 * 
	 * @param scientificname
	 * @return
	 */
	public ArrayList<Occurrence> fetchOccurrences(String scientificname, boolean ignoreNullCoordinates) {
		ArrayList<Occurrence> occurrences = null;
		ResultSet rs = null;
		if (ignoreNullCoordinates) {
			rs = dbc.queryOccurrencesIgnoreNullCoordinates(scientificname);
		} else {
			rs = dbc.queryOccurrences(scientificname);
		}	
		if (rs != null)
			occurrences = processResultSet(rs);

		// Close resultSet after being used:
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return occurrences;
	}

	/**
	 * Processes a given resultset into an arraylist of occurrence objects.
	 * 
	 * @param rs
	 * @return
	 */
	public ArrayList<Occurrence> processResultSet(ResultSet rs) {
		ArrayList<Occurrence> occurrences = new ArrayList<Occurrence>();
		try {
			while (rs.next()) {
				Integer auto_id = Integer.parseInt(rs.getString("auto_id"));
				Double decimallatitude = Utils.getDouble(rs, "decimallatitude");
				Double decimallongitude = Utils.getDouble(rs, "decimallongitude");
				Occurrence occurrence = new Occurrence(auto_id, decimallatitude, decimallongitude);
				occurrences.add(occurrence);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return occurrences;
	}

	/**
	 * Generates a JSON representing all results from occurrence search;
	 * 
	 * @param occurrences
	 * @return
	 */
	public OutputStream generateJSON(ArrayList<Occurrence> occurrences, String scientificname) {
		// JSON mapper that will serialize the response in JSON format:
		ObjectMapper mapper = new ObjectMapper();
		// Json stream as byte array in memory (instead of a hard drive file):
		OutputStream output = new ByteArrayOutputStream();

		// Build JSON header:
		String header = "{\"Provided name for query\": " + "\"" + scientificname + "\"" + ", \"count\": "
				+ occurrences.size() + ", \"results\": ";
		byte[] b = header.getBytes(Charset.forName("UTF-8"));
		try {
			output.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Output all results:
		try {
			// Generate json file:
			mapper.writeValue(output, occurrences);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Close JSON header with final bracket:
		try {
			output.write('}');
		} catch (IOException e) {
			e.printStackTrace();
		}

		return output;
	}

	/**
	 * Release database connection once the application stops running
	 */
	@PreDestroy
	private void destroy() {
		dbc.releaseConnection();
		System.out.println("*** Destroying database connection [Clean up]");
	}
}