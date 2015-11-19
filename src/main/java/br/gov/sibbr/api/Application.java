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

package br.gov.sibbr.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
/**
 * Application entry point
 * @author Pedro Guimarães
 *
 */
public class Application {

	// List of all current cacheable calls to the API
	public static final String[] CACHEABLE_CALLS = { "occurrence",
			"resource_occurrence", "total_records", "total_geo_records",
			"total_repatriados", "total_species", "total_phylum",
			"total_class", "total_order", "total_genus", "total_family" };

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager(CACHEABLE_CALLS);
	}
}