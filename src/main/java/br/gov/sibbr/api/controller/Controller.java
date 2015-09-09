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
public class Controller {

	// Auxiliary service class
	Service service = new Service();

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/occurrences", method = RequestMethod.GET)
	public Result occurrence(@RequestParam(value = "scientificname", defaultValue = "") String scientificname,
			@RequestParam(value = "ignoreNullCoordinates", defaultValue = "false") String ignorenullcoordinates,
			@RequestParam(value = "limit", defaultValue = "0") String limit) {
		ArrayList<Occurrence> occurrences = null;
		int intLimit = Integer.parseInt(limit);
		if (ignorenullcoordinates.equalsIgnoreCase("false")) {
			occurrences = service.fetchOccurrences(scientificname, false, intLimit);
		} else {
			occurrences = service.fetchOccurrences(scientificname, true, intLimit);
		}
		return new Result(scientificname, occurrences);
	}
	
}
