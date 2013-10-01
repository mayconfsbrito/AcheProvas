package com.acheprovas.model;

public class Prova {

	private int id;
	private int nome;
	private String descricao;
	
	public Prova(int id, int nome, String descricao){
		super();
		this.setId(id);
		this.setNome(nome);
		this.setDescricao(descricao);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNome() {
		return nome;
	}
	public void setNome(int nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
