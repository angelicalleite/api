package br.gov.sibbr.api.testconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@ComponentScan(basePackages={
		"br.gov.sibbr.api.service",
		"br.gov.sibbr.api.testconfig",
		"br.gov.sibbr.api.controller",
		"br.gov.sibbr.api.db",
		"br.gov.sibbr.api.config.web.filters"}
)
public class ApplicationTestConfig {

	@Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }
}
