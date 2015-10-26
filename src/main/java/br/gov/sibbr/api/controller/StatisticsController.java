package br.gov.sibbr.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.gov.sibbr.api.model.StatsResult;
import br.gov.sibbr.api.service.Service;

@RestController
/**
 * Controller responsible for managing statistics requests to the database
 * 
 * @author Pedro Guimarães
 *
 */
public class StatisticsController {

	// Auxiliary service class
	Service service = new Service();



	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/stats/totalRegistros", method = RequestMethod.GET)
	public StatsResult fetchTotalRecords() {
		StatsResult statsResult = service.fetchTotalRecords();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/stats/totalRegistrosGeorreferenciados", method = RequestMethod.GET)
	public StatsResult fetchTotalGeoRecords() {
		StatsResult statsResult = service.fetchTotalGeoRecords();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/stats/totalRegistrosRepatriados", method = RequestMethod.GET)
	public StatsResult fetchTotalRepatriatedRecords() {
		StatsResult statsResult = service.fetchTotalRepatriatedRecords();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/stats/totalPublicadores", method = RequestMethod.GET)
	public StatsResult fetchTotalPublishers() {
		StatsResult statsResult = service.fetchTotalPublishers();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/stats/totalRecursos", method = RequestMethod.GET)
	public StatsResult fetchTotalResources() {
		StatsResult statsResult = service.fetchTotalResources();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/stats/totalEspecies", method = RequestMethod.GET)
	public StatsResult fetchTotalSpecies() {
		StatsResult statsResult = service.fetchTotalSpecies();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/stats/totalFilo", method = RequestMethod.GET)
	public StatsResult fetchTotalPhylum() {
		StatsResult statsResult = service.fetchTotalPhylum();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/stats/totalClasse", method = RequestMethod.GET)
	public StatsResult fetchTotalClass() {
		StatsResult statsResult = service.fetchTotalClass();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/stats/totalOrdem", method = RequestMethod.GET)
	public StatsResult fetchTotalOrder() {
		StatsResult statsResult = service.fetchTotalOrder();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/stats/totalGenero", method = RequestMethod.GET)
	public StatsResult fetchTotalGenus() {
		StatsResult statsResult = service.fetchTotalGenus();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/stats/totalFamilia", method = RequestMethod.GET)
	public StatsResult fetchTotalFamily() {
		StatsResult statsResult = service.fetchTotalFamily();
		return statsResult;
	}
}
