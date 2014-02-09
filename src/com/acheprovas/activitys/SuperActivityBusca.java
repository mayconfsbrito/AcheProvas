package com.acheprovas.activitys;

import android.annotation.SuppressLint;
import android.app.ActionBar;
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

/**
 * Super classe abstrata para implementação de activity de busca
 * 
 * @author mayconfsbrito
 */
@SuppressLint("NewApi")
public abstract class SuperActivityBusca extends Activity {

	protected abstract void initComponents();

	/**
	 * Infla as açoes da action bar desta view
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Infla o menu da action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.busca_activity_actions, menu);

		// Remove o ícone da action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Define as ações para cada item da ActionBar
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_collection:
			// Chama a activity de provas armazenadas
			Intent it = new Intent(getBaseContext(), ProvasActivity.class);
			startActivityForResult(it, 0);
			

			return true;
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
