package com.acheprovas.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.acheprovas.R;
import com.acheprovas.persistencia.dao.AbstractDAO;

/**
 * Esta classe implementa a Activity da view "MainActivity", ou seja, a view de
 * introdu��o da aplica��o.
 * 
 * @author mayconfsbrito
 * @since 14/09/2013
 */
public class BuscaActivity extends SuperActivityBusca {

	/**
	 * Vari�veis da classe que representam componentes gr�ficos da view
	 */
	private EditText etBuscar;
	private ImageButton ibBuscar;
	private ProgressDialog pd;
	private static int contExecucoes = 0;

	/**
	 * Implementa as a��es a serem executadas assim que a activity � criada
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.busca_provas);
		this.initComponents();

	}

	/**
	 * Inicializa e instanc�a os componentes gr�ficos da view
	 */
	protected void initComponents() {

		// Instanc�a os componentes gr�ficos
		this.setEtBuscar((EditText) findViewById(R.id.etBuscar));
		this.setIbBuscar((ImageButton) findViewById(R.id.ibBuscar));

		// Declara o evento OnClick do bot�o
		this.ibBuscar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String strBusca = etBuscar.getText().toString();

				// Existe internet dispon�vel?
				if (isNetworkAvailable()) {

					// O texto para busca foi preenchido?
					if (strBusca.length() > 0) {

						// Cria uma nova intent com a string inserida e envia
						// para a activity de resultado de busca
						Intent it = new Intent(getBaseContext(),
								ListaBuscaActivity.class);
						it.putExtra("strBusca", strBusca);
						startActivityForResult(it, 0);

					}
				}

			}
		});

		// Declara o evento
		etBuscar.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

				// O EditText foi preenchido com pelo menos um caracter?
				if (etBuscar.getText().length() > 0) {

					// Desbloqueia o bot�o de submiss�o
					ibBuscar.setEnabled(true);
					ibBuscar.setBackgroundResource(R.color.vermelho);

				} // O EditText est� vazio
				else {
					// Bloqueia o bot�o de submiss�o
					ibBuscar.setEnabled(false);
					ibBuscar.setBackgroundResource(R.color.cinza_escuro);

				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * M�todo executado quando o bot�o para voltar for pressionado
	 */
	@Override
	public void onBackPressed() {

		// Inicializa vari�veis
		int id = 0;
		
		int validacao = 0;

		// Consulta as informa��es de valida��o da app
		Cursor cursor = this.consultaValidacao();
		if (cursor != null && !cursor.isAfterLast()) {
			id = Integer.parseInt(cursor.getString(0));
			contExecucoes = Integer.parseInt(cursor.getString(1));
			validacao = Integer.parseInt(cursor.getString(2));

		}
		
		// O usu�rio ainda n�o avaliou a app? A contagem de execucoes � divis�vel por 5?
		if (validacao == 0 && (contExecucoes % 3 == 0)) {

			// Exibe um AlertDialog solicitando ao usuario para avaliar a app
			new AlertDialog.Builder(this)
					.setTitle("Vai n�o... � cedo uai!")
					.setMessage(
							"Antes de partir, deixe sua avalia��o para nosso aplicativo!\n\n� 1 minutinho!\n\n;)")
					.setCancelable(true)
					.setNegativeButton("Quero avaliar!",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									// Inicializa a activity para avalia��o
									startActivity(new Intent(
											Intent.ACTION_VIEW,
											Uri.parse("http://acheprovas.com/avalie")));

									// Insere no bd a informa��o de que o
									// aplicativo foi validado
									AbstractDAO dao = new AbstractDAO(
											BuscaActivity.this);
									ContentValues cv = new ContentValues();
									cv.put("validacao", 1);
									dao.alterar("informacoes",
											cv, "id=1", null);

								}
							})
					.setPositiveButton("Lembre-me mais tarde.",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									//Conta a execu��o
									contaExecucao();
									
									// Finaliza a activity
									finish();
								}
							}).show();
		} // Contabiliza a execu��o do aplicativo 
		else {
			
			//Conta a execu��o
			contaExecucao();
			
			// Finaliza a activity
			finish();
		}

	}
	
	/**
	 * Consulta o bd para saber as informa��es de execu��o da apk
	 * @return
	 */
	public Cursor consultaValidacao(){
		AbstractDAO dao = new AbstractDAO(BuscaActivity.this);
		Cursor cursor = dao.consultar("informacoes", null, "id=1", null);
		
		return cursor;
	}
	
	/**
	 * Armazena no bd mais uma execu��o da apk
	 */
	public void contaExecucao(){
		// Insere no bd a informa��o de que o
		// aplicativo foi executado
		AbstractDAO dao = new AbstractDAO(
				BuscaActivity.this);
		ContentValues cv = new ContentValues();
		cv.put("contExecucoes", ++contExecucoes);
		dao.alterar("informacoes",
				cv, "id=1", null);
	}

	/**
	 * M�todos Getters e Setters da Classe
	 */
	public EditText getEtBuscar() {
		return etBuscar;
	}

	public void setEtBuscar(EditText etBuscar) {
		this.etBuscar = etBuscar;
	}

	public ImageButton getIbBuscar() {
		return ibBuscar;
	}

	public void setIbBuscar(ImageButton btBuscar) {
		this.ibBuscar = btBuscar;
	}

}