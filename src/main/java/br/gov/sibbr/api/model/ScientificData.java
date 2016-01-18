package br.gov.sibbr.api.model;

import br.gov.sibbr.api.utils.TAXONOMIAS;

public class ScientificData {

	private TAXONOMIAS taxon;
	private Long idRecurso;
	private String nomeCientifico;
	
	public String getTaxon() {
		return taxon.getLatin();
	}
	public Long getIdRecurso() {
		return idRecurso;
	}
	public String getNomeCientifico() {
		return nomeCientifico;
	}
	
	public ScientificData(TAXONOMIAS taxon, Long idRecurso, String nomeCientifico) {
		super();
		this.taxon = taxon;
		this.idRecurso = idRecurso;
		this.nomeCientifico = nomeCientifico;
	}
	
	
}
