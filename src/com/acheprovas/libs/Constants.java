package com.acheprovas.libs;

import org.json.JSONArray;

public class Constants {

	// Tempo padrão de duração do Toast
	public static final int TEMPO_TOAST = 10000;

	// Nomes dos atributos JSON
	public static final String JSON_ARRAY = "resultados";
	public static final String TAG_ID = "id";
	public static final String TAG_NOME = "nome";
	public static final String TAG_DESC = "descricao";
	public static final String TAG_LINK = "link";
	
	// Array JSON de resultados
	public JSONArray resultado = null; 
	
	//Constantes contendo os diretorios do sistema
	public static final String DIRETORIO_PROVAS = "/provas/";
}
