package br.gov.sibbr.api.test.login;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testng.Assert;
import org.testng.annotations.Test;

import br.gov.sibbr.api.test.base.BasicTestClass;
import br.gov.sibbr.api.utils.TAXONOMIAS;

public class LoginTest extends BasicTestClass {
	
  private String tokenToUse="";
  
  @Test
  public void logInAdmin() throws Exception {
	  ResultActions resAct = localMVC.perform(post("/login").param("email", "admin@sibbr.gov.br").param("password", "passw0rd"));
	  MvcResult retorno = resAct.andReturn();
	  resAct.andExpect(model().hasNoErrors()).andExpect(model().attributeExists("token"));
	  LOGGER.debug("Token : "+retorno.getModelAndView().getModel().get("token"));
	  tokenToUse = retorno.getModelAndView().getModel().get("token").toString();
  }
  
  @Test(dependsOnMethods={"logInAdmin"})
  public void searchBySpecie() throws Exception {
	  ResultActions lala = localMVC.perform(get("/recursos/2/sdata").param("type", TAXONOMIAS.ESPECIE.getLatin()).param("token", tokenToUse));
	  MvcResult retorno = lala.andReturn();
	  lala.andExpect(model().hasNoErrors()).andExpect(model().attributeExists("scientificDataList"));
	  LOGGER.debug("DAdos : "+retorno.getModelAndView().getModel().get("scientificDataList"));
  }
  
  
  
  
}
