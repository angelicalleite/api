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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.sibbr.api.Application;
import br.gov.sibbr.api.model.OccurrenceResult;
import br.gov.sibbr.api.model.ResourceResult;
import br.gov.sibbr.api.service.Service;

@RestController
public class ResourceController {

	// Auxiliary service class
	Service service = new Service();

	@RequestMapping(value = Application.VERSION + "/recursos", method = RequestMethod.GET)
	public ResourceResult resources(Model model) {
		ResourceResult resourceResult = new ResourceResult(
				service.fetchResources());
		return resourceResult;
	}

	// Method responsible for managing occurrence requests with resource
	// filtering
	@Cacheable("resource_occurrence")
	@RequestMapping(value = Application.VERSION + "/recursos/{id}/ocorrencias", method = RequestMethod.GET)
	public OccurrenceResult occurrencesByResource(
			@PathVariable String id,
			@RequestParam(value = "scientificname", defaultValue = "") String scientificname,
			@RequestParam(value = "ignoreNullCoordinates", defaultValue = "false") String ignorenullcoordinates,
			@RequestParam(value = "limit", defaultValue = "0") String limit,
			@RequestParam(value = "fields", defaultValue = "0") String fields) {
		int intResourceId = Integer.parseInt(id);
		int intLimit = Integer.parseInt(limit);
		int intFields = Integer.parseInt(fields);
		if (ignorenullcoordinates.equalsIgnoreCase("false")) {
			return new OccurrenceResult(scientificname,
					service.fetchOccurrencesByResource(scientificname, false,
							intLimit, intFields, intResourceId));
		} else if (ignorenullcoordinates.equalsIgnoreCase("true")) {
			return new OccurrenceResult(scientificname,
					service.fetchOccurrencesByResource(scientificname, true,
							intLimit, intFields, intResourceId));
		}
		return new OccurrenceResult();
	}
}
