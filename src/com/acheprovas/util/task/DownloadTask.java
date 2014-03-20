package com.acheprovas.util.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
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
	 * Vari�veis da classe
	 */
	private Context context;
	protected ProgressDialog mProgressDialog;
	protected AlertDialog alertDialog;
	private NotificationManager mNotifyManager;
	private Builder mBuilder;
	private ArrayList<Integer> listIdNotf = new ArrayList<Integer>();
	private Boolean isCancelled = false; // Substitui o m�todo isCancelled desta
											// classe devido customiza��o

	public DownloadTask(Context context, ProgressDialog mProgressDialog) {
		this.context = context;
		this.mProgressDialog = mProgressDialog;
	}

	/**
	 * M�todo de execu��o da thread. Realiza de forma independete o download de
	 * uma determinada prova.
	 * 
	 * Artigo Refer�ncia:
	 * http://stackoverflow.com/questions/3028306/download-a-file
	 * -with-android-and-showing-the-progress-in-a-progressdialog
	 */
	@SuppressWarnings("resource")
	@Override
	protected String doInBackground(Prova... prova) {

		// Previne que a CPU seja desligada caso o usu�rio pressione o bot�o de
		// desligar durante o download
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
		wl.acquire();

		// Inicializa vari�veis de conex�o
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		File arquivo = null;

		try {

			// Inicializa a conex�o para download da prova
			String urlProva = prova[0].getLink().replaceAll(" ", "%20");
			urlProva = Uri.parse(urlProva).toString();
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
			// Ser� -1 caso o servidor n�o retorne um tamanho para o arquivo
			int fileLength = connection.getContentLength();

			// Realiza o download do arquivo
			input = connection.getInputStream();
			String enderecoDiretorio = Environment
					.getExternalStorageDirectory() + Constants.DIRETORIO_PROVAS;
			File diretorio = new File(enderecoDiretorio);
			arquivo = new File(diretorio, prova[0].getNome() + ".zip");
			diretorio.mkdirs(); // Cria o diret�rio para salvar o arquivo
			output = new FileOutputStream(arquivo); // Endere�o aonde o arquivo
													// da prova ser� gravado

			// Inicializa o notificador da barra superior fixa do android
			Integer id = new Random().nextInt();
			listIdNotf.add(id);
			mNotifyManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			mBuilder = new NotificationCompat.Builder(context);
			mBuilder.setContentTitle("Baixando Prova")
					.setContentText("Download em Progresso")
					.setSmallIcon(R.drawable.ic_action_download);
			mNotifyManager.notify(id, mBuilder.build());

			// Grava o download em um arquivo byte a byte atrav�s de um buffer
			byte data[] = new byte[4096];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {

				// A Task foi cancelada?
				if (this.isCancelled) {
					// Deleta o arquivo gravado no disco
					arquivo.delete();

					// Retorna que a mesma foi cancelada
					return "cancelled";
				}

				// Publicando o progresso no ProgressBar
				total += count;
				if (fileLength > 0) { // Se o tamanho total do arquivo for
										// conhecido

					Integer pro = (int) (total * 100 / fileLength);
					UIClass.publicar(this, pro);

					mBuilder.setProgress(fileLength, pro, true).setContentText(
							pro.toString() + "%");
					// Issues the notification
					mNotifyManager.notify(id, mBuilder.build());
				}
				output.write(data, 0, count);
			}

			// Faz uma requisi��o ao servidor para informar a conclus�o do
			// download
			// para efeitos estat�sticos do servidor
			String urlCount = "http://api.acheprovas.com/counter.php?id_prova="
					+ prova[0].getId() + "&origem=3";
			url = new URL(urlCount);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.connect();
			connection.getResponseCode();

		} catch (IOException e) {
			e.printStackTrace();

			// Deleta o arquivo gravado no disco
			arquivo.delete();
			return "N�o foi poss�vel gravar o arquivo no dispositivo!";

		} catch (Exception e) {
			e.printStackTrace();

			// Deleta o arquivo gravado no disco
			arquivo.delete();
			return "Erro n�o identificado!";

		} finally {
			try {
				// Fecha os Buffers de escrita e envio de informa��es atrav�s da
				// conex�o
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (IOException ignored) {
			}

			// Fecha a conex�o do download
			if (connection != null)
				connection.disconnect();

			wl.release();
		}

		return null;

	}

	/**
	 * Exibe o ProgressBar ao iniciar a execu��o da thread
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog.show();
		mProgressDialog.setCanceledOnTouchOutside(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				exibirDialogCancel();

			}
		});

	}

	/**
	 * Exibe o dialog perguntando ao usu�rio se ele deseja cancelar o download
	 */
	private void exibirDialogCancel() {
		//
		alertDialog = new AlertDialog.Builder(context)
				.setTitle("Aten��o!")
				.setMessage("Deseja cancelar o download desta prova?")
				.setCancelable(true)
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								mProgressDialog.cancel();
								isCancelled = true;

							}
						})
				.setNegativeButton("N�o",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mProgressDialog.show();
							}
						}).show();

	}

	/**
	 * Atualiza o progresso do download atrav�s no ProgressBar
	 */
	@Override
	protected void onProgressUpdate(Integer... progress) {
		this.publicar(progress);
		super.onProgressUpdate(progress);

	}

	/**
	 * Realiza a atualiza��o da publica��o ao encerrar a execu��o do download
	 */
	@Override
	protected void onPostExecute(String result) {
		this.publicarFim(result);

	}

	/**
	 * Caso o download seja cancelado executa o seguinte m�todo sobrescrito
	 */
	@Override
	protected void onCancelled(String result) {
		super.onCancelled(result);

		this.publicarFim(result);
	}

	/**
	 * Informa o sucesso, cancelamento ou abortagem do download atrav�s de um
	 * toast e da notifica��o na barra superior
	 * 
	 * @param concluido
	 *            determina se o aviso ser� sobre sucesso ou
	 *            cancelamento/abortagem do download
	 * @param result
	 *            cont�m o resultado do download
	 */
	private void notificacaoDownload(String result) {

		if (mBuilder != null && mNotifyManager != null) {

			// O download foi conclu�do?
			if (!this.isCancelled) {
				mBuilder.setContentTitle("Download Conclu�do")
						.setContentText(
								"Download da prova conclu�do com sucesso")
						.setProgress(0, 0, false);
				mNotifyManager.notify(listIdNotf.get(0), mBuilder.build());
				Toast.makeText(context, R.string.downConcl,
						Constants.TEMPO_TOAST).show();

				// O Download n�o foi conclu�do?
			} else {

				mBuilder.setContentTitle("Download Cancelado")
						.setContentText("O download da prova foi cancelado")
						.setProgress(0, 0, false);
				mNotifyManager.notify(listIdNotf.get(0), mBuilder.build());
				Toast.makeText(context, R.string.downCancel,
						Constants.TEMPO_TOAST).show();

			}
		}

	}

	/**
	 * Publica o progresso do download
	 */
	public void publicar(Integer... progress) {
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgress(progress[0]);
	}

	/**
	 * Publica o final da execu��o desta task
	 */
	public void publicarFim(String result) {

		notificacaoDownload(result);
		mProgressDialog.hide();
		if (alertDialog != null) {
			alertDialog.dismiss();
		}

	}

}

/**
 * Classe interna para publicar progressos quando a task for cancelada
 * 
 * @author mayconfsbrito
 */
class UIClass {
	public static void publicar(DownloadTask task, Integer... progress) {
		task.publicar(progress);
	}
}
