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

import br.gov.sibbr.api.model.LoginForm;
import br.gov.sibbr.api.service.AuthService;

/**
 * Controller for the general html templates.
 * 
 * @author Pedro Guimarães
 *
 */
@Controller
public class InterfaceController implements ErrorController {

	private final String ERROR_PATH = "/erro";

	private AuthService authService = new AuthService();

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(LoginForm loginForm, Model model) {
		String email = loginForm.getEmail();
		String password = loginForm.getPassword();
		if (email != null && password != null) {
			String message = authService.checkPassword(email, password);
			if (message == null) {
				// Successful authentication with valid credentials, fetch user
				// token:
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

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(LoginForm loginForm, Model model) {
		String email = loginForm.getEmail();
		String password = loginForm.getPassword();
		String passwordCheck = loginForm.getPasswordCheck();
		if (password != null || passwordCheck != null) {
			if (email == null || email.contains("@")) {
				// Check if both passwords are equal:
				if (password.equalsIgnoreCase(passwordCheck)) {
					if (password.length() >= 8) {
						String message = authService.createAccount(email, password);
						model.addAttribute("success", message);
					}
					// Password too small:
					else {
						model.addAttribute("error",
								"Password too small. Password must be at least 5 characters long, with a valid address.");
					}
				}
				// Passwords don't match
				else {
					model.addAttribute("error",
							"The passwords don't match. Try again, and make sure the same password is entered in both password and password verification fields.");
				}
			}
			// Invalid e-mail information
			else {
				model.addAttribute("error", "Invalid e-mail. Try again, with a valid address.");
			}
		}
		// One of the fields was left blank:
		else { 
			model.addAttribute("error", "You must provide a valid password and repeat it on the Veirification field. Please, try again.");
		}
		return "register";
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/admin", method = RequestMethod.POST)
	public String admin(LoginForm loginForm, Model model) {
		String email = loginForm.getEmail();
		String password = loginForm.getPassword();
		if (email != null && password != null) {
			String message = authService.checkPassword(email, password);
			if (message == null) {
				// Successful authentication with valid credentials, fetch user
				// token:
				String token = authService.fetchToken(email);
				if (token != null) {
					model.addAttribute("token", token);
					return "admin_login_success";
				}
			}
			model.addAttribute("message", message);
		}
		return "admin_login_fail";
	}

	@RequestMapping("/")
	public String greeting(Model model) {
		return "index";
	}

	// Method responsible for calling the login template
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	// Method responsible for calling the registration template
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register() {
		return "register";
	}

	// Method responsible for calling the login template
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String admin() {
		return "admin";
	}

	// Method responsible for managing occurrence requests
	@RequestMapping(value = "/stats", method = RequestMethod.GET)
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
