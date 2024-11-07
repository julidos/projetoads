package com.projeto.ads.Enum;

public enum Curso {
	ADS ("Analise e desenvolvimento de sistemas"),
	CD("Ciencia de dados"),
	GTI("Gestão da tecnologia humana"),
	MARKETING("Marketing"),
	RH("Recursos humanos");
	
	private String descricao;
	private Curso(String descricao) {
		this.descricao=descricao;
	}
	public String getDescricao() {
		return this.descricao;
	}
}
