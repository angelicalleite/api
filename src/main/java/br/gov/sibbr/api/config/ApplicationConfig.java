package br.gov.sibbr.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import br.gov.sibbr.api.config.web.filters.TokenFilter;

@WebAppConfiguration
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "br.gov.sibbr.api.config.db", "br.gov.sibbr.api.config.services",
		"br.gov.sibbr.api.service", "br.gov.sibbr.api.config", "br.gov.sibbr.api.controller", "br.gov.sibbr.api.db",
		"br.gov.sibbr.api.config.web.filters" })
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = { ApplicationConfig.class })
public class ApplicationConfig extends WebMvcConfigurerAdapter {

	@Autowired(required=true)
	TokenFilter tFilter;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/stylesheets/**").addResourceLocations("/static/stylesheets/");
	}

	@Bean(name = "templateResolver")
	public ServletContextTemplateResolver getTemplateResolver() {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setPrefix("/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("XHTML");
		templateResolver.setTemplateMode("LEGACYHTML5");
	    templateResolver.setCharacterEncoding("UTF-8");
	    templateResolver.setCacheable(false);
		return templateResolver;
	}

	@Bean(name = "templateEngine")
	public SpringTemplateEngine getTemplateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(getTemplateResolver());
		return templateEngine;
	}

	@Bean(name = "viewResolver")
	public ThymeleafViewResolver getViewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(getTemplateEngine());
	    viewResolver.setContentType("text/html;charset=UTF-8");
	    viewResolver.setCharacterEncoding("utf-8");
		return viewResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(tFilter).excludePathPatterns("/","/admin/**","/unauth",
	    		"/alterarSenha","/login","/registrar","/estatisticas/**","/recurso","/ocorrencia","/erro");
	}
	
/*	@Bean(name = "messageSource")
	public MessageSource getMessageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("/WEB-INF/i18/blogmsg");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
*/
	
}
