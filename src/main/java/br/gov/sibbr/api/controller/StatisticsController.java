package br.gov.sibbr.api.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.gov.sibbr.api.Application;
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
	@RequestMapping(value = Application.VERSION + "/stats/totalRegistros", method = RequestMethod.GET)
	@Cacheable("total_records")
	public StatsResult fetchTotalRecords() {
		StatsResult statsResult = service.fetchTotalRecords();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = Application.VERSION + "/stats/totalRegistrosGeorreferenciados", method = RequestMethod.GET)
	@Cacheable("total_geo_records")
	public StatsResult fetchTotalGeoRecords() {
		StatsResult statsResult = service.fetchTotalGeoRecords();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = Application.VERSION + "/stats/totalRegistrosRepatriados", method = RequestMethod.GET)
	@Cacheable("total_repatriados")
	public StatsResult fetchTotalRepatriatedRecords() {
		StatsResult statsResult = service.fetchTotalRepatriatedRecords();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = Application.VERSION + "/stats/totalPublicadores", method = RequestMethod.GET)
	public StatsResult fetchTotalPublishers() {
		StatsResult statsResult = service.fetchTotalPublishers();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = Application.VERSION + "/stats/totalRecursos", method = RequestMethod.GET)
	public StatsResult fetchTotalResources() {
		StatsResult statsResult = service.fetchTotalResources();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = Application.VERSION + "/stats/totalEspecies", method = RequestMethod.GET)
	@Cacheable("total_species")
	public StatsResult fetchTotalSpecies() {
		StatsResult statsResult = service.fetchTotalSpecies();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = Application.VERSION + "/stats/totalFilo", method = RequestMethod.GET)
	@Cacheable("total_phylum")
	public StatsResult fetchTotalPhylum() {
		StatsResult statsResult = service.fetchTotalPhylum();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = Application.VERSION + "/stats/totalClasse", method = RequestMethod.GET)
	@Cacheable("total_class")
	public StatsResult fetchTotalClass() {
		StatsResult statsResult = service.fetchTotalClass();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = Application.VERSION + "/stats/totalOrdem", method = RequestMethod.GET)
	@Cacheable("total_order")
	public StatsResult fetchTotalOrder() {
		StatsResult statsResult = service.fetchTotalOrder();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = Application.VERSION + "/stats/totalGenero", method = RequestMethod.GET)
	@Cacheable("total_genus")
	public StatsResult fetchTotalGenus() {
		StatsResult statsResult = service.fetchTotalGenus();
		return statsResult;
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = Application.VERSION + "/stats/totalFamilia", method = RequestMethod.GET)
	@Cacheable("total_family")
	public StatsResult fetchTotalFamily() {
		StatsResult statsResult = service.fetchTotalFamily();
		return statsResult;
	}
}
