package br.gov.sibbr.api.testconfig;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class ConnectionProvider {
	
	@Autowired(required=true)
	@Qualifier("ocorrenciasdb")
	private DataSource occurDB;
	
	@Autowired(required=true)
	@Qualifier("authdb")
	private DataSource authDB;
	
	@Bean(name="ocorrenciasconex")
	public Connection getOcorrenciasConex() throws SQLException{
		return occurDB.getConnection();
	}
	
	@Bean(name="authconex")
	public Connection getAuthConex() throws SQLException{
		return authDB.getConnection();
	}
}
