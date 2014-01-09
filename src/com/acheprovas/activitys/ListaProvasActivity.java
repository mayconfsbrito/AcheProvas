package com.acheprovas.activitys;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.acheprovas.R;
import com.acheprovas.libs.Constants;
import com.acheprovas.util.json.JSONParser;
import com.acheprovas.util.task.DownloadTask;

/**
 * Lista o resultado da busca efetuada pelo servidor
 * 
 * @author mayconfsbrito
 * @since 14/09/2013
 * 
 */
public class ListaProvasActivity extends ListActivity {

	protected static ProgressDialog pd;
	protected ProgressDialog mProgressDialog;
	protected static ArrayList<HashMap<String, String>> array = null;

	/**
	 * Implementa as ações a serem executadas assim que a activity é criada
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Define a view da activity
		this.setContentView(R.layout.lista_provas);

		// Inicializa e exibe o ProgressDialog
		pd = new ProgressDialog(ListaProvasActivity.this);
		pd.setTitle("Bucando...");
		pd.setMessage("Favor aguardar.");
		pd.setCancelable(false);
		pd.show();

		// Executa a busca da prova através de uma AsynkTask
		new ListaProvasTask().execute();

	}

	@Override
	protected void onPause() {
		super.onPause();
		pd.dismiss();
	}

	/**
	 * Método que invoca o Handler de busca
	 */
	public ArrayList<HashMap<String, String>> search() {

		// Instancia na string o termo da busca
		String strBusca = getIntent().getExtras().getString("strBusca");

		// Inicializa Variáveis
		URL url = null;
		URI uri = null;
		ArrayList<HashMap<String, String>> listProvas = null;

		// Monta a URL
		String strUrl = "http://acheprovas.com/api/api.php?termo=" + strBusca
				+ "&enviar=Buscar+prova";

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

					HashMap<String, String> map = new HashMap<String, String>();
					map.put(Constants.TAG_ID, obj.getString(Constants.TAG_ID));
					map.put(Constants.TAG_NOME,
							obj.getString(Constants.TAG_NOME));
					map.put(Constants.TAG_DESC,
							obj.getString(Constants.TAG_DESC));
					map.put(Constants.TAG_LINK,
							obj.getString(Constants.TAG_LINK));

					// Insere o novo HashMap instanciado no ArrayList de
					// HashMap
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

	/**
	 * Lista o resultado da busca na tela
	 */
	public void listItens(ArrayList<HashMap<String, String>> array) {

		if (array != null) {
			ListAdapter adapter = new SimpleAdapter(this, array,
					R.layout.lista_provas, new String[] { Constants.TAG_NOME,
							Constants.TAG_DESC }, new int[] { R.id.text1,
							R.id.text2 });
			setListAdapter(adapter);

			// Define o evento onClick de download para cada componente da lista
			// exibida (cada prova)
			getListView().setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					HashMap<String, String> map = (HashMap<String, String>) getListAdapter()
							.getItem(position);

					// String nome = listItem.getString(Constants.TAG_NOME);
					// Toast.makeText(getBaseContext(),
					// "Baixando a prova " + map.get(Constants.TAG_NOME),
					// Constants.TEMPO_TOAST).show();

					mProgressDialog = new ProgressDialog(
							ListaProvasActivity.this);
					mProgressDialog.setMessage("Baixando a prova");
					mProgressDialog.setIndeterminate(true);
					mProgressDialog
							.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					mProgressDialog.setCancelable(true);

					// execute this when the downloader must be fired
					final DownloadTask downloadTask = new DownloadTask(
							ListaProvasActivity.this, mProgressDialog);
					downloadTask
							.execute("http://www.education.gov.yk.ca/pdf/pdf-test.pdf");

					mProgressDialog
							.setOnCancelListener(new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {
									downloadTask.cancel(true);
								}
							});

				}

			});

		}

	}

	/**
	 * Classe Interna que implementa a AsyncTask para busca e listagem de provas
	 * 
	 * @author maycon
	 */
	private class ListaProvasTask extends AsyncTask<Void, Void, Void> {

		/**
		 * Executa a busca da prova e insere e retorna no array
		 */
		@Override
		protected Void doInBackground(Void... params) {

			// Busca na API online do ache provas o resultado do termo de busca
			array = search();

			return null;
		}

		/**
		 * Executa o preocedimento de listagem das provas ao final da execução
		 * da thread
		 */
		@Override
		protected void onPostExecute(Void result) {

			// Imprime o resultado da busca nesta ListActivity
			listItens(array);

			// Remove o ProgressDialog caso ele esteja sendo exibido
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}

		}

	}
}
