package br.gov.sibbr.api.model;

/**
 * Class to model a POJO for an api_user instance
 * @author Pedro Guimarães
 *
 */
public class ApiUser {
	
	private Long auto_id;
	private String email;
	private Boolean authorized;
	
	/**
	 * Default class constructor
	 * @param auto_id
	 * @param email
	 * @param authorized
	 */
	public ApiUser(Long auto_id, String email, Boolean authorized) {
		this.auto_id = auto_id;
		this.email = email;
		this.authorized = authorized;
	}

	public Long getAuto_id() {
		return auto_id;
	}

	public void setAuto_id(Long auto_id) {
		this.auto_id = auto_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getAuthorized() {
		return authorized;
	}

	public void setAuthorized(Boolean authorized) {
		this.authorized = authorized;
	}
}
