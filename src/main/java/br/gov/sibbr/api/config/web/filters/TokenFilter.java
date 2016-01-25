package br.gov.sibbr.api.config.web.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import br.gov.sibbr.api.model.ErrorResult;
import br.gov.sibbr.api.service.AuthService;

@Component
public class TokenFilter implements HandlerInterceptor {

	private final static String TOKENPARAM = "token";
	private static final Logger LOGGER =  LoggerFactory.getLogger(TokenFilter.class);
	
	@Autowired(required=true)
	AuthService authService;

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
			LOGGER.debug("afterCompletion...");
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		
		 	LOGGER.debug("postHandle...");
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		
		
		LOGGER.debug("preHandle() init - authService = "+authService);
		
		if (request.getParameter(TOKENPARAM) == null){
			request.setAttribute("terror", "Sem Token");
			request.getRequestDispatcher("/unauth").forward(request, response);
			LOGGER.error("Token vazio, adieu");
		}
		
		String theToken = request.getParameter(TOKENPARAM).toString();
		if (theToken.isEmpty()){
			request.setAttribute("terror", "Sem Token");
			request.getRequestDispatcher("/unauth").forward(request, response);
			LOGGER.error("Token vazio, adieu");
		}
		
		String tokenCheck = authService.checkToken(theToken);
		if (tokenCheck != null && ! tokenCheck.isEmpty()){
			LOGGER.error("Token inválido : "+tokenCheck);
			//request.getSession(true).setAttribute("terror", new ErrorResult(tokenCheck));
			request.setAttribute("terror", tokenCheck);
			request.getRequestDispatcher("/unauth").forward(request, response);
		}

		LOGGER.debug("preHandle() exit");
		return true;
	}
	
	
}
