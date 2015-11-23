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

import br.gov.sibbr.api.Application;
import br.gov.sibbr.api.model.LoginForm;
import br.gov.sibbr.api.service.AuthService;

@Controller
/**
 * Controller for the general html templates.
 * 
 * @author Pedro Guimarães
 *
 */
public class InterfaceController implements ErrorController {

	private final String ERROR_PATH = "/erro";

	private AuthService authService = new AuthService();

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(LoginForm loginForm, Model model) {
		// TODO: receber e validar a lista de parâmetros, conectar ao banco de
		// dados para verificar usuário e senha
		String email = loginForm.getEmail();
		String password = loginForm.getPassword();
		if (email != null && password != null) {
			String message = authService.checkPassword(email, password);
			if (message == null) {
				// Successful authentication with valid credentials, fetch user token:
				String token = authService.fetchToken(email);
				if (token != null) {
					model.addAttribute("token", token);
					return "login_success";
				}
			}
			model.addAttribute("message", message);
		}
		return "login_fail";
	}

	@RequestMapping("/")
	public String greeting(Model model) {
		return "index";
	}

	// Method responsible for calling the login template
	@RequestMapping(value ="/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value ="/stats", method = RequestMethod.GET)
	public String stats() {
		return "stats";
	}

	@RequestMapping(value = ERROR_PATH)
	public String error() {
		return "error";
	}

	public String getErrorPath() {
		return ERROR_PATH;
	}
}
