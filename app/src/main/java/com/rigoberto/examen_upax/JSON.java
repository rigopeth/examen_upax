package com.rigoberto.examen_upax;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import androidx.core.app.ActivityCompat;

public class JSON extends Activity {

    String acceso = "https://apisls.upaxdev.com/task/initial_load";
    String Link_zip;
    TextView TEXTO;
    String filename;
    private final static int Solicitud = 1;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TEXTO = findViewById(R.id.texto);
        Solicitar_Almacenamiento();
        new Hilo_Conexion().execute();
        //Descarga();

    }

    public void Solicitar_Almacenamiento(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Solicitud);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Solicitud: {

                if ( grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }

        }
    }


    public class Hilo_Conexion extends AsyncTask<Void,String,String> {
        public Hilo_Conexion() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TEXTO.setText("Conectando");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            TEXTO.setText("Conectado");
        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                StringBuilder stringBuilder = new StringBuilder();
                URL url = new URL(acceso);
                HttpURLConnection httpURLconnection = (HttpURLConnection) url.openConnection();
                httpURLconnection.setRequestMethod("POST");
                httpURLconnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                httpURLconnection.setDoOutput(true);

                DataOutputStream dataenvio = new DataOutputStream(httpURLconnection.getOutputStream());
                JSONObject jsonenvio = new JSONObject();
                jsonenvio.put("userId","89602");
                jsonenvio.put("env","dev");
                jsonenvio.put("os","android");

                dataenvio.writeBytes(jsonenvio.toString());
                dataenvio.flush();
                dataenvio.close();
                InputStream inputStream = httpURLconnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String JSON_String;
                while ((JSON_String = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_String);
                }
                JSONObject respuestaJSON = new JSONObject(stringBuilder.toString());


                bufferedReader.close();
                inputStream.close();
                httpURLconnection.disconnect();

                Link_zip = respuestaJSON.getString("message");

                return respuestaJSON.getString("message");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Iniciar_Descarga(s);
        }


        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void Iniciar_Descarga(String S) {
        String url = S;
        new Descargando_Archivo().execute(url);
    }

    class Descargando_Archivo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TEXTO.setText("Descargando");
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                File Directorio = new File("/sdcard/zips");
                Directorio.mkdirs();
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream("/sdcard/zips/Examen.zip");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {}
            return null;

        }
        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String unused) {
            TEXTO.setText("Descromprimiendo");
            Descomprimir_Zip("/sdcard/zips/Examen.zip");
            finish();
        }
    }

    public boolean Descomprimir_Zip(String filePath) {
        InputStream inputs;
        ZipInputStream zipzinputs;
        try {

            File zipfile = new File(filePath);
            String parentFolder = zipfile.getParentFile().getPath();

            inputs = new FileInputStream(filePath);
            zipzinputs = new ZipInputStream(new BufferedInputStream(inputs));
            ZipEntry zipe;
            byte[] buffer = new byte[1024];
            int count;

            while ((zipe = zipzinputs.getNextEntry()) != null) {

                if (zipe.isDirectory()) {
                    File fmd = new File(parentFolder + "/Carga_inicial.txt" );
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fileout = new FileOutputStream(parentFolder + "/Carga_inicial.txt");

                while ((count = zipzinputs.read(buffer)) != -1) {
                    fileout.write(buffer, 0, count);
                }

                fileout.close();
                zipzinputs.closeEntry();
            }

            zipzinputs.close();

            finish();

        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
