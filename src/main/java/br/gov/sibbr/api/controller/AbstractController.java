package br.gov.sibbr.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.sibbr.api.service.AuthService;

public class AbstractController {
	
	protected final Logger LOGGER =  LoggerFactory.getLogger(this.getClass());
	
	@Autowired(required=true)
	protected AuthService authService;

}
