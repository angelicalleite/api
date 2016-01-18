package br.gov.sibbr.api.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@WebAppConfiguration
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "br.gov.sibbr.api.config.db", "br.gov.sibbr.api.config.services",
		"br.gov.sibbr.api.service", "br.gov.sibbr.api.config", "br.gov.sibbr.api.controller", "br.gov.sibbr.api.db",
		"br.gov.sibbr.api.config.web.filters" })
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = { ApplicationConfig.class })
public class ApplicationConfig extends WebMvcConfigurerAdapter {

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
	    templateResolver.setCacheable(true);
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

/*	@Bean(name = "messageSource")
	public MessageSource getMessageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("/WEB-INF/i18/blogmsg");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
*/
}
