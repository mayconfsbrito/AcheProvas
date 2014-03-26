package com.acheprovas.persistencia.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.acheprovas.persist.DBAdapter;

/**
 * 
 * @author mayconfsbrito
 *
 */
public class AbstractDAO extends DBAdapter {

	public AbstractDAO(Context context) {
		super(context);

	}

	/**
	 * Cadastra um determinado objeto em uma tabela do bd
	 * @param nomeTabela
	 * @param values
	 * @return
	 */
	public long cadastrar(String nomeTabela, ContentValues values) {

		long id = 0;

		try {

			open();
			id = getDatabase().insert(nomeTabela, null, values);
			close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return id;
	}

	/**
	 * Altera um determinado objeto de uma tabela do bd
	 * @param obj
	 * @return
	 */
	public boolean alterar(String nomeTabela, ContentValues values, String whereClause, String[] whereArgs){

		try{

			open();
			getDatabase().update(nomeTabela, values, whereClause, whereArgs);
			close();

			return true;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	/**
	 * Exclui um determinado objeto de uma tabela do bd
	 * @param nomeTabela
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public boolean excluir(String nomeTabela, String whereClause, String[] whereArgs){

		try {

			open();
			getDatabase().delete(nomeTabela, whereClause, whereArgs);
			close();

			return true;

		} catch(Exception ex){
			ex.printStackTrace();

		}

		return false;
	}

	/**
	 * Lista todos os objetos presentes em uma tabela do bd
	 */
	public Cursor listar(String nomeTabela, String select, String[] selectionArgs) {

		try {

			open();
			Cursor cursor = getDatabase().rawQuery("select " + select + " from " + nomeTabela, selectionArgs);
			cursor.moveToFirst();
			close();

			return cursor;

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Consulta os objetos de uma determinada tabela no bd
	 */
	public Cursor consultar(boolean distinct, String nomeTabela, String[] colunas, String selecao, String[] selecaoArgs, 
			String groupBy, String having, String orderBy, String limit){

		try {

			open();
			Cursor cursor = getDatabase().query(distinct, nomeTabela, colunas, selecao, selecaoArgs, groupBy, having, orderBy, limit);
			cursor.moveToFirst();
			close();

			return cursor;

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
	public Cursor consultar(String nomeTabela, String[] colunas, String selecao, String[] selecaoArgs, 
			String groupBy, String having, String orderBy){
		return consultar(false, nomeTabela, colunas, selecao, selecaoArgs, groupBy, having, orderBy, null);
	}
	public Cursor consultar(String nomeTabela, String[] colunas, String selecao, String[] selecaoArgs){
		return consultar(nomeTabela, colunas, selecao, selecaoArgs, null, null, null);
	}
	public Cursor consultar(String sql, String[] selectionArgs){
		
		try {

			open();
			Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
			cursor.moveToFirst();
			close();

			return cursor;

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Executa no bd um determinado código SQL
	 * @param sql
	 * @return
	 */
	public boolean executarSQL(String sql){

		try {

			open();
			getDatabase().execSQL(sql);
			close();

			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;

	}
}
