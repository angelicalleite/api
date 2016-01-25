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
import br.gov.sibbr.api.service.AuthService;
import br.gov.sibbr.api.service.DatabaseService;

/**
 * Controller responsible for managing URL requests and calling for services to
 * provide occurrence data
 * 
 * @author Pedro Guimarães
 *
 */
@RestController
@Component
public class OccurrenceController {

	@Autowired(required=true)
	AuthService authService;
	
	@Autowired(required=true)
	DatabaseService databaseService;

	// Method responsible for managing occurrence requests
	@Cacheable("occurrence")
	@RequestMapping(value = "/ocorrencias", method = RequestMethod.GET)
	public Object occurrence(
			@RequestParam(value = "scientificname", defaultValue = "null") String scientificname,
			@RequestParam(value = "ignoreNullCoordinates", defaultValue = "false") String ignorenullcoordinates,
			@RequestParam(value = "limit", defaultValue = "0") String limit,
			@RequestParam(value = "fields", defaultValue = "0") String fields) {
		Long startTimeInMs = System.currentTimeMillis();
		int intLimit = Integer.parseInt(limit);
		int intFields = Integer.parseInt(fields);
		// Avoid returning all records when no filter is provided
		if (!scientificname.equalsIgnoreCase("null")) {
			if (ignorenullcoordinates.equalsIgnoreCase("false")) {
				ArrayList<?> occurrences = databaseService.fetchOccurrences(scientificname, false, intLimit,
						intFields);
				Long totalTimeInMs = databaseService.calculateTimeLapse(startTimeInMs, System.currentTimeMillis());
				return new OccurrenceResult(scientificname, occurrences, totalTimeInMs);
			} else if (ignorenullcoordinates.equalsIgnoreCase("true")) {
				ArrayList<?> occurrences = databaseService.fetchOccurrences(scientificname, true, intLimit,
						intFields);
				Long totalTimeInMs = databaseService.calculateTimeLapse(startTimeInMs, System.currentTimeMillis());
				return new OccurrenceResult(scientificname, occurrences, totalTimeInMs);
			}
		}
		// No scientificname provided:
		return new ErrorResult("No scientific name provided for the search");

	}
	
	@RequestMapping(value = "/ocorrencias/{ido}/cidade/{idc}/alldata", method = RequestMethod.GET)
	public Object fullOccurrencesOnaCityOnaResource(@PathVariable("ido") String ido,@PathVariable("idc") String idc,
			@RequestParam(value = "ignc", defaultValue = "false") String ignorenullcoordinates) {
			Long resourceId = Long.parseLong(ido);
			Long cityId = Long.parseLong(idc);
				if (resourceId != null && cityId != null) {
					return databaseService.getExtendedOcurrencesByResourceAndCity(resourceId, cityId, ignorenullcoordinates.equalsIgnoreCase("true"));
				}	
				
				return resourceId == null ? new ErrorResult("No Resource Id provided for the search"):
					new ErrorResult("No City Id provided for the search");
				
		
	}
	@RequestMapping(value = "/ocorrencias/{ido}/cidade/{idc}/somedata", method = RequestMethod.GET)
	public Object partialOccurrencesOnaCityOnaResource(@PathVariable("ido") String ido,@PathVariable("idc") String idc,
			@RequestParam(value = "ignc", defaultValue = "false") String ignorenullcoordinates) {
			Long resourceId = Long.parseLong(ido);
			Long cityId = Long.parseLong(idc);
			if (resourceId != null && cityId != null) {
				return databaseService.getReducedOcurrencesByResourceAndCity(resourceId, cityId, ignorenullcoordinates.equalsIgnoreCase("true"));
			}	
			
			return resourceId == null ? new ErrorResult("No Resource Id provided for the search"):
				new ErrorResult("No City Id provided for the search");

	}
}