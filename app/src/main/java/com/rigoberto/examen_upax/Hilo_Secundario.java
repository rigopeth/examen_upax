package com.rigoberto.examen_upax;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class Hilo_Secundario extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int id=10;

        super.onCreate(savedInstanceState);
        RelativeLayout relativelayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams Parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);

        Button Boton = new Button(this);

        RelativeLayout.LayoutParams BParametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,200);
        BParametros.addRule(RelativeLayout.CENTER_IN_PARENT);

        Boton.setLayoutParams(BParametros);
        Boton.setText("Hilo_Secundario");
        Boton.setId(id);

        relativelayout.addView(Boton);

        setContentView(relativelayout,Parametros);

        Boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Hilo().execute();
            }
        });

    }


    private class Hilo extends AsyncTask<Void,Void,Boolean>
    {


        @Override
        protected void onPreExecute() {
            Toast toast = Toast.makeText(Hilo_Secundario.this, "Procesando....", Toast.LENGTH_LONG);
            toast.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                Thread.currentThread();
                Thread.sleep(10000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }return true;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            if (resultado==true){
            Toast toast = Toast.makeText(Hilo_Secundario.this, "El tiempo a Terminado", Toast.LENGTH_SHORT);
            toast.show();}
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Menu_Principal(null);
        finish();
    }



    public void Menu_Principal (View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}
