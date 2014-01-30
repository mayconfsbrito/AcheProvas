package com.acheprovas.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acheprovas.R;
import com.acheprovas.R.string;
import com.acheprovas.libs.Constants;

/**
 * Esta classe implementa a Activity da view "MainActivity", ou seja, a view de
 * introdução da aplicação.
 * 
 * @author mayconfsbrito
 * @since 14/09/2013
 */
public class BuscaActivity extends Activity {

	/**
	 * Variáveis da classe que representam componentes gráficos da view
	 */
	private EditText etBuscar;
	private Button btBuscar;
	private ProgressDialog pd;

	/**
	 * Implementa as ações a serem executadas assim que a activity é criada
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.busca_provas);
		this.initComponents();

	}
	
	/**
	 * Infla as açoes da action bar desta view
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.busca_activity_actions, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Inicializa e instancía os componentes gráficos da view
	 */
	protected void initComponents() {

		// Instancía os componentes gráficos
		this.setEtBuscar((EditText) findViewById(R.id.etBuscar));
		this.setBtBuscar((Button) findViewById(R.id.btBuscar));
		

		// Declara o evento OnClick do botão
		this.btBuscar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String strBusca = etBuscar.getText().toString();

				// Existe internet disponível?
				if (isNetworkAvailable()) {

					// O texto para busca foi preenchido?
					if (strBusca.length() > 0) {
						
						
						// Cria uma nova intent com a string inserida e envia para a activity de resultado de busca
						Intent it = new Intent(getBaseContext(),
						ListaProvasActivity.class);
						it.putExtra("strBusca", strBusca);
						
						
						startActivityForResult(it, 0);
						
						
						
					} 
				}

			}
		});
		
		//Declara o evento 
		etBuscar.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
				//O EditText foi preenchido com pelo menos um caracter?
				if(etBuscar.getText().length() > 0){
					
					//Desbloqueia o botão de submissão
					btBuscar.setEnabled(true);
					
					
				} // O EditText está vazio
				else{
					// Bloqueia o botão de submissão
					btBuscar.setEnabled(false);
					
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		

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

	/**
	 * Métodos Getters e Setters da Classe
	 */
	public EditText getEtBuscar() {
		return etBuscar;
	}

	public void setEtBuscar(EditText etBuscar) {
		this.etBuscar = etBuscar;
	}

	public Button getBtBuscar() {
		return btBuscar;
	}

	public void setBtBuscar(Button btBuscar) {
		this.btBuscar = btBuscar;
	}

}