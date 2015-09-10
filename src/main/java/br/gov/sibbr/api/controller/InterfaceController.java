package br.gov.sibbr.api.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class InterfaceController implements ErrorController {

	private final String PATH = "/erro";

	@RequestMapping("/")
	public String greeting(Model model) {
		return "index";
	}

	@RequestMapping(value = PATH)
	public String error() {
		return "error";
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}
}
