package com.acheprovas.activitys;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.acheprovas.R;

@SuppressLint("NewApi")
public class ProvasActivity extends Activity {

	protected ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Define a view da activity
		this.setContentView(R.layout.provas_armazenadas);

		// Uicializa componentes
		this.initComponents();

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
		actionBar.setDisplayShowHomeEnabled(false);
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
			//Finaliza a execução desta activity
			finish();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Inicializa e instancía os componentes gráficos da view
	 */
	protected void initComponents() {

	}

}
