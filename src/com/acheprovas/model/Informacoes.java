package com.acheprovas.model;

import android.content.ContentValues;

public class Informacoes {

	private int id = 1;
	private int contExecucoes = 0;
	private int validacao = 0;
	
	
	public Informacoes(int contExecucoes, int validacao) {
		super();
		this.setId(1);
		this.setContExecucoes(contExecucoes);
		this.setValidacao(validacao);
	}
	
	public int getContExecucoes() {
		return contExecucoes;
	}


	public void setContExecucoes(int contExecucoes) {
		this.contExecucoes = contExecucoes;
	}


	public int getValidacao() {
		return validacao;
	}


	public void setValidacao(int validacao) {
		this.validacao = validacao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
