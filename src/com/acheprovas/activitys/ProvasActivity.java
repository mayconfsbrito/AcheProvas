package com.acheprovas.activitys;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.acheprovas.R;
import com.acheprovas.activitys.adapters.ArrayAdapterProvas;
import com.acheprovas.libs.Constants;

@SuppressLint({ "NewApi", "ShowToast" })
public class ProvasActivity extends Activity {

	protected static ProgressDialog pd;
	protected Object mActionMode;
	protected ListView listView;
	ArrayList<String> list = new ArrayList<String>();
	public int selectedItem = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Define a view da activity
		this.setContentView(R.layout.provas_armazenadas);

		// Uicializa componentes
		this.initComponents();

		// Executa a busca da prova através de uma AsynkTask
		new LerProvasArmazenadas(this).execute();

	}

	/**
	 * Infla as açoes da action bar desta view
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Infla o menu da action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.provas_activity_actions, menu);

		// Configura a ActionBar
		ActionBar actionBar = getActionBar();
		// actionBar.setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Define as ações para cada item da ActionBar
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			// Finaliza a execução desta activity
			finish();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Inicializa e instancía os componentes gráficos da view
	 */
	protected void initComponents() {

		// Insere na Action Bar o ícone para retornar a activity anterior
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Inicializa e exibe o ProgressDialog
		pd = new ProgressDialog(ProvasActivity.this);
		pd.setTitle("Procurando Provas...");
		pd.setMessage("Favor aguardar.");
		pd.setCancelable(false);
		pd.show();

		// Inicializa a listView
		this.listView = (ListView) findViewById(R.id.listview);

		// Inicializa o evento de pressionar algum item da listView
		this.listView.setLongClickable(true);
		this.listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(null, "ListView Pressionado");

				// O action mode está ativado no momento?
				if (mActionMode != null) {
					// Cancela o evento
					return false;
				}
				selectedItem = position;

				mActionMode = ProvasActivity.this
						.startActionMode(mActionModeCallback);
				view.setSelected(true);
				return true;
			}
		});

	}

	/**
	 * Implementa o ActionMode para esta Activity
	 */
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {

			// Infla o recurso do menu a ser exibido
			MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate( R.menu.linha_selecionada, menu );
	        
			Log.d(null, "Menu Inflado");

			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			switch (item.getItemId()) {
			case R.id.action_remover:
				show();
				// Fecha a Action quando executada
				mode.finish();
				break;

			default:
				break;
			}

			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// Anula o ActionMode
			mActionMode = null;
			selectedItem = -1;

		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}
		
		private void show() {
		    Toast.makeText(ProvasActivity.this,
		        "show", Constants.TEMPO_TOAST).show();
		    Log.d(null, "show()");
		  }

	};

	/**
	 * Classe Interna que implementa a AsyncTask para listagem de provas
	 * armazenadas no diretório de provas padrão do dispositivo
	 * 
	 * @author maycon
	 */
	private class LerProvasArmazenadas extends AsyncTask<Void, Void, Integer> {

		private Context context;

		/**
		 * Construtor da subclasse
		 * 
		 * @param context
		 *            Contexto da activity que instancía esta subclasse
		 */
		public LerProvasArmazenadas(Context context) {
			this.context = context;
		}

		/**
		 * Método que executa a thread desta AsyncTask
		 */
		@Override
		protected Integer doInBackground(Void... params) {

			try {
				String path = Environment.getExternalStorageDirectory()
						+ Constants.DIRETORIO_PROVAS;
				File f = new File(path);
				File file[] = f.listFiles();
				for (int i = 0; i < file.length; i++) {
					list.add(file[i].getName());
				}

			} catch (NullPointerException e) {
				e.printStackTrace();

				return -1;
			}

			return 1;
		}

		/**
		 * Executa o preocedimento de listagem das provas ao final da execução
		 * da thread
		 */
		@Override
		protected void onPostExecute(Integer result) {

			// Imprime o resultado da busca nesta ListActivity
			listItens(list);

			// Remove o ProgressDialog caso ele esteja sendo exibido
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}

			if (result == -1) {
				Toast.makeText(this.context, R.string.noProvas,
						Toast.LENGTH_LONG).show();
			}

		}

	}

	/**
	 * Lista o resultado da busca na tela
	 */
	public void listItens(final ArrayList<String> list) {

		if (list != null) {

			// Liga a ListView ao seu ArrayAdapter
			ArrayAdapterProvas arrayAdapter = new ArrayAdapterProvas(this,
					R.layout.target_item_busca, list);
			listView.setAdapter(arrayAdapter);

			// Define o evento onClick para abrir cada componente da lista
			this.listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					try {
						String path = Environment.getExternalStorageDirectory()
								+ Constants.DIRETORIO_PROVAS
								+ list.get(position);

						File file = new File(path);
						Intent it = new Intent();
						it.setAction(android.content.Intent.ACTION_VIEW);
						it.setDataAndType(Uri.fromFile(file), "application/zip");
						startActivity(it);

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			});

		}

	}

}
