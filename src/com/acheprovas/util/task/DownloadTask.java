package com.acheprovas.util.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;

import com.acheprovas.R;
import com.acheprovas.libs.Constants;
import com.acheprovas.model.Prova;

/**
 * 
 * @author mayconfsbrito
 * 
 */

@SuppressLint({ "ShowToast", "Wakelock" })
public class DownloadTask extends AsyncTask<Prova, Integer, String> {

	/**
	 * Variáveis da classe
	 */
	private Context context;
	private String resultado;
	protected ProgressDialog mProgressDialog;
	private NotificationManager mNotifyManager;
	private Builder mBuilder;
	private ArrayList<Integer> listIdNotf = new ArrayList<Integer>();
	
	public DownloadTask(Context context, ProgressDialog mProgressDialog) {
		this.context = context;
		this.mProgressDialog = mProgressDialog;
	}

	/**
	 * Método de execução da thread. Realiza de forma independete o download de
	 * uma determinada prova.
	 * 
	 * Artigo Referência:
	 * http://stackoverflow.com/questions/3028306/download-a-file
	 * -with-android-and-showing-the-progress-in-a-progressdialog
	 */
	@SuppressWarnings("resource")
	@Override
	protected String doInBackground(Prova... prova) {

		// Previne que a CPU seja desligada caso o usuário pressione o botão de
		// desligar durante o download
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
		wl.acquire();

		// Inicializa variáveis de conexão
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		String resultado = "";
		File arquivo = null;

		try {

			// Inicializa a conexão para download da prova
			String urlProva = prova[0].getLink().replaceAll(" ", "%20");
			Log.d(null, "Baixando a prova: " + urlProva);
			URL url = new URL(urlProva);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.connect();

			// Aguarda um response request HTTP 200 OK, pois em caso positivo
			// reporta o erro
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
				return "Server returned HTTP " + connection.getResponseCode()
						+ " " + connection.getResponseMessage();

			// Exibe a porcentagem do download
			// Será -1 caso o servidor não retorne um tamanho para o arquivo
			int fileLength = connection.getContentLength();

			// Realiza o download do arquivo
			input = connection.getInputStream();
			String enderecoDiretorio = Environment
					.getExternalStorageDirectory() + Constants.DIRETORIO_PROVAS;
			File diretorio = new File(enderecoDiretorio);
			arquivo = new File(diretorio, prova[0].getNome() + ".zip");
			diretorio.mkdirs(); // Cria o diretório para salvar o arquivo
			output = new FileOutputStream(arquivo); // Endereço aonde o arquivo
													// da prova será gravado
			
			//Inicializa o notificador da barra superior fixa do android
			Integer id = new Random().nextInt();
			listIdNotf.add(id);
			mNotifyManager =
			        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			mBuilder = new NotificationCompat.Builder(context);
			mBuilder.setContentTitle("Baixando Prova")
			    .setContentText("Download em Progresso")
			    .setSmallIcon(R.drawable.ic_action_download);
			mNotifyManager.notify(id, mBuilder.build());

			// Grava o download em um arquivo byte a byte através de um buffer
			byte data[] = new byte[4096];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				// Permite cancelamento com o botão voltar
				if (isCancelled()) {
					
					//Deleta o arquivo gravado no disco
					arquivo.delete();
					
					return "cancelled";
				}
				total += count;
				// Publicando o progresso no ProgressBar
				if (fileLength > 0) { // Se o tamanho total do arquivo for
										// conhecido
					publishProgress((int) (total * 100 / fileLength));
					
					mBuilder.setProgress(fileLength, (int) (total * 100 / fileLength), true);
					// Issues the notification
					mNotifyManager.notify(id, mBuilder.build());
				}
				output.write(data, 0, count);
			}
			
			//Faz uma requisição ao servidor para informar a conclusão do download
			//para efeitos estatísticos do servidor
			String urlCount = "http://api.acheprovas.com/counter.php?id_prova=" + prova[0].getId() + "&origem=3";
			url = new URL(urlCount);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.connect();
			connection.getResponseCode();
			Log.d(null, urlCount);
			

		} catch (IOException e) {
			e.printStackTrace();

			//Deleta o arquivo gravado no disco
			arquivo.delete();
			return "Não foi possível gravar o arquivo no dispositivo!";

		} catch (Exception e) {
			e.printStackTrace();

			//Deleta o arquivo gravado no disco
			arquivo.delete();
			return "Erro não identificado!";

		} finally {
			try {
				// Fecha os Buffers de escrita e envio de informações através da
				// conexão
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (IOException ignored) {
			}

			// Fecha a conexão do download
			if (connection != null)
				connection.disconnect();

			wl.release();
		}

		return null;

	}

	/**
	 * Exibe o ProgressBar ao iniciar a execução da thread
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog.show();
	}

	/**
	 * Atualiza o progresso do download através no ProgressBar
	 */
	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);

		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgress(progress[0]);

	}

	/**
	 * Realiza a atualização da publicação ao encerrar a execução do download
	 */
	@Override
	protected void onPostExecute(String result) {
		mProgressDialog.dismiss();

		//O download foi abortado?
		if (result != null) {
			
			//Informa a abortagem do download
			notificacaoDownload(false, result);
			Toast.makeText(context, "Download error: " + result,
					Constants.TEMPO_TOAST).show();

			//O download foi concluído com sucesso
		} else {
			
			//Informa a conclusão do download
			notificacaoDownload(true, result);
			Toast.makeText(context, R.string.downConcl,
					Constants.TEMPO_TOAST).show();
		}
		
	}
	
	/**
	 * Caso o download seja cancelado executa o seguinte método sobrescrito
	 */
	@Override
	protected void onCancelled(String result) {
		super.onCancelled(result);
		
		//Notifica o cancelamento do download
		notificacaoDownload(false, result);
		Toast.makeText(context, R.string.downCancel,
				Constants.TEMPO_TOAST).show();
	}
	
	/**
	 * Informa o sucesso, cancelamento ou abortagem do download através de
	 * um toast e da notificação na barra superior
	 * 
	 * @param concluido determina se o aviso será sobre sucesso ou cancelamento/abortagem do download
	 * @param result contém o resultado do download
	 */
	private void notificacaoDownload(boolean concluido, String result){
		
		//O download foi concluído?
		if(concluido){
			mBuilder.setContentTitle("Downloa Concluído").setContentText("Download da prova concluído com sucesso").setProgress(0,0,false);
	        mNotifyManager.notify(listIdNotf.get(0), mBuilder.build());
			Toast.makeText(context, R.string.downConcl,
					Constants.TEMPO_TOAST).show();
			
			//O Download não foi concluído?
		} else {
			
			mBuilder.setContentTitle("Download Cancelado").setContentText("O download da prova foi cancelado").setProgress(0,0,false);
			mNotifyManager.notify(listIdNotf.get(0), mBuilder.build());
			
		}
	}

}
