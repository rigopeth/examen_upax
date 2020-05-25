package com.rigoberto.examen_upax;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class Base_SQL extends Activity {
    private static final String TABLA_EMPLEADOS = "Empleados";
    private static final String TABLA_ID = "_id";
    private static final String TABLA_NOMBRE = "Nombre";
    private static final String TABLA_FECHA = "Fecha_Nacimiento";
    private static final String TABLA_PUESTO = "Puesto";
    private static final String CREAR_TABLA = "CREATE TABLE " +TABLA_EMPLEADOS+" ("+TABLA_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +TABLA_NOMBRE+" TEXT , "+TABLA_FECHA+ " Date , "+TABLA_PUESTO+ " TEXT )" ;
    String Nombres[] = {"Miguel Cervantes","Juan Morales","Roberto Méndez", "Miguel Cuevas"};
    String Fecha_Nacimiento[] = {"08-Dic-1990","03-Jul-1990","14-Oct-1990","08-Dic-1990"};
    String Puestos[] = {"Desarrollador","Desarrollador","Desarrollador","Desarrollador"};
    int N[]={R.id.Nombres1,R.id.Nombres2,R.id.Nombres3,R.id.Nombres4};
    int F[]={R.id.Fechas1,R.id.Fechas2,R.id.Fechas3,R.id.Fechas4};
    int P[]={R.id.Puestos1,R.id.Puestos2,R.id.Puestos3,R.id.Puestos4};
    int Posicion = 0;
    TextView Nom,Fec,Pue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_tabla);
        Guardar();
        Consultar();

    }

    private void Guardar(){
        SQL Conexion_db = new SQL(this,"upaxdb",null,1);
        SQLiteDatabase db = Conexion_db.getWritableDatabase();
        String Contador = "SELECT count(*) FROM " +TABLA_EMPLEADOS;
        Cursor Puntero = db.rawQuery(Contador,null);
        Puntero.moveToFirst();
        int iContador = Puntero.getInt(0);
        ContentValues Valores = new ContentValues();
        if (iContador>0) {
            //Toast.makeText(getApplicationContext(),"Ya Hay una base de datos ",Toast.LENGTH_LONG).show();
        }else{
            for (int i = 0; i <= 3; i++) {
                Valores.put(TABLA_NOMBRE, Nombres[i]);
                Valores.put(TABLA_FECHA, Fecha_Nacimiento[i]);
                Valores.put(TABLA_PUESTO, Puestos[i]);
                db.insert(TABLA_EMPLEADOS, TABLA_ID, Valores);
            }
            //Toast.makeText(getApplicationContext(),"db Registrada",Toast.LENGTH_LONG).show();
        }

        db.close();

    }

    private void Consultar(){
        SQL Conexion_db = new SQL(this,"upaxdb",null,1);
        SQLiteDatabase db = Conexion_db.getWritableDatabase();
        for (int i=1;i<=4;i++ ) {
            Cursor Puntero = db.rawQuery("SELECT " + TABLA_NOMBRE + " , " + TABLA_FECHA + " , " + TABLA_PUESTO + " FROM " + TABLA_EMPLEADOS + " WHERE " + TABLA_ID + "=" + i, null);
            Puntero.moveToFirst();
            Nom = findViewById(N[Posicion]);
            Fec = findViewById(F[Posicion]);
            Pue = findViewById(P[Posicion]);
            Nom.setText(Puntero.getString(0));
            Fec.setText(Puntero.getString(1));
            Pue.setText(Puntero.getString(2));
            Posicion++;
        }
            Posicion=0;
            db.close();
        }


        class SQL extends SQLiteOpenHelper{

            public SQL(Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {
                super(context, name, factory, version);
            }

            @Override
            public void onCreate(SQLiteDatabase upaxdb) {
                upaxdb.execSQL(CREAR_TABLA);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS "+TABLA_EMPLEADOS);
                onCreate(db);
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

