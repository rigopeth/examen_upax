package com.rigoberto.examen_upax;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Random;

public class Actividad_Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button Boton;
    LatLng Coordenadas;
    EditText Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad__mapa);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Boton = findViewById(R.id.Boton_Mapa);
        Text = findViewById(R.id.Texto_Lat_Long);

        Lanzar_JSON(null);

        Boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Leer_Archivo_TXT();
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Menu_Principal(null);
        finish();
    }

        public void Leer_Archivo_TXT(){
            try {
                File tuarchivo = new File( "/sdcard/zips/Carga_inicial.txt");
                FileInputStream stream = new FileInputStream(tuarchivo);
                String jsonStr = null;
                try {
                    FileChannel fc = stream.getChannel();
                    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                    jsonStr = Charset.defaultCharset().decode(bb).toString();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    stream.close();
                }

                JSONObject jsonObj = new JSONObject(jsonStr);

                JSONObject data  = jsonObj.getJSONObject("CARGA_INICIAL");
                JSONArray ubi = data.getJSONArray("UBICACIONES");

                Random ran = new Random();
                int x = ran.nextInt(data.length());

                String s1 = ubi.getJSONObject(x).getString("FNLATITUD");
                String s2 = ubi.getJSONObject(x).getString("FNLONGITUD");
                String s3 = ubi.getJSONObject(x).getString("FCNOMBRE");
                Text.setText("Lat: "+s1+" Long: "+s2);
                Coordenadas = new LatLng(Double.parseDouble(s1),Double.parseDouble(s2));
                mMap.addMarker(new MarkerOptions().position(Coordenadas).title(s3));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(Coordenadas));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void Menu_Principal (View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void Lanzar_JSON (View view){
        Intent i = new Intent(this, JSON.class);
        startActivity(i);
    }

}
