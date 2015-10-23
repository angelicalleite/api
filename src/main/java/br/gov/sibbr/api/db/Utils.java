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
	
	/**
	 * Auxiliary method to avoid 0.0 values for double fields when field value
	 * is null
	 * 
	 * @param rs
	 * @param strColName
	 * @return
	 * @throws SQLException
	 */
	public static String getString (ResultSet rs, String strColName) throws SQLException {
		String value = rs.getString(strColName);
		return rs.wasNull() ? null : value;
	}
}
