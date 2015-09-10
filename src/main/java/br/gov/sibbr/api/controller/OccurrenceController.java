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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.sibbr.api.model.Occurrence;
import br.gov.sibbr.api.model.Result;
import br.gov.sibbr.api.service.Service;

@RestController
/**
 * Controller responsible for managing URL requests and calling for services to
 * provide data
 * 
 * @author Pedro Guimarães
 *
 */
public class OccurrenceController {

	// Auxiliary service class
	Service service = new Service();

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/ocorrencias", method = RequestMethod.GET)
	public Result occurrence(@RequestParam(value = "scientificname", defaultValue = "") String scientificname,
			@RequestParam(value = "ignoreNullCoordinates", defaultValue = "false") String ignorenullcoordinates,
			@RequestParam(value = "limit", defaultValue = "0") String limit) {
		ArrayList<Occurrence> occurrences = null;
		int intLimit = Integer.parseInt(limit);
		if (ignorenullcoordinates.equalsIgnoreCase("false")) {
			occurrences = service.fetchOccurrences(scientificname, false, intLimit);
		} else if (ignorenullcoordinates.equalsIgnoreCase("true")) {
			occurrences = service.fetchOccurrences(scientificname, true, intLimit);
		}
		return new Result(scientificname, occurrences);
	}
	
}
