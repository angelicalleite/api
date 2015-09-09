package br.gov.sibbr.api.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Utils {

	/**
	 * Auxiliary method to avoid 0.0 values for double fields when field value
	 * is null
	 * 
	 * @param rs
	 * @param strColName
	 * @return
	 * @throws SQLException
	 */
	public static Double getDouble(ResultSet rs, String strColName) throws SQLException {
		double nValue = rs.getDouble(strColName);
		return rs.wasNull() ? null : nValue;
	}
}
