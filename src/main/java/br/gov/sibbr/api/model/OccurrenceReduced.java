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

package br.gov.sibbr.api.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.gov.sibbr.api.db.Utils;

/**
 * This class should model the subset of occurrence fields that will be
 * delivered in the output for each occurrence record that matches the queries
 * 
 * @author Pedro Guimarães
 *
 */
public class OccurrenceReduced {
	public Long auto_id;
	public Double decimallatitude;
	public Double decimallongitude;

	/**
	 * Default constructor for some of the available filters for the API queries
	 * @param auto_id
	 * @param decimallatitude
	 * @param decimallongtude
	 */
	public OccurrenceReduced(Long auto_id, Double decimallatitude, Double decimallongtude) {
		this.auto_id = auto_id;
		this.decimallatitude = decimallatitude;
		this.decimallongitude = decimallongtude;
	}
	
	public OccurrenceReduced(ResultSet rSet) throws SQLException {
		this.auto_id = rSet.getLong("auto_id");
		this.decimallatitude = Utils.getDouble(rSet, "decimallatitude");
		this.decimallongitude = Utils.getDouble(rSet, "decimallongitude");

	}

	public Long getAuto_id() {
		return auto_id;
	}

	public void setAuto_id(Long auto_id) {
		this.auto_id = auto_id;
	}

	public Double getDecimallatitude() {
		return decimallatitude;
	}

	public void setDecimallatitude(Double decimallatitude) {
		this.decimallatitude = decimallatitude;
	}

	public Double getDecimallongitude() {
		return decimallongitude;
	}

	public void setDecimallongitude(Double decimallongitude) {
		this.decimallongitude = decimallongitude;
	}
}
