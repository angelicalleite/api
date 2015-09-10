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

import java.util.ArrayList;

/**
 * This class should model a full api result to be mapped into a json object,
 * including arbitrary headers.
 * 
 * @author Pedro Guimarães
 *
 */

public class Result {

	// Represents the requested scientificname
	private String scientificname;
	
	// Represents the total amount of results
	private int count;
	
	// The actual dataset of results
	private ArrayList<Occurrence> occurrences;

	/**
	 * Default constructor
	 * @param scientificname
	 * @param occurrences
	 */
	public Result(String scientificname, ArrayList<Occurrence> occurrences) {
		this.scientificname = scientificname;
		this.count = occurrences.size();
		this.occurrences = occurrences;
	}
	
	public String getScientificName() {
		return scientificname;
	}

	public void setScientificName(String scientificName) {
		this.scientificname = scientificName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ArrayList<Occurrence> getOccurrences() {
		return occurrences;
	}

	public void setOccurrences(ArrayList<Occurrence> occurrences) {
		this.occurrences = occurrences;
	}
}
