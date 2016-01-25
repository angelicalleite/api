package br.gov.sibbr.api.model;

public class Estado {

	private Long id;
	private String sigla;
	private String nome;
	
	public Estado(Long id, String sigla, String nome) {
		super();
		this.id = id;
		this.sigla = sigla;
		this.nome = nome;
	}
	
	public Long getId() {
		return id;
	}
	public String getSigla() {
		return sigla;
	}
	public String getNome() {
		return nome;
	}
	
	
}
