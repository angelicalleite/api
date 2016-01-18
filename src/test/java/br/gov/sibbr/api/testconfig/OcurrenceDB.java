package br.gov.sibbr.api.testconfig;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:database.properties")
public class OcurrenceDB {

	@Value("${db.dataUrl}")
    private String database_URL;
	
	@Value("${db.dataUser}")
    private String database_User;	
	
	@Value("${db.dataPassword}")
    private String database_Pass;
	
	@Value("${db.driverclass}")
    private String database_DriverClass;
	
	@Value("${dbauth.dataUrl}")
    private String databaseauth_URL;
	
	@Value("${dbauth.dataUser}")
    private String databaseauth_User;	
	
	@Value("${dbauth.dataPassword}")
    private String databaseauth_Pass;
	
	@Value("${dbauth.driverclass}")
    private String databaseauth_DriverClass;
	
	@Bean(name="ocorrenciasdb")
	@Primary
	public DataSource dataSource(){
		DriverManagerDataSource ds = new DriverManagerDataSource(database_URL, database_User, database_Pass);
		return ds;
		 //return DataSourceBuilder.create().driverClassName(database_DriverClass).username(database_User).password(database_Pass).url(database_URL).build();
	}
	
	@Bean(name="authdb")
	@Primary
	public DataSource authdataSource(){
		DriverManagerDataSource ds = new DriverManagerDataSource(databaseauth_URL, databaseauth_User, databaseauth_Pass);
		return ds;
		 //return DataSourceBuilder.create().driverClassName(databaseauth_DriverClass).username(databaseauth_User).password(databaseauth_Pass).url(databaseauth_URL).build();
	}
	
	
}
