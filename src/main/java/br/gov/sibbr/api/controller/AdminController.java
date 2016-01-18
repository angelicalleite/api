package br.gov.sibbr.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.sibbr.api.model.ErrorResult;
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

	// Service layer responsible for communicating with the database
	@Autowired(required=true)
	AuthService authService;

	// Method responsible for providing the list of all users
	@RequestMapping(value = "/admin/users", method = RequestMethod.GET)
	public Object listUsers(@RequestParam(value = "token", defaultValue = "null") String token, Model model) {
		String message = null;
		if (token != null) {
			message = authService.checkTokenAdmin(token);
			// The token is valid for the admin user, fetch user list
			if (message == null) {
				return authService.fetchApiUsers();
			}
		}
		// Something went wrong, display error message
		return new ErrorResult(message);
	}

	// Method responsible for providing the list of all authorized users
	@RequestMapping(value = "/admin/users/authorized", method = RequestMethod.GET)
	public Object listAuthorizedUsers(@RequestParam(value = "token", defaultValue = "null") String token, Model model) {
		String message = null;
		if (token != null) {
			message = authService.checkTokenAdmin(token);
			// The token is valid for the admin user, fetch user list
			if (message == null) {
				return authService.fetchApiAuthorizedUsers();
			}
		}
		// Something went wrong, display error message
		return new ErrorResult(message);
	}

	// Method responsible for providing the list of all unauthorized users
	@RequestMapping(value = "/admin/users/unauthorized", method = RequestMethod.GET)
	public Object listUnauthorizedUsers(@RequestParam(value = "token", defaultValue = "null") String token,
			Model model) {
		String message = null;
		if (token != null) {
			message = authService.checkTokenAdmin(token);
			// The token is valid for the admin user, fetch user list
			if (message == null) {
				return authService.fetchApiUnauthorizedUsers();
			}
		}
		// Something went wrong, display error message
		return new ErrorResult(message);
	}

	@RequestMapping(value = "/admin/users/{id}/authorize", method = RequestMethod.GET)
	public Object occurrencesByResource(@PathVariable String id,
			@RequestParam(value = "token", defaultValue = "null") String token) {
		String message = null;
		Long userId = Long.parseLong(id);
		if (token != null) {
			// Check of the user provided proper admin token:
			String tokenCheck = authService.checkTokenAdmin(token);
			// User provided a valid admin token, proceed:
			if (tokenCheck == null) {
				// Check if the auto_id is valid
				message = authService.checkApiUser(userId); 
				if (message != null) {
					return authService.habilitateApiUser(userId);
				}
				// Invalid user id:
				else {
					return new ErrorResult("Invalid user id provided. Please check again the id of the user you intend to authorize.");
				}
			}
			// The user has bad token authentication, display error message:
			else {
				return new ErrorResult(tokenCheck);
			}
		}
		return new ErrorResult("No token provided. Please provide a valid admin token to perform this operation.");
	}
}
