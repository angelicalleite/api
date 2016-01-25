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

package br.gov.sibbr.api.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.gov.sibbr.api.service.AuthService;
import br.gov.sibbr.api.service.DatabaseService;

/**
 * Controller responsible for managing statistics requests to the database
 * 
 * @author Pedro Guimarães
 *
 */
@RestController
public class StatisticsController {

	// Auxiliary service class
	DatabaseService databaseService = new DatabaseService();
	AuthService authService = new AuthService();

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/estatisticas/totalRegistros", method = RequestMethod.GET)
	@Cacheable("total_records")
	public Object fetchTotalRecords() {
		return databaseService.fetchTotalRecords();
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/estatisticas/totalRegistrosGeorreferenciados", method = RequestMethod.GET)
	@Cacheable("total_geo_records")
	public Object fetchTotalGeoRecords() {
		return databaseService.fetchTotalGeoRecords();
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/estatisticas/totalRegistrosRepatriados", method = RequestMethod.GET)
	@Cacheable("total_repatriados")
	public Object fetchTotalRepatriatedRecords() {
		return databaseService.fetchTotalRepatriatedRecords();
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/estatisticas/totalPublicadores", method = RequestMethod.GET)
	public Object fetchTotalPublishers() {
		return databaseService.fetchTotalPublishers();
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/estatisticas/totalRecursos", method = RequestMethod.GET)
	public Object fetchTotalResources() {
		return databaseService.fetchTotalResources();
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/estatisticas/totalEspecies", method = RequestMethod.GET)
	@Cacheable("total_species")
	public Object fetchTotalSpecies() {
		return databaseService.fetchTotalSpecies();
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/estatisticas/totalFilo", method = RequestMethod.GET)
	@Cacheable("total_phylum")
	public Object fetchTotalPhylum() {
		return databaseService.fetchTotalPhylum();
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/estatisticas/totalClasse", method = RequestMethod.GET)
	@Cacheable("total_class")
	public Object fetchTotalClass() {
		return databaseService.fetchTotalClass();
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/estatisticas/totalOrdem", method = RequestMethod.GET)
	@Cacheable("total_order")
	public Object fetchTotalOrder() {
		return databaseService.fetchTotalOrder();
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/estatisticas/totalGenero", method = RequestMethod.GET)
	@Cacheable("total_genus")
	public Object fetchTotalGenus() {
		return databaseService.fetchTotalGenus();
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/estatisticas/totalFamilia", method = RequestMethod.GET)
	@Cacheable("total_family")
	public Object fetchTotalFamily() {
		return databaseService.fetchTotalFamily();
	}
}