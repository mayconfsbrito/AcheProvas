package com.acheprovas.activitys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.acheprovas.R;
import com.acheprovas.libs.Constants;
import com.acheprovas.util.task.HttpRequest;

/**
 * Lista o resultado da busca efetuada pelo servidor
 * 
 * @author Maycon Brito
 * @since 14/09/2013
 * 
 */
public class ListaProvasActivity extends ListActivity {

	protected ProgressDialog pd;
	
	/**
	 * Implementa as ações a serem executadas assim que a activity é criada
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Define a view da activity
		this.setContentView(R.layout.lista_provas);
		
		// Busca na API online do ache provas o resultado do termo de busca
		ArrayList<HashMap<String, String>> array = this.search();

		// Imprime o resultado da busca nesta ListActivity
		this.listItens(array);
	}

	/**
	 * Método que invoca o Handler de busca
	 */
	public ArrayList<HashMap<String, String>> search() {

		// Inicializa variáveis
		ArrayList<HashMap<String, String>> array = null;

		// Instancia na string o termo da busca
		String strBusca = getIntent().getExtras().getString("strBusca");

		try {
			
			// Log.d("com.acheprovas", "Buscando Array=" + array.toString());
			array = new HttpRequest(strBusca, pd, this).execute().get();
			// Log.d("com.acheprovas", "ArrayList Retornado=" +
			// array.toString());

		} catch (InterruptedException e) {
			e.printStackTrace();

		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return array;
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


				}

			});

			// ListView lv = getListView();
		}

	}
}
