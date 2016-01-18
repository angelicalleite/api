package br.gov.sibbr.api.test.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;


import br.gov.sibbr.api.testconfig.ApplicationTestConfig;


@ContextConfiguration(
		loader = AnnotationConfigWebContextLoader.class, 
		classes={
				ApplicationTestConfig.class
		}
)
@WebAppConfiguration
public class BasicTestClass extends AbstractTestNGSpringContextTests {
	
	protected final Logger LOGGER =  LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private WebApplicationContext context;

    protected MockMvc localMVC;
    
    @BeforeClass
    public void setup() {
    	LOGGER.debug("Setup() - start");
    	localMVC = MockMvcBuilders.webAppContextSetup(context).build();
    	LOGGER.debug("Setup() - end : LocalMVC = "+localMVC);
    }

}
