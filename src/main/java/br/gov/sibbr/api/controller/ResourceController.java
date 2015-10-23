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

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.gov.sibbr.api.model.ResourceResult;
import br.gov.sibbr.api.service.Service;

@RestController
public class ResourceController {

	// Auxiliary service class
	Service service = new Service();
	
	@RequestMapping(value = "/recursos", method = RequestMethod.GET)
	public ResourceResult resources(Model model) {
		ResourceResult resourceResult = new ResourceResult(service.fetchResources());
		return resourceResult;
	}
}
