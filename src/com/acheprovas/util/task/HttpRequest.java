package com.acheprovas.util.task;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.acheprovas.libs.Constants;
import com.acheprovas.util.json.JSONParser;

public class HttpRequest extends
		AsyncTask<URL, Void, ArrayList<HashMap<String, String>>> {

	private String strBusca;

	public HttpRequest(String strBusca) {
		this.strBusca = strBusca;
	}

	/**
	 * Sobrescreve o método de execução em background da thread
	 */
	@Override
	protected ArrayList<HashMap<String, String>> doInBackground(URL... arg0) {

		// Inicializa Variáveis
		URL url = null;
		URI uri = null;
		ArrayList<HashMap<String, String>> listProvas = null;

		// Monta a URL
		String strUrl = "http://acheprovas.com/api/api.php?termo="
				+ this.strBusca + "&enviar=Buscar+prova";

		try {

			// Instancia o JSONParser
			JSONParser jParser = new JSONParser();

			// Monta a url a ser submetida
			url = new URL(strUrl);
			uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(),
					url.getPort(), url.getPath(), url.getQuery(), url.getRef());

			// Busca o objeto JSON apartir da url construída
			JSONObject json = jParser.getJSONFromUrl(uri.toString());

			// Foi encontrado algum resultado de objeto JSON?
			if (json != null) {

				// Inicializaa o ArrayList que conterá as provas
				listProvas = new ArrayList<HashMap<String, String>>();

				// Captura o array de nomes
				JSONArray jsonArray = json.getJSONArray(Constants.JSON_ARRAY);

				// Percorre o Array JSON
				for (int index = 0; index < jsonArray.length(); index++) {
					JSONObject obj = jsonArray.getJSONObject(index);

					// Armazena cada item json em um novo HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(Constants.TAG_ID, obj.getString(Constants.TAG_ID));
					map.put(Constants.TAG_NOME,
							obj.getString(Constants.TAG_NOME));
					map.put(Constants.TAG_DESC,
							obj.getString(Constants.TAG_DESC));
					map.put(Constants.TAG_LINK,
							obj.getString(Constants.TAG_LINK));

					// Insere o novo HashMap instanciado no ArrayList de HashMap
					// de provas
					listProvas.add(map);
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return listProvas;
	}

	

}
