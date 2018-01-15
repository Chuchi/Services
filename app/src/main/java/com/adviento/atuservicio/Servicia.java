package com.adviento.atuservicio;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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


    TextView TXV10, TXV101, TXV21, TXV40;
    Button BTN10;
    EditText EDT10;
    TextInputLayout Cortina11;
    ImageButton BTNCAMERA1 , BTNCAMERA2, BTNCAMERA3 , BTNCAMERA4,IMBTN10;
    LinearLayout LLVV40 ;
    ArrayList<String[]> Orden;
    MiAyudanteSQLite ayuquito;
    Typeface TFAmaranthRegular;
    Typeface TFAmaranthBold;
    Dialog MiDialogo = null;
    ProgressBar Progress11;

    Intent PideCamara1, DameArchivo1,PideCamara2, DameArchivo2;
    int Toma1 =258;
    int Toma2=365;
    int Arch1 =33;
    int Arch2=35;

    private Uri imaginax;
    private Bitmap bmppp;

    LocationManager locManager;
    LocationListener locListener;
    Double Lat = 0d;
    Double Long = 0d;

    AlertDialog pedido;

    int Serviciax = 0;
     JSONObject json;
    SharedPreferences sonadas;


    final static int Codigo_Peticion_Permiso_Ubicacion=14;
    int ServicioElegido = 0;

    Boolean inter = false;
    int punteos = 0;
    Handler Chispea = new Handler();
    Runnable caifas = new Runnable() {
        public void run() {
            Flasheo();
            if (inter) {
                IMBTN10.setImageResource(R.drawable.necesito);
            } else {
                IMBTN10.setImageResource(R.drawable.escoger);
            }
            Chispea.postDelayed(caifas, 700);
        }
    };
    Runnable anas = new Runnable() {
        public void run() {
            Flasheo();
            if (inter) {
                TXV101.setAlpha(1f);
            } else {
                TXV101.setAlpha(0.3f);
            }
            Chispea.postDelayed(anas, 400);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sonadas = PreferenceManager.getDefaultSharedPreferences(this);
            setContentView(R.layout.activity_servicia);

            ayuquito = new MiAyudanteSQLite(this);

            IMBTN10 = (ImageButton) findViewById(R.id.IMBTN10);
            BTNCAMERA1=(ImageButton) findViewById(R.id.BTNCAMERA1) ;
            BTNCAMERA2=(ImageButton) findViewById(R.id.BTNCAMERA2) ;
            BTNCAMERA3=(ImageButton) findViewById(R.id.BTNCAMERA3) ;
            BTNCAMERA4=(ImageButton) findViewById(R.id.BTNCAMERA4) ;
            LLVV40=(LinearLayout) findViewById(R.id.LLVV40);
            TXV10 = (TextView) findViewById(R.id.TXV10);
            TXV40 = (TextView) findViewById(R.id.TXV40);
            TXV101 = (TextView) findViewById(R.id.TXV101);
            TXV21 = (TextView) findViewById(R.id.TXV21);
            EDT10 = (EditText) findViewById(R.id.EDT10);
            BTN10 = (Button) findViewById(R.id.BTN10);
            Cortina11 = (TextInputLayout) findViewById(R.id.Cortina11);
           Progress11=(ProgressBar)findViewById(R.id.Progress11) ;


            IMBTN10.setOnClickListener(this);
            BTN10.setOnClickListener(this);
            BTNCAMERA1.setOnClickListener(this);
            BTNCAMERA2.setOnClickListener(this);

            //Configura aspecto inicial
            BTNCAMERA3.setAlpha(0.2f);
            BTNCAMERA3.setPadding(30,30,30,30);
            BTNCAMERA4.setAlpha(0.2f);
            BTNCAMERA4.setPadding(30,30,30,30);

            TXV40.setOnClickListener(this);
            Cortina11.setVisibility(View.INVISIBLE);
            LLVV40.setVisibility(View.INVISIBLE);
            TXV40.setVisibility(View.INVISIBLE);
            AbreDialogoServicios();

            TXV101.setText(sonadas.getString(getResources().getString(R.string.PropiedadIdCliente), "") );



            //Verifica si los permisos de Ubicacion esta concedidos. En caso negativo pide al usuario
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Codigo_Peticion_Permiso_Ubicacion);
            }else {
            //En caso positivo, continua instanciando el servico de Ubicacion
                InstanciaUbicacion();
            }

            String Amaranth_Regular = "Vermut/Amaranth-Regular.ttf";
            String Amaranth_Bold = "Vermut/Amaranth-Bold.ttf";
            TFAmaranthRegular = Typeface.createFromAsset(getAssets(), Amaranth_Regular);
            TFAmaranthBold = Typeface.createFromAsset(getAssets(), Amaranth_Bold);
            TXV10.setTypeface(TFAmaranthBold);
            TXV40.setTypeface(TFAmaranthBold);
            Chispea.postDelayed(caifas, 500);
        }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.BTN10:

                Progress11.setVisibility(View.VISIBLE);
                json = new JSONObject();

                try {
                    json.put("Cliente",sonadas.getString(getResources().getString(R.string.PropiedadIdCliente), ""));
                    json.put("Servicio", Serviciax);
                    json.put("Latitud", Lat);
                    json.put("Longuitud", Long);
                    json.put("Observaciones", EDT10.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TareaWSInsertar tarea = new TareaWSInsertar();
                tarea.execute(json.toString());
                break;

            case R.id.TXV40:
                LLVV40.setAlpha(1f);
                TXV40.setVisibility(View.INVISIBLE);
                break;

            case R.id.IMBTN10:
                AbreDialogoServicios();

                break;

            case R.id.BTNCAMERA1:
                PideCamara1 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(PideCamara1,Toma1);
                break;

            case R.id.BTNCAMERA3:
                PideCamara2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(PideCamara2,Toma2);
                break;

            case R.id.BTNCAMERA2:
                DameArchivo1 = new Intent(Intent.ACTION_GET_CONTENT);
                DameArchivo1.setType("image/*");
                startActivityForResult(DameArchivo1, Arch1);
                break;

            case R.id.BTNCAMERA4:
                DameArchivo2 = new Intent(Intent.ACTION_GET_CONTENT);
                DameArchivo2.setType("image/*");
                startActivityForResult(DameArchivo2, Arch2);
                break;
/*
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
                */
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Codigo_Peticion_Permiso_Ubicacion: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    InstanciaUbicacion();

                } else {

                    Toast.makeText(this, "Esta app necesita obligatoriamente permisos de Ubicacion para funcionar correctamente", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == Toma1 && resultCode == RESULT_OK  ) {
            Bundle ext = data.getExtras();
            bmppp= (Bitmap)ext.get("data");
            BTNCAMERA1.setImageBitmap(bmppp);

            BTNCAMERA3.setImageResource(R.drawable.camarita);
            BTNCAMERA3.setPadding(0,0,0,0);
            BTNCAMERA3.setAlpha(1f);
            BTNCAMERA3.setOnClickListener(this);


        }

        if (requestCode == Toma2 && resultCode == RESULT_OK ) {
            Bundle ext = data.getExtras();
            bmppp= (Bitmap)ext.get("data");
            BTNCAMERA3.setImageBitmap(bmppp);

        }

        if (requestCode == Arch1 && resultCode == RESULT_OK   ){
            imaginax = data.getData();
            BTNCAMERA2.setImageURI(imaginax);
            BTNCAMERA4.setImageResource(R.drawable.archivito);
            BTNCAMERA4.setPadding(0,0,0,0);
            BTNCAMERA4.setAlpha(1f);
            BTNCAMERA4.setOnClickListener(this);


        }

        if (requestCode == Arch2 && resultCode == RESULT_OK  ) {
            imaginax = data.getData();
            BTNCAMERA4.setImageURI(imaginax);

        }
    }
    private void checkCameraPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para la camara!.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 225);
            Toast.makeText(this, "sera", Toast.LENGTH_LONG).show();
        } else {
            Log.i("Mensaje", "Tienes permiso para usar la camara.");

        }
    }
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
                URL url = new URL("http://sareta.somee.com/PeticionCliente");
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
            Progress11.setVisibility(View.INVISIBLE);
            if (result.equals("1")){
                Toast.makeText(Servicia.this, "Peticion de Servicio ha sido activada", Toast.LENGTH_LONG).show();
                Handler tama = new Handler() ;
                tama.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2500);
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
    public void InstanciaUbicacion(){

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Pide al usuario activar el GPS en caso de estar desactivado
        assert locManager != null;
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           AlertNoGps();
           // turnGPSOn();
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
    }
    private void AbreDialogoServicios(){
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
                Cortina11.setVisibility(View.VISIBLE);
                LLVV40.setVisibility(View.VISIBLE);
                LLVV40.setAlpha(0.1f);
                TXV40.setVisibility(View.VISIBLE);


                MiDialogo.dismiss();
            }
        });

        PoblarGrid(GRD10);
        MiDialogo.show();
    }


}