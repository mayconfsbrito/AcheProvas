package com.acheprovas.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.acheprovas.R;
import com.acheprovas.R.string;
import com.acheprovas.libs.Constants;
import com.google.analytics.tracking.android.EasyTracker;

/**
 * Super classe abstrata para implementação de activity de busca
 * 
 * @author mayconfsbrito
 */
@SuppressLint("NewApi")
public abstract class SuperActivityBusca extends Activity {

	protected abstract void initComponents();

	/**
	 * Infla as actions da ActionBar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Infla o menu da action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.busca_activity_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Sobrescreve o método onStart para inserir o GoogleAnalytics através do EasyTracker 
	 */
	@Override
	  public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	  }
	
	/**
	 * Sobrescreve o método onStop para inserir o GoogleAnalytics através do EasyTracker
	 */
	@Override
	  public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	}

	/**
	 * Define as ações para cada item da ActionBar
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Verifica o menu item selecionado
		switch (item.getItemId()) {

		// Caso foi selecionado 'Buscar'
		case R.id.action_collection:
			// Chama a activity de provas armazenadas
			Intent it = new Intent(getBaseContext(), ProvasActivity.class);
			startActivityForResult(it, 0);
			break;

		// Caso foi selecionado 'Sobre'
		case R.id.action_sobre:
			Intent it1 = new Intent(getBaseContext(), SobreActivity.class);
			startActivityForResult(it1, 0);
			break;

		// Caso foi pressionado para Retornar
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Verifica se o dispositivo está com internet disponível
	 * 
	 * @return true se existir conectividade ou false em caso negativo
	 */
	public boolean isNetworkAvailable() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}

		// Exibe uma mensagem ao usuário
		Toast.makeText(getBaseContext(), string.noInternet,
				Constants.TEMPO_TOAST).show();

		return false;
	}
}
