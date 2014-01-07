package com.acheprovas.util.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;

import com.acheprovas.libs.Constants;

public class DownloadTask extends AsyncTask<String, Integer, String>{

	//Artigo Referencia
	//http://stackoverflow.com/questions/3028306/download-a-file-with-android-and-showing-the-progress-in-a-progressdialog
	private Context context;
	
	public DownloadTask(Context context){
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... sUrl) {
		Log.d("com.acheprovas", "DoInBackground");
		// take CPU lock to prevent CPU from going off if the user 
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
             getClass().getName());
        wl.acquire();
        Log.d("com.acheprovas", "DoInBackground1");
        
        try {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            Log.d("com.acheprovas", "DoInBackground2");
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                Log.d("com.acheprovas", "DoInBackground3");

                // expect HTTP 200 OK, so we don't mistakenly save error report 
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                     return "Server returned HTTP " + connection.getResponseCode() 
                         + " " + connection.getResponseMessage();
                Log.d("com.acheprovas", "DoInBackground4");

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                Log.d("com.acheprovas", "DoInBackground5");

                // download the file
                input = connection.getInputStream();
                Log.d("com.acheprovas", "DoInBackground6");
                output = new FileOutputStream(Environment.getExternalStorageDirectory() + Constants.DIRETORIO_PROVAS);
                Log.d("com.acheprovas", "DoInBackground7");

                Log.d("com.acheprovas", "Inicializando bytes");
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                Log.d("com.acheprovas", "Antes do While");
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
						return null;
					}
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
                Log.d("com.acheprovas", "Depois do While");
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } 
                catch (IOException ignored) { }

                if (connection != null)
                    connection.disconnect();
            }
        } finally {
            wl.release();
        }
        return null;
		
	}

}
