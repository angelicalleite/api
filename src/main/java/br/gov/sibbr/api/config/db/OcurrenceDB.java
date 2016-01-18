package br.gov.sibbr.api.config.db;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:database.properties")
public class OcurrenceDB {

	protected final static Logger LOGGER =  LoggerFactory.getLogger(OcurrenceDB.class);
	
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
	
	@Value("${db.jndiName}")
    private String database_JNDIName;
	
	@Value("${dbauth.jndiName}")
    private String databaseauth_JNDIName;
	
	@Bean(name="ocorrenciasdb")
	@Primary
	public DataSource dataSource(){
		if (database_JNDIName == null || database_JNDIName.isEmpty()){
			LOGGER.debug("Conexão direta : "+database_URL+" - "+database_JNDIName);
			DriverManagerDataSource ds = new DriverManagerDataSource(database_URL, database_User, database_Pass);
			return ds;
		}
		LOGGER.debug("Conexão do container : "+database_JNDIName);
		final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        DataSource dataSource = dsLookup.getDataSource(database_JNDIName);
        return dataSource;
	}
	
	@Bean(name="authdb")
	@Primary
	public DataSource authdataSource(){
		if (databaseauth_JNDIName == null || databaseauth_JNDIName.isEmpty()){
			LOGGER.debug("Conexão direta : "+databaseauth_URL);
			DriverManagerDataSource ds = new DriverManagerDataSource(databaseauth_URL, databaseauth_User, databaseauth_Pass);
			return ds;
		}
		final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        DataSource dataSource = dsLookup.getDataSource(databaseauth_JNDIName);
        return dataSource;
	}
	
	
}
