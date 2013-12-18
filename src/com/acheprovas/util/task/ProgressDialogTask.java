package com.acheprovas.util.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ProgressDialogTask extends AsyncTask<Integer, Integer, Integer>{

	private ProgressDialog pd;
	private Context context;
	
	public ProgressDialogTask(ProgressDialog pd, Context context){
		this.pd = pd;
		this.context = context;
	}

	/**
	 * Sobrescreve o método de pré execução da thread
	 */
	@Override
	protected void onPreExecute() {

		// Inicializa e exibe o ProgressDialog
		pd = new ProgressDialog(this.context);
		pd.setTitle("Processing...");
		pd.setMessage("Please wait.");
		pd.setCancelable(false);
		pd.show();

	}
	
	/**
	 * Sobrescreve o método de encerramento de execução da thread
	 * 
	 * @param result
	 */
	@Override
	protected void onPostExecute(Integer result) {

	}

	@Override
	protected void onCancelled() {
		if (pd != null) {
			pd.dismiss();
		}

	}
	
	@Override
	protected Integer doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		return null;
	};
}
