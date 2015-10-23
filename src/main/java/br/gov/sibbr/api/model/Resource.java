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
 * This class should model an api result to be mapped into a json object,
 * including any arbitrary headers.
 * 
 * @author Pedro Guimarães
 *
 */
public class Resource {

	private Integer id;
	private String name;
	private String archiveUrl;
	private String gbifPackageId;
	private Integer recordCount;
	private Integer publisherId;

	public Resource(Integer id, String name, String archiveUrl, String gbifPackageId, Integer recordCount,
			Integer publisherId) {
		this.id = id;
		this.name = name;
		this.archiveUrl = archiveUrl;
		this.gbifPackageId = gbifPackageId;
		this.recordCount = recordCount;
		this.publisherId = publisherId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArchiveUrl() {
		return archiveUrl;
	}

	public void setArchiveUrl(String archiveUrl) {
		this.archiveUrl = archiveUrl;
	}

	public String getGbifPackageId() {
		return gbifPackageId;
	}

	public void setGbifPackageId(String gbifPackageId) {
		this.gbifPackageId = gbifPackageId;
	}

	public Integer getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}

	public Integer getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Integer publisherId) {
		this.publisherId = publisherId;
	}

}
