package br.gov.sibbr.api.utils;

public enum TAXONOMIAS {
	DOMINIO("regio","domínio","DOMAIN","REGIO","IMPERIO"),
	REINO("regnum","reino","KINGDOM"),
	FILO("phylum","filo","FILO","PHYLUM"),
	CLASSE("classis","classe","CLASS","CLASSE"),
	ORDEM("ordo","ordem","ORDER","ORDEM"),
	FAMILIA("familia","família","FAMILY","FAMÍLIA","FAMÌLIA"),
	GENERO("genus","gênero","GENUS","GÍNERO","GÊNERO"),
	SUBGENERO("subgenus","sub gênero","SUB-GÊNERO","SUB-GÍNERO","SUB GÊNERO","SUBGÊNERO"),
	ESPECIE("species","espécie","ESPÉCIE","ESPÈCIE","SPECIES","ESPECIE"),
	SUBESPECIE("subspecies","sub espécie","SUBSPECIES","SUB-ESPÈCIE","SUBESPÈCIE","SUBESPÉCIE"),
	VARIEDADE("varietate","variedade","VARIETY","VARIEDADE"),
	SUBVARIEDADE("subvarietate","sub variedade","SUBVARIETY","SUB-VARIEDADE");
	
	private TAXONOMIAS(String lat, String por, String... sinonimos){
		this.latin = lat;
		this.portugues = por;
		this.sinonimos = sinonimos;
	}
	public String getLatin() {
		return latin;
	}
	public String getPortugues() {
		return portugues;
	}
	public String[] getSinonimos() {
		return sinonimos;
	}
	private final String latin;
	private final String portugues;
	private final String[] sinonimos;

	public static TAXONOMIAS getByLatinName(String name){
		for (TAXONOMIAS cada : TAXONOMIAS.values()){
			if(cada.latin.equalsIgnoreCase(name)){
				return cada;
			}
		}
		return null;
	}
}
