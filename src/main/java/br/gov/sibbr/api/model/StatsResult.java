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

/**
 * This class should model an api result to be mapped Integero a json object,
 * including arbitrary headers. This result represents the current status of the database.
 * 
 * @author Pedro Guimarães
 *
 */

public class StatsResult {
	
	private Integer totalRecords;
	private Integer totalGeoReferencedRecords;
	private Integer totalRepatriated;
	private Integer totalPublishers;
	private Integer totalResources;
	private Integer totalSpecies;
	private Integer totalPhylum;
	private Integer totalClass;
	private Integer totalOrder;
	private Integer totalFamily;
	private Integer totalGender;
	
	/**
	 * Default class constructor
	 */
	public StatsResult(Integer totalRecords, Integer totalGeoReferencedRecords, Integer totalRepatriated, Integer totalPublishers, Integer totalResources, Integer totalSpecies, Integer totalPhylum, Integer totalClass, 
Integer totalOrder, Integer totalFamily, Integer totalGender) {
		this.totalRecords = totalRecords;
		this.totalGeoReferencedRecords = totalGeoReferencedRecords;
		this.totalRepatriated = totalRepatriated;
		this.totalPublishers = totalPublishers;
		this.totalResources = totalResources;
		this.totalSpecies = totalSpecies;
		this.totalPhylum = totalPhylum;
		this.totalClass = totalClass;
		this.totalOrder = totalOrder;
		this.totalFamily = totalFamily;
		this.totalGender = totalGender;
	}
	
	public Integer getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}
	public Integer getTotalGeoReferencedRecords() {
		return totalGeoReferencedRecords;
	}
	public void setTotalGeoReferencedRecords(Integer totalGeoReferencedRecords) {
		this.totalGeoReferencedRecords = totalGeoReferencedRecords;
	}
	public Integer getTotalRepatriated() {
		return totalRepatriated;
	}
	public void setTotalRepatriated(Integer totalRepatriated) {
		this.totalRepatriated = totalRepatriated;
	}
	public Integer getTotalPublishers() {
		return totalPublishers;
	}
	public void setTotalPublishers(Integer totalPublishers) {
		this.totalPublishers = totalPublishers;
	}
	public Integer getTotalResources() {
		return totalResources;
	}
	public void setTotalResources(Integer totalResources) {
		this.totalResources = totalResources;
	}
	public Integer getTotalSpecies() {
		return totalSpecies;
	}
	public void setTotalSpecies(Integer totalSpecies) {
		this.totalSpecies = totalSpecies;
	}
	public Integer getTotalPhylum() {
		return totalPhylum;
	}
	public void setTotalPhylum(Integer totalPhylum) {
		this.totalPhylum = totalPhylum;
	}
	public Integer getTotalClass() {
		return totalClass;
	}
	public void setTotalClass(Integer totalClass) {
		this.totalClass = totalClass;
	}
	public Integer getTotalOrder() {
		return totalOrder;
	}
	public void setTotalOrder(Integer totalOrder) {
		this.totalOrder = totalOrder;
	}
	public Integer getTotalFamily() {
		return totalFamily;
	}
	public void setTotalFamily(Integer totalFamily) {
		this.totalFamily = totalFamily;
	}
	public Integer getTotalGender() {
		return totalGender;
	}
	public void setTotalGender(Integer totalGender) {
		this.totalGender = totalGender;
	}	

}
