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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.sibbr.api.model.OccurrenceResult;
import br.gov.sibbr.api.service.Service;

@RestController
/**
 * Controller responsible for managing URL requests and calling for services to
 * provide occurrence data
 * 
 * @author Pedro Guimarães
 *
 */
public class OccurrenceController {

	// Auxiliary service class
	Service service = new Service();

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/ocorrencias", method = RequestMethod.GET)
	public OccurrenceResult occurrence(
			@RequestParam(value = "scientificname", defaultValue = "null") String scientificname,
			@RequestParam(value = "ignoreNullCoordinates", defaultValue = "false") String ignorenullcoordinates,
			@RequestParam(value = "limit", defaultValue = "0") String limit,
			@RequestParam(value = "fields", defaultValue = "0") String fields) {
		int intLimit = Integer.parseInt(limit);
		int intFields = Integer.parseInt(fields);
		// Avoid returning all records when no filter is provided
		if (!scientificname.equalsIgnoreCase("null")) {
			if (ignorenullcoordinates.equalsIgnoreCase("false")) {
				return new OccurrenceResult(scientificname,
						service.fetchOccurrences(scientificname, false, intLimit, intFields));
			} else if (ignorenullcoordinates.equalsIgnoreCase("true")) {
				return new OccurrenceResult(scientificname,
						service.fetchOccurrences(scientificname, true, intLimit, intFields));
			}
		}
		return new OccurrenceResult();
	}
}
