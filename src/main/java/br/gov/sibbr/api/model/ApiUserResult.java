package br.gov.sibbr.api.model;

import java.util.ArrayList;

/**
 * Class to model a POJO for a user list response in the JSON format
 * 
 * @author Pedro Guimarães
 *
 */
public class ApiUserResult {

	private ArrayList<ApiUser> users;
	
	public ApiUserResult(ArrayList<ApiUser> users) {
		this.users = users;
	}

	public ArrayList<ApiUser> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<ApiUser> users) {
		this.users = users;
	}

}
