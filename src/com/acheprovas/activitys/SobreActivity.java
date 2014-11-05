package com.acheprovas.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.acheprovas.R;
import com.google.analytics.tracking.android.EasyTracker;

/**
 * Activity com informações "sobre" o aplicativo
 * 
 * @author mayconfsbrito
 * 
 */
public class SobreActivity extends Activity {

	/**
	 * Inicializa a activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.sobre);

		// Inicializa o componente webview e o seu respectivo texto html a ser
		// exibido
		WebView webView = (WebView) findViewById(R.id.tvSobre);
		String texto = new String(
				"<html><body style='text-align:justify'>"
						+ "       Com início em 2012, o AcheProvas busca fornecer, de maneira rápida e prática, provas de vestibular e ENEM para download. "
						+ "Pouco a pouco, estamos percebendo uma boa aceitação de vocês, estudantes. "
						+ "Isto nos motiva a sempre continuar melhorando e criando novas funcionalidades."
						+ "<br /><br /><b>Idealização:</b>"
						+ "<br />Marcony Felipe e Robson Morais."
						+ "<br /><br /><b>Desenvolvimento:</b>"
						+ "<br />Maycon Brito e Flávia Viana"
						+ "<br /><a href='https://github.com/mayconfsbrito/'>https://github.com/mayconfsbrito/</a>"
						+ "<br /><br /><b>Maiores informações e contato:</b>"
						+ "<br /><a href='https://acheprovas.com/'>https://acheprovas.com/</a>"
						+ "<br /><br />Obrigador por utilizar!"
						+ "</body></html>");

		// Define as configurações (charset) do webview e exibe o texto na view
		WebSettings settings = webView.getSettings();
		settings.setDefaultTextEncodingName("utf-8");
		webView.loadDataWithBaseURL(null, texto, "text/html", "utf-8", null);
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
	 * Infla as actions da ActionBar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Infla o menu da action bar
		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.sobre_activity_actions, menu);

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
			finish();
			break;
			
		case R.id.action_search:
			// Finaliza a execução desta activity
			finish();
			break;
			
		}
		return super.onOptionsItemSelected(item);
	}
}
