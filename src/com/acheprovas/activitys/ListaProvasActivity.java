package com.acheprovas.activitys;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.acheprovas.R;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * Lista o resultado da busca efetuada pelo servidor
 * 
 * @author Maycon Brito
 * @since 14/09/2013
 *
 */
public class ListaProvasActivity extends ListActivity{

	/**
	 * Implementa as ações a serem executadas assim que a activity é criada
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Define a view da activity
		this.setContentView(R.layout.lista_provas);
	}
	
	/**
	 * Realiza a busca on-line na API do servidor
	 */
	private void buscar(){
		
		//Captura na intent a descrição para busca
		String strBusca = getIntent().getExtras().getString("strBusca");
		
		/***********
		 * Testando o código http://www.androidsnippets.com/executing-a-http-post-request-with-httpclient
		 */
		
		//Cria uma requisição http cliente
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://http://acheprovas.com/api/api.php?termo=" + strBusca + "&enviar=Buscar+prova");
				
		
	}
}
