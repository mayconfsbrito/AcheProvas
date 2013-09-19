package com.acheprovas.activitys;

import ufop.smd.controle.Constantes;
import ufop.smd.gui.activity.Opcoes;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.acheprovas.R;

/**
 * Esta classe implementa a Activity da view "MainActivity", ou seja, a view de
 * introdução da aplicação.
 * 
 * @author Maycon Brito
 * @since 14/09/2013
 */
public class MainActivity extends Activity {

	/**
	 * Variáveis da classe que representam componentes gráficos da view
	 */
	private EditText etBuscar;
	private Button btBuscar;

	/**
	 * Implementa as ações a serem executadas assim que a activity é criada
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_main);
		this.initComponents();
	}

	/**
	 * Inicializa e instancía os componentes gráficos da view
	 */
	protected void initComponents() {

		//Instancía os componentes gráficos
		this.setEtBuscar((EditText) findViewById(R.id.etBuscar));
		this.setBtBuscar((Button) findViewById(R.id.btBuscar));
		
		//Define os eventos de cada componente
		this.btBuscar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(this, ListaProvasActivity.class);
				startActivityForResult(it, 0);
				
			}
		});
		
		this.etBuscar.setText("Hellow!!!");
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