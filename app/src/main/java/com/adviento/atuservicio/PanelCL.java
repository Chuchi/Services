package com.adviento.atuservicio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PanelCL extends AppCompatActivity {

    SharedPreferences tonadas;
    Handler Probala = new Handler();
    Handler Cambiador = new Handler();
    Runnable CambiaTitulo = new Runnable() {
        public void run() {

            setTitle( tonadas.getString(getResources().getString(R.string.PropiedadIdCliente), ""));

        }
    };
    Runnable Masahorita = new Runnable() {
        public void run() {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tonadas = PreferenceManager.getDefaultSharedPreferences(this);
        if (tonadas.getString(getResources().getString(R.string.PropiedadIdCliente), "").equals("")) {

            //Verificamos que la aplicacion ha sido inicializada con el registro de cliente
            Intent Paris = new Intent("android.intent.action.Iniciando");
            startActivity(Paris);

        } else {

            setContentView(R.layout.activity_panel_cl);
            Cambiador.postDelayed(CambiaTitulo,2500);
            //Instanciamos la barra de herramientas
            Toolbar TB10 = (Toolbar) findViewById(R.id.TB10);
            setSupportActionBar(TB10);
            //Instanciamos la boton flotante
            FloatingActionButton FLT10 = (FloatingActionButton) findViewById(R.id.FLT10);
            FLT10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent Jonas = new Intent("android.intent.action.Servicia");
                    startActivity(Jonas);
                }
            });
        }
    }
            @Override public boolean onCreateOptionsMenu (Menu menu){
                // Infla los componentes en el menu
                getMenuInflater().inflate(R.menu.botonera, menu);
                return true;
            }

            @Override public boolean onOptionsItemSelected (MenuItem item){

                // Pone funcionalidad al menu
                int id = item.getItemId();

                if (id == R.id.Creador) {

                }
                if (id == R.id.Modificar) {

                }

                return super.onOptionsItemSelected(item);
            }


        }

