package com.adviento.atuservicio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Servicia extends AppCompatActivity implements View.OnClickListener {


    TextView TXV10, TXV11, TXV21;
    Button BTN10;
    EditText EDT10;
    ImageButton IMBTN10;
    ArrayList<String[]> Orden;
    MiAyudanteSQLite ayuquito;
    Typeface TFAmaranthRegular;
    Typeface TFAmaranthBold;
    Dialog MiDialogo = null;
    LocationManager locManager;
    LocationListener locListener;
    Double Lat = 0d;
    Double Long = 0d;
    AlertDialog pedido;
    int IdClient = 39;
    int Ciudadela =0;
    int Serviciax=0;
    static JSONObject json;



    //  int Localidad=0;
    int ServicioElegido = 0;

    Boolean inter = false;
    int punteos = 0;
    Handler Chispea = new Handler();
    Runnable caifas = new Runnable() {
        public void run() {
            Flasheo();
            if (inter) {
                IMBTN10.setAlpha(1f);
            } else {
                IMBTN10.setAlpha(0.3f);
            }
            Chispea.postDelayed(caifas, 1200);
        }
    };
    Runnable anas = new Runnable() {
        public void run() {
            Flasheo();
            if (inter) {
                TXV11.setAlpha(1f);
            } else {
                TXV11.setAlpha(0.3f);
            }
            Chispea.postDelayed(anas, 600);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicia);

        ayuquito = new MiAyudanteSQLite(this);

        IMBTN10 = (ImageButton) findViewById(R.id.IMBTN10);
        TXV10 = (TextView) findViewById(R.id.TXV10);
        TXV11 = (TextView) findViewById(R.id.TXV11);
        TXV21 = (TextView) findViewById(R.id.TXV21);
        EDT10 = (EditText) findViewById(R.id.EDT10);
        BTN10 = (Button) findViewById(R.id.BTN10);

        IMBTN10.setOnClickListener(this);


        BTN10.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                json = new JSONObject();

                try {
                    json.put("Cliente", IdClient);
                    json.put("Servicio", Serviciax);
                    json.put("Latitud", Lat);
                    json.put("Longuitud", Long);
                    json.put("Localidad", Ciudadela);
                    json.put("Observaciones", EDT10.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TareaWSInsertar tarea = new TareaWSInsertar();
                tarea.execute(json.toString());
            }
        });


        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertNoGps();
        }

        locListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                Lat = location.getLatitude();
                Long = location.getLongitude();
                TXV21.setText(String.valueOf(Lat) + " , " + String.valueOf(Long));


            }


            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2, locListener);


        String Amaranth_Regular = "Vermut/Amaranth-Regular.ttf";
        String Amaranth_Bold = "Vermut/Amaranth-Bold.ttf";

        TFAmaranthRegular = Typeface.createFromAsset(getAssets(), Amaranth_Regular);
        TFAmaranthBold = Typeface.createFromAsset(getAssets(), Amaranth_Bold);
        TXV10.setTypeface(TFAmaranthBold);

        Chispea.postDelayed(caifas, 500);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.IMBTN10:
                // con este tema personalizado evitamos los bordes por defecto
                MiDialogo = new Dialog(this, R.style.Theme_Dialog_Translucent);
                //deshabilitamos el título por defecto
                MiDialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //establecemos el contenido de nuestro dialog
                MiDialogo.setContentView(R.layout.dialog);

                final GridView GRD10 = (GridView) MiDialogo.findViewById(R.id.DIAGRD10);
                GRD10.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        Serviciax= Integer.parseInt(Orden.get(position)[0]);
                        ayuquito.AbrirBase();

                        TXV10.setVisibility(View.VISIBLE);
                        TXV10.setText(Orden.get(position)[1]);


                        TXV10.setOnClickListener(Servicia.this);
                        String icono = Orden.get(position)[2];

                        ServicioElegido = Integer.valueOf(Orden.get(position)[0]);

                        IMBTN10.setImageResource(Servicia.this.getResources().getIdentifier(icono, "drawable", Servicia.this.getPackageName()));
                        Chispea.removeCallbacks(caifas);
                        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            TXV11.setVisibility(View.VISIBLE);
                            Chispea.postDelayed(anas, 500);
                            TXV11.setOnClickListener(Servicia.this);
                        } else {
                            EDT10.setVisibility(View.VISIBLE);

                        }

                        MiDialogo.dismiss();
                    }
                });

                PoblarGrid(GRD10);
                MiDialogo.show();
                break;


            case R.id.TXV11: // ImageButton ORIGEN
                Chispea.removeCallbacks(caifas);
                final Dialog dialog = new Dialog(this, R.style.Theme_Dialog_Translucent);
                punteos = 0;
                //    Localidad = 0;
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.forma_dialogo);
                final ListView LV10 = (ListView) dialog.findViewById(R.id.LV10);
                final TextView TXV99 = (TextView) dialog.findViewById(R.id.TXV99);
                ayuquito.AbrirBase();
                SimpleCursorAdapter paco = new SimpleCursorAdapter(this, R.layout.venturi_doble, ayuquito.PueblaDepartamentos(), new String[]{"_id", "Departamento"}, new int[]{0, R.id.venturi22});


                //Carlo.CerrarBase();
                LV10.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                        // Direcciona en el primer punteo

                        if (punteos > 0) {
                            Ciudadela=(int)id;
                            String casino = String.valueOf(id);
                            //    Localidad = id;
                            ayuquito.AbrirBase();
                            TXV11.setVisibility(View.VISIBLE);
                            TXV11.setText(ayuquito.BuscaCiudad(casino));
                            TXV11.setTypeface(TFAmaranthBold);
                            TXV11.setTextColor(getResources().getColor(R.color.Fucsia100));
                            ayuquito.CerrarBase();
                            Chispea.removeCallbacks(anas);
                            EDT10.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }
                        if (punteos == 0) {
                            String paulov = String.valueOf(id);
                            ayuquito.AbrirBase();
                            SimpleCursorAdapter pepe = new SimpleCursorAdapter(view.getContext(), R.layout.venturi_doble, ayuquito.PueblaCiudadesDeDepartamento(paulov), new String[]{"_id", "Ciudad"}, new int[]{0, R.id.venturi22});
                            LV10.setAdapter(pepe);
                            TXV99.setText("Elija Ciudad");
                            punteos++;
                            ayuquito.CerrarBase();

                            //5555
                        }
                    }
                });

                LV10.setAdapter(paco);
                // dialog.setTitle("Elija el Departamento");
                dialog.show();
                ayuquito.CerrarBase();
                break;
        }

    }

    /*  public void mostrar(View view)
      {
          // con este tema personalizado evitamos los bordes por defecto
          customDialog = new Dialog(this,R.style.Theme_Dialog_Translucent);
          //deshabilitamos el título por defecto
          customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
          //obligamos al usuario a pulsar los botones para cerrarlo
          customDialog.setCancelable(false);
          //establecemos el contenido de nuestro dialog
          customDialog.setContentView(R.layout.dialog);

          GridView GRD10 = (GridView) customDialog.findViewById(R.id.DIAGRD10);
          PoblarGrid();
             ((Button) customDialog.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {

              @Override
              public void onClick(View view)
              {
                  customDialog.dismiss();
                  Toast.makeText(Servicia.this, "ACCEPT", Toast.LENGTH_SHORT).show();

              }
          });

          customDialog.show();
      }
  */
    public void PoblarGrid(GridView grid) {
        Orden = new ArrayList<String[]>();
        ayuquito.AbrirBase();
        Cursor pepone = ayuquito.PueblaServicios();
        if (pepone.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String[] Canton = new String[3];
                Canton[0] = pepone.getString(0);
                Canton[1] = pepone.getString(1);
                Canton[2] = pepone.getString(2);
                Orden.add(Canton);
            } while (pepone.moveToNext());
        }

        MiAdaptadorCarrito cartucho = new MiAdaptadorCarrito(this, Orden);
        grid.setAdapter(cartucho);

    }

    public void Flasheo() {

        if (inter) {
            inter = false;
        } else {
            inter = true;
        }
    }

    private void AlertNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El sistema GPS esta desactivado y es imperativa su activacion para proseguir con el siguiente paso. ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        pedido = builder.create();
        pedido.show();
    }

    private class TareaWSInsertar extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {

            String resul="";

// Obtener la conexión


            HttpURLConnection MiConexion = null;

            try {
                // Construir los datos a enviar
                URL url = new URL("http://sareta.somee.com/caminatodo");
                String Datos_cuerpo =params[0];
                MiConexion = (HttpURLConnection) url.openConnection();
                MiConexion.setRequestMethod("POST");
                MiConexion.setRequestProperty("Content-Type", "application/json");
                MiConexion.setRequestProperty("Content-Length",  Integer.toString(Datos_cuerpo.getBytes().length));
                //  connection.setRequestProperty("Content-Language", "en-US");

                //  connection.setUseCaches (false);
                MiConexion.setDoInput(true);
                MiConexion.setDoOutput(true);

                OutputStream out = new BufferedOutputStream(MiConexion.getOutputStream());

                out.write(Datos_cuerpo.getBytes());
                out.flush();
                out.close();

                resul=LectorRespuestas(MiConexion);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (MiConexion != null)
                    MiConexion.disconnect();
            }

            return resul;
        }

        protected void onPostExecute(String  result) {
            if (result.equals("1")){
                Toast.makeText(Servicia.this, "Peticion de Servicio ha sido activada", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(Servicia.this, "Se ha reportado un problema. Verifique su conexion a internet e intentelo de nuevo mas tarde", Toast.LENGTH_LONG).show();
            }
    }
    }

    private String LectorRespuestas(HttpURLConnection connection) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        } catch (Exception e) {
           // Log.i(TAG, "Error reading InputStream");
            result = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                   // Log.i(TAG, "Error closing InputStream");
                }
            }
        }

        return result;
    }

}