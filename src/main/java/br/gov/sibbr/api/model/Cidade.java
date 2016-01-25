package br.gov.sibbr.api.model;

public class Cidade {

	private Long id;
	private String nome;
	

	public Cidade(Long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}
	
	public Long getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	
}
