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
 * Controller responsible for managing URL requests and calling for services to provide data
 * @author Pedro Guimarães
 *
 */
public class Controller {

	// Auxiliary service class
	Service service = new Service();
	
	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/occurrences", method = RequestMethod.GET)
	public Result occurrence(@RequestParam(value="scientificname", defaultValue="") String scientificname, @RequestParam(value="ignoreNullCoordinates", defaultValue="false") String ignorenullcoordinates) {
		ArrayList<Occurrence> occurrences = null;
		if (ignorenullcoordinates.equalsIgnoreCase("false")) {
			occurrences = service.fetchOccurrences(scientificname, false);
		} else {
			occurrences = service.fetchOccurrences(scientificname, true);
		}
		return new Result(scientificname, occurrences);
	}
}
