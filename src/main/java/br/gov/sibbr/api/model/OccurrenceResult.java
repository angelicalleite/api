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
public class OccurrenceResult {

	// Represents the requested scientificname
	private String scientificname;
	
	// Represents the total amount of results
	private int count;
	
	// The actual dataset of results
	private ArrayList<?> occurrences;
	
	// The amount of time that took the query in milliseconds
	private Long queryLength;

	/*
	 * Default constructor for queries where no scientific name is provided for the occurrence search
	 */
	public OccurrenceResult() {
		this.scientificname = "No scientific name provided for the search";
		this.count = 0;
		this.occurrences = null;
		this.queryLength = new Long(0);
	}
	
	/**
	 * Default constructor
	 * @param scientificname
	 * @param occurrences
	 */
	public OccurrenceResult(String scientificname, ArrayList<?> occurrences, Long queryLength) {
		this.scientificname = scientificname;
		this.count = occurrences.size();
		this.occurrences = occurrences;
		this.queryLength = queryLength;
	}
	
	public Long getQueryLength() {
		return queryLength;
	}

	public void setQueryLength(Long queryLength) {
		this.queryLength = queryLength;
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

	public ArrayList<?> getOccurrences() {
		return occurrences;
	}

	public void setOccurrences(ArrayList<OccurrenceExpanded> occurrences) {
		this.occurrences = occurrences;
	}
}