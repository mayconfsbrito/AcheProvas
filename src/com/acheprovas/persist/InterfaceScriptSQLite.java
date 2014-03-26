/**
 * 
 */
package com.acheprovas.persist;

/**
 * @author MAYCON
 * 
 */
public interface InterfaceScriptSQLite {

	public static final String NOME = "com.acheprovas_db";
	public static final Integer VERSAO = 2;

	public static final String SCRIPT_SQLITE_CREATE_INFORMACOES = "CREATE TABLE `informacoes` (`id` INTEGET NOT NULL, `contExecucoes` INTEGER NOT NULL, `validacao` INTEGER NOT NULL);";
	public static final String SCRIPT_SQLITE_DROP_INFORMACOES = "DROP TABLE IF EXISTS informacoes;";

}