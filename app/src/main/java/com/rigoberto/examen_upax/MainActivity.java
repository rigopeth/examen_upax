package com.rigoberto.examen_upax;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button B1,B2,B3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        B1 = findViewById(R.id.button1);
        B2 = findViewById(R.id.button2);
        B3 = findViewById(R.id.button3);

        Botones();

    }

    private void Botones (){
        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ejercicio1(null);
                finish();
                }
            });

        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ejercicio2(null);
                finish();
                }
            });

        B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ejercicio3(null);
                finish();
                }
            });
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    public void Ejercicio1 (View view){
        Intent i = new Intent(this, Actividad_Mapa.class);
        startActivity(i);
    }

    public void Ejercicio2 (View view){
        Intent i = new Intent(this, Hilo_Secundario.class);
        startActivity(i);
    }

    public void Ejercicio3 (View view){
        Intent i = new Intent(this, Base_SQL.class);
        startActivity(i);
    }


}
