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

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.sibbr.api.model.ErrorResult;
import br.gov.sibbr.api.model.OccurrenceResult;
import br.gov.sibbr.api.model.Resource;
import br.gov.sibbr.api.model.ResourceResult;
import br.gov.sibbr.api.service.AuthService;
import br.gov.sibbr.api.service.DatabaseService;
import br.gov.sibbr.api.utils.TAXONOMIAS;

@RestController
/**
 * Controller class for the management of all resource related calls to the API
 * @author Pedro Guimarães
 *
 */
@Component
public class ResourceController {

	@Autowired(required=true)
	AuthService authService;
	
	@Autowired(required=true)
	DatabaseService databaseService;

	@RequestMapping(value = "/recursos", method = RequestMethod.GET)
	public Object resources(@RequestParam(value = "token", defaultValue = "null") String token) {
		// Check of the user has proper access grant token
		String tokenCheck = authService.checkToken(token);
		// If user provided a valid token, proceed:
		if (tokenCheck == null) {

			Long startTimeInMs = System.currentTimeMillis();
			ArrayList<Resource> resources = databaseService.fetchResources();
			Long totalTimeInMs = databaseService.calculateTimeLapse(startTimeInMs, System.currentTimeMillis());
			ResourceResult resourceResult = new ResourceResult(resources, totalTimeInMs);
			return resourceResult;
		}
		return new ErrorResult(tokenCheck);
	}

	// Method responsible for managing occurrence requests with resource
	// filtering
	@Cacheable("resource_occurrence")
	@RequestMapping(value = "/recursos/{id}/ocorrencias", method = RequestMethod.GET)
	public Object occurrencesByResource(@PathVariable String id,
			@RequestParam(value = "scientificname", defaultValue = "") String scientificname,
			@RequestParam(value = "ignoreNullCoordinates", defaultValue = "false") String ignorenullcoordinates,
			@RequestParam(value = "limit", defaultValue = "0") String limit,
			@RequestParam(value = "fields", defaultValue = "0") String fields,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		Long startTimeInMs = System.currentTimeMillis();
		int intResourceId = Integer.parseInt(id);
		int intLimit = Integer.parseInt(limit);
		int intFields = Integer.parseInt(fields);
		// Check of the user has proper access grant token
		String tokenCheck = authService.checkToken(token);
		// If user provided a valid token, proceed:
		if (tokenCheck == null) {
			if (ignorenullcoordinates.equalsIgnoreCase("false")) {
				ArrayList<?> occurrences = databaseService.fetchOccurrencesByResource(scientificname, false, intLimit,
						intFields, intResourceId);
				Long totalTimeInMs = databaseService.calculateTimeLapse(startTimeInMs, System.currentTimeMillis());
				return new OccurrenceResult(scientificname, occurrences, totalTimeInMs);
			} else if (ignorenullcoordinates.equalsIgnoreCase("true")) {
				ArrayList<?> occurrences = databaseService.fetchOccurrencesByResource(scientificname, true, intLimit,
						intFields, intResourceId);
				Long totalTimeInMs = databaseService.calculateTimeLapse(startTimeInMs, System.currentTimeMillis());
				return new OccurrenceResult(scientificname, occurrences, totalTimeInMs);
			}
		}
		// The user has bad token authentication, display error message:
		else {
			return new ErrorResult(tokenCheck);
		}
		return new ErrorResult("No scientific name provided for the search");
	}
	
	// Method responsible for returning the available scientific data within a resource
		@RequestMapping(value = "/recursos/{id}/sdata", method = RequestMethod.GET)
		public Object scientificDataByResource(@PathVariable String id,
				@RequestParam(value = "token", defaultValue = "null") String token,
				@RequestParam(value = "type", defaultValue = "genus") String theType
				) {
			Long intResourceId = Long.parseLong(id);
			// Check of the user has proper access grant token
			String tokenCheck = authService.checkToken(token);
			// If user provided a valid token, proceed:
			TAXONOMIAS theTax = TAXONOMIAS.getByLatinName(theType);
			if (tokenCheck == null && theTax != null) {
				return databaseService.getScientifcDataOnResource(intResourceId, theTax);
			}
			// The user has bad token authentication, display error message:
			else {
				return theTax == null ? new ErrorResult("Taxonomia Inválida") : new ErrorResult(tokenCheck) ;
			}
		}
}