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
 * This class should model the subset of occurrence fields that will be
 * delivered in the output for each occurrence record that matches the queries
 * 
 * @author Pedro Guimarães
 *
 */
public class OccurrenceExpanded {
	public Integer auto_id;
	public String resourcename;
	public String publishername;
	public String kingdom;
	public String phylum;
	public String _class;
	public String _order;
	public String family;
	public String genus;
	public String specificepithet;
	public String infraspecificepithet;
	public String species;
	public String scientificname;
	public String taxonrank;
	public String typestatus;
	public String recordedby;
	public String eventdate;
	public String continent;
	public String country;
	public String stateprovince;
	public String municipality;
	public String county;
	public Double minimumelevationinmeters;
	public Double maximumelevationinmeters;
	public Boolean hascoordinates;
	public Double decimallatitude;
	public Double decimallongitude;
	public Boolean hasmedia;
	public String associatedmedia;
	
	/**
	 * Default constructor with all available filters to the API queries
	 * @param auto_id
	 * @param resourcename
	 * @param publishername
	 * @param kingdom
	 * @param phylum
	 * @param _class
	 * @param _order
	 * @param family
	 * @param genus
	 * @param specificepithet
	 * @param infraspecificepithet
	 * @param species
	 * @param scientificname
	 * @param taxonrank
	 * @param typestatus
	 * @param recordedby
	 * @param eventdate
	 * @param continent
	 * @param country
	 * @param stateprovince
	 * @param municipality
	 * @param county
	 * @param minimumelevationinmeters
	 * @param maximumelevationinmeters
	 * @param hascoordinates
	 * @param decimallatitude
	 * @param decimallongitude
	 * @param hasmedia
	 * @param associatedmedia
	 */
	public OccurrenceExpanded(Integer auto_id, String resourcename, String publishername, String kingdom, String phylum,
			String _class, String _order, String family, String genus, String specificepithet,
			String infraspecificepithet, String species, String scientificname, String taxonrank, String typestatus,
			String recordedby, String eventdate, String continent, String country, String stateprovince,
			String municipality, String county, Double minimumelevationinmeters, Double maximumelevationinmeters,
			Boolean hascoordinates, Double decimallatitude, Double decimallongitude, Boolean hasmedia,
			String associatedmedia) {
		this.auto_id = auto_id;
		this.resourcename = resourcename;
		this.publishername = publishername;
		this.kingdom = kingdom;
		this.phylum = phylum;
		this._class = _class;
		this._order = _order;
		this.family = family;
		this.genus = genus;
		this.specificepithet = specificepithet;
		this.infraspecificepithet = infraspecificepithet;
		this.species = species;
		this.scientificname = scientificname;
		this.taxonrank = taxonrank;
		this.typestatus = typestatus;
		this.recordedby = recordedby;
		this.eventdate = eventdate;
		this.continent = continent;
		this.country = country;
		this.stateprovince = stateprovince;
		this.municipality = municipality;
		this.county = county;
		this.minimumelevationinmeters = minimumelevationinmeters;
		this.maximumelevationinmeters = maximumelevationinmeters;
		this.hascoordinates = hascoordinates;
		this.decimallatitude = decimallatitude;
		this.decimallongitude = decimallongitude;
		this.hasmedia = hasmedia;
		this.associatedmedia = associatedmedia;
	}

	/**
	 * Default constructor for some of the available filters for the API queries
	 * @param auto_id
	 * @param decimallatitude
	 * @param decimallongtude
	 */
	public OccurrenceExpanded(Integer auto_id, Double decimallatitude, Double decimallongtude) {
		this.auto_id = auto_id;
		this.decimallatitude = decimallatitude;
		this.decimallongitude = decimallongtude;
	}

	public Integer getAuto_id() {
		return auto_id;
	}

	public String getResourcename() {
		return resourcename;
	}

	public void setResourcename(String resourcename) {
		this.resourcename = resourcename;
	}

	public String getPublishername() {
		return publishername;
	}

	public void setPublishername(String publishername) {
		this.publishername = publishername;
	}

	public String getKingdom() {
		return kingdom;
	}

	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}

	public String getPhylum() {
		return phylum;
	}

	public void setPhylum(String phylum) {
		this.phylum = phylum;
	}

	public String get_class() {
		return _class;
	}

	public void set_class(String _class) {
		this._class = _class;
	}

	public String get_order() {
		return _order;
	}

	public void set_order(String _order) {
		this._order = _order;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getGenus() {
		return genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	public String getSpecificepithet() {
		return specificepithet;
	}

	public void setSpecificepithet(String specificepithet) {
		this.specificepithet = specificepithet;
	}

	public String getInfraspecificepithet() {
		return infraspecificepithet;
	}

	public void setInfraspecificepithet(String infraspecificepithet) {
		this.infraspecificepithet = infraspecificepithet;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getScientificname() {
		return scientificname;
	}

	public void setScientificname(String scientificname) {
		this.scientificname = scientificname;
	}

	public String getTaxonrank() {
		return taxonrank;
	}

	public void setTaxonrank(String taxonrank) {
		this.taxonrank = taxonrank;
	}

	public String getTypestatus() {
		return typestatus;
	}

	public void setTypestatus(String typestatus) {
		this.typestatus = typestatus;
	}

	public String getRecordedby() {
		return recordedby;
	}

	public void setRecordedby(String recordedby) {
		this.recordedby = recordedby;
	}

	public String getEventdate() {
		return eventdate;
	}

	public void setEventdate(String eventdate) {
		this.eventdate = eventdate;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStateprovince() {
		return stateprovince;
	}

	public void setStateprovince(String stateprovince) {
		this.stateprovince = stateprovince;
	}

	public String getMunicipality() {
		return municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public Double getMinimumelevationinmeters() {
		return minimumelevationinmeters;
	}

	public void setMinimumelevationinmeters(Double minimumelevationinmeters) {
		this.minimumelevationinmeters = minimumelevationinmeters;
	}

	public Double getMaximumelevationinmeters() {
		return maximumelevationinmeters;
	}

	public void setMaximumelevationinmeters(Double maximumelevationinmeters) {
		this.maximumelevationinmeters = maximumelevationinmeters;
	}

	public Boolean getHascoordinates() {
		return hascoordinates;
	}

	public void setHascoordinates(Boolean hascoordinates) {
		this.hascoordinates = hascoordinates;
	}

	public Boolean getHasmedia() {
		return hasmedia;
	}

	public void setHasmedia(Boolean hasmedia) {
		this.hasmedia = hasmedia;
	}

	public String getAssociatedmedia() {
		return associatedmedia;
	}

	public void setAssociatedmedia(String associatedmedia) {
		this.associatedmedia = associatedmedia;
	}

	public void setAuto_id(Integer auto_id) {
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
