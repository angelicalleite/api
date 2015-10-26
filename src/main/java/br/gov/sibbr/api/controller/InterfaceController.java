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

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
/**
 * Controller for the html templates.
 * @author Pedro Guimarães
 *
 */
public class InterfaceController implements ErrorController {

	private final String PATH = "/erro";

	@RequestMapping("/")
	public String greeting(Model model) {
		return "index";
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/stats/", method = RequestMethod.GET)
	public String stats() {
		return "stats";
	}
	
	@RequestMapping(value = PATH)
	public String error() {
		return "error";
	}

	public String getErrorPath() {
		return PATH;
	}
}
