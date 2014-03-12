package com.acheprovas.activitys;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.acheprovas.R;
import com.acheprovas.activitys.adapters.ArrayAdapterBusca;
import com.acheprovas.libs.Constants;
import com.acheprovas.model.Prova;
import com.acheprovas.util.json.JSONParser;
import com.acheprovas.util.task.DownloadTask;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Lista o resultado da busca efetuada pelo servidor
 * 
 * @author mayconfsbrito
 * @since 14/09/2013
 * 
 */
@SuppressLint("NewApi")
public class ListaProvasActivity extends SuperActivityBusca {

	protected static ProgressDialog pd;
	protected ProgressDialog mProgressDialog;
	protected static ArrayList<HashMap<String, String>> array = null;
	protected ListView listView;

	/**
	 * Implementa as ações a serem executadas assim que a activity é criada
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Define a view da activity e inicializa os componentes
		this.setContentView(R.layout.lista_provas);
		this.initComponents();
		
		AdView adView = (AdView)findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);

		// Executa a busca da prova através de uma AsynkTask
		new ListaProvasTask().execute();

	}

	/**
	 * Sobrescrita de método
	 */
	@Override
	protected void onPause() {
		super.onPause();

		// Despacha o progress bar
		pd.dismiss();
	}

	/**
	 * Inicializa e instancía os componentes gráficos da view
	 */
	protected void initComponents() {

		// Insere na Action Bar o ícone para retornar a activity anterior
		if (android.os.Build.VERSION.SDK_INT >= 11)
		    getActionBar().setDisplayHomeAsUpEnabled(true);

		// Inicializa e exibe o ProgressDialog
		pd = new ProgressDialog(ListaProvasActivity.this);
		pd.setTitle("Bucando...");
		pd.setMessage("Favor aguardar.");
		pd.setCancelable(false);
		pd.show();

		// Inicializa a listView
		this.listView = (ListView) findViewById(R.id.listview);

	}

	/**
	 * Invoca o Handler de busca
	 */
	public ArrayList<HashMap<String, String>> buscar() {

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

				// Inicializa o ArrayList que conterá as provas
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
			array = buscar();

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

	/**
	 * Lista o resultado da busca na tela
	 */
	public void listItens(final ArrayList<HashMap<String, String>> array) {

		if (array != null) {

			// Liga a ListView ao seu ArrayAdapter
			ArrayAdapterBusca arrayAdapter = new ArrayAdapterBusca(this,
					R.layout.target_item_busca, array);
			listView.setAdapter(arrayAdapter);

			// Define o evento onClick de download para cada componente da lista
			// exibida (cada prova)
			this.listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					HashMap<String, String> map = (HashMap<String, String>) array
							.get(position);

					Prova prova = new Prova(map);

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
					downloadTask.execute(prova);

					mProgressDialog
							.setOnCancelListener(new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {
									downloadTask.cancel(true);
								}
							});

				}

			});

		} else {
			Toast.makeText(this, R.string.buscaVazia,
					Constants.TEMPO_TOAST).show();
			finish();
		}

	}

}
