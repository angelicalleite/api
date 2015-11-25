package br.gov.sibbr.api.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.sibbr.api.service.AuthService;

/**
 * Controller responsible for managing URL requests and calling for services to
 * provide administrative operations
 * 
 * @author Pedro Guimarães
 *
 */
@RestController
public class AdminController {

	AuthService authService = new AuthService();

	// Method responsible for providing the list of all users
	@RequestMapping(value = "/admin/users", method = RequestMethod.GET)
	public String listUsers(@RequestParam(value = "token", defaultValue = "null") String token, Model model) {
		String message = authService.checkTokenAdmin(token);
		// Something went wrong. Display error message.
		if (message != null) {
			model.addAttribute("error", message);
		}
		// Proper admin identification, display
		return "users";
	}

	// Method responsible for providing the list of all users
	@RequestMapping(value = "/admin/users/authorized", method = RequestMethod.GET)
	public String listAuthorizedUsers(@RequestParam(value = "token", defaultValue = "null") String token, Model model) {
		String message = authService.checkTokenAdmin(token);
		// Something went wrong. Display error message.
		if (message != null) {
			model.addAttribute("error", message);
		}
		// Proper admin identification, display
		return "users";
	}
}
