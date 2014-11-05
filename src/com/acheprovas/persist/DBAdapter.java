package com.acheprovas.persist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Classe que trabalha diretamente com o SQLite, executando querys, inserts,
 * updates e etc.
 * 
 * @author mayconfsrito
 * 
 */
public class DBAdapter {

	private static SQLiteDatabase database;
	private static DBHelper dbHelper;

	public DBAdapter(Context context) {
		dbHelper = new DBHelper(context);
	}

	public static synchronized SQLiteDatabase getDatabase(){
		return database;
	}

	public static synchronized DBHelper getHelper(){
		return dbHelper;
	}

	/**
	 * Abre o banco de dados
	 */
	 public void open() {
		 try {
			 if(getHelper() != null){
				 if(database==null ||(database != null && !database.isOpen())){
					 database = getHelper().getWritableDatabase();
				 }
			 }
		 } catch (Exception ex) {
			 Log.d(null, "Erro ao abrir o bd.", ex.fillInStackTrace());
		 }
	 }

	 /**
	  * Fecha o banco de dados
	  */
	 public void close() {
		 if(database != null && database.isOpen()){
			 database.close();
		 }
	 }

}
