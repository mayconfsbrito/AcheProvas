package com.acheprovas.model;

import java.util.HashMap;
import com.acheprovas.libs.Constants;

/**
 * 
 * @author mayconfsbrito
 *
 */

public class Prova {

	private Integer id;
	private String nome;
	private String descricao;
	private String link;
	
	public Prova(int id, String nome, String descricao, String link){
		super();
		this.setId(id);
		this.setNome(nome);
		this.setDescricao(descricao);
		this.setLink(link);
	}
	
	public Prova(HashMap<String, String> map){
		
		this.id = Integer.parseInt(map.get(Constants.TAG_ID));
		this.nome = map.get(Constants.TAG_NOME);
		this.descricao = map.get(Constants.TAG_DESC);
		this.link = map.get(Constants.TAG_LINK);
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
}
