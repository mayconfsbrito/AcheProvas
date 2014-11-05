package com.acheprovas.persist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Classe Respons�vel por criar o Banco de Dados Cria ou atualiza o Bd no
 * momento em que a aplica��es s�o instalada
 * 
 * @author mayconfsbrito
 */
public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, InterfaceScriptSQLite.NOME, null,
				InterfaceScriptSQLite.VERSAO);

	}

	/**
	 * Cria o banco de dados no momento em que a apk � instalada
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			
			Log.d(null, "Criando banco de dados...");
			db.execSQL(InterfaceScriptSQLite.SCRIPT_SQLITE_CREATE_INFORMACOES);
			Log.d(null, "Banco de dados criado com sucesso!");
			
			//Cria a tupla com propriedades padr�es da tabela CONFIGURACAO
			ContentValues values = new ContentValues();
			values.put("id", 1);
			values.put("contExecucoes", 0);
			values.put("validacao", 0);
			db.insert("informacoes", null, values);
			
			
		} catch (Exception ex){
			Log.d(null, "Erro ao criar o banco de dados!", ex.fillInStackTrace());
			System.exit(1);
		}
		
	}

	/**
	 * Atualiza o banco de dados para novas vers�es
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(null, "Atualizando banco de dados de " + oldVersion + " para " + newVersion + "...");
		db.execSQL(InterfaceScriptSQLite.SCRIPT_SQLITE_DROP_INFORMACOES);
		onCreate(db);
		Log.d(null, "Banco de dados atualizado com sucesso!");

	}

}
