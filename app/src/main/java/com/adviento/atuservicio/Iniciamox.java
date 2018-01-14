package com.adviento.atuservicio;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.caminar.miscosas.Utilidades;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Iniciamox extends AppCompatActivity implements View.OnClickListener {

    private static final long Ticks_Hasta_1Enero1970 = 621355968000000000L;


    Button BTNX1;
    TextView TVX2;
    EditText ETX1, ETX2, ETX3;
    TextInputLayout Cortina2, Cortina1, Cortina3;
    LinearLayout login;
    ImageView IMV10;
    String ClaveFCM ="";
    Typeface TFAmaranthBold, TFAmaranthRegular;
    ProgressBar Progress10;
    private Handler tomate = new Handler();
    private Handler mostaza = new Handler();
    Runnable Carme = new Runnable() {
        @Override
        public void run() {
            finishAffinity();

        }
    };

    SharedPreferences amadas;
    Handler opera = new Handler();
    Handler prima = new Handler();
    JSONObject canto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciamox);
        BTNX1 = (Button) findViewById(R.id.BTNX1);
        ETX1 = (EditText) findViewById(R.id.ETX1);
        ETX2 = (EditText) findViewById(R.id.ETX2);
        ETX3 = (EditText) findViewById(R.id.ETX3);
        TVX2 = (TextView) findViewById(R.id.TVX2);
        IMV10 = (ImageView) findViewById(R.id.IMV10);

        Cortina2 = (TextInputLayout) findViewById(R.id.Cortina2);
        Cortina1 = (TextInputLayout) findViewById(R.id.Cortina1);
        Cortina3 = (TextInputLayout) findViewById(R.id.Cortina3);
        login = (LinearLayout) findViewById(R.id.login);
        Progress10=(ProgressBar)findViewById(R.id.Progress10) ;


        String amaru = "Vermut/Amaranth-Bold.ttf";
        String amichi = "Vermut/Amaranth-Regular.ttf";



        TFAmaranthBold=Typeface.createFromAsset(getAssets(), amaru);
        TFAmaranthRegular=Typeface.createFromAsset(getAssets(), amichi);

        TVX2.setTypeface(TFAmaranthBold);
        ETX1.setTypeface(TFAmaranthRegular);
        ETX2.setTypeface(TFAmaranthRegular);
        ETX3.setTypeface(TFAmaranthRegular);


        BTNX1.setOnClickListener(this);

        amadas = PreferenceManager.getDefaultSharedPreferences(this);



    }

    @Override
    public void onClick(View view) {

        if (!ETX1.getText().toString().equals("")&&!ETX2.getText().toString().equals("")&&!ETX3.getText().toString().equals("")){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(login.getWindowToken(), 0);
            BTNX1.setVisibility(View.INVISIBLE);
            Progress10.setVisibility(View.VISIBLE);
            IMV10.setVisibility(View.INVISIBLE);

            SharedPreferences.Editor editorial = amadas.edit();

            editorial.putString(getResources().getString(R.string.PropiedadNombre), ETX1.getText().toString());
            editorial.putString(getResources().getString(R.string.PropiedadApellido), ETX2.getText().toString());
            editorial.putString(getResources().getString(R.string.PropiedadCelular),ETX3.getText().toString());

            editorial.apply();

            tomate.postDelayed(new Runnable() {
                public void run() {
                    Progress10.setVisibility(View.INVISIBLE);
                    Toast.makeText(Iniciamox.this, "RECEPCION EN SERVIDOR .... OK",                                Toast.LENGTH_LONG).show();
                    mostaza.postDelayed(Carme,2000);
                }
            }, 3000);


         }

     // PreguntaAzure horitinga = new PreguntaAzure();
    //  horitinga.execute(ETX1.getText().toString(), ETX2.getText().toString());
    }

    public boolean RegistroClaveFcmSocioEnServidor(String traza, String clafcm)
    // Registra el RegistroID por primera vez en el servidor
    {
        boolean reg = false;
         canto = new JSONObject();

        try {
            canto.put("id", traza);
            canto.put("Rfcm", clafcm);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String gason  = canto.toString();
        String resina="";

// Obtener la conexión

        String direccion="http://sareta.somee.com/MicroMoreREST/FCMSocio";

        HttpURLConnection MiConexione = null;

        try {
            // Construir los datos a enviar

            URL urll = new URL(direccion);
            MiConexione = (HttpURLConnection) urll.openConnection();
            MiConexione.setRequestMethod("POST");
            MiConexione.setRequestProperty("Content-Type", "application/json");
            MiConexione.setRequestProperty("Content-Length",  Integer.toString(gason.getBytes().length));

            MiConexione.setDoInput(true);
            MiConexione.setDoOutput(true);

            OutputStream outt = new BufferedOutputStream(MiConexione.getOutputStream());

            outt.write(gason.getBytes());
            outt.flush();
            outt.close();

            resina= Utilidades.LectorRespuestasSTRING(MiConexione);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (MiConexione != null)
                MiConexione.disconnect();
        }

            if (resina.equals("1")){reg=true;} else {reg=false;}


        return reg;
    }

    private void Desvanecer(final ImageView img) {
        Animation desvaneceme = new AlphaAnimation(0, 1);
        desvaneceme.setInterpolator(new AccelerateInterpolator());
        desvaneceme.setDuration(3000);

        desvaneceme.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        img.startAnimation(desvaneceme);
    }

    private void crossFadeAnimation(final View fadeInTarget, final View fadeOutTarget, long duration) {
        AnimatorSet mAnimationSet = new AnimatorSet();
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(fadeOutTarget, View.ALPHA, 1f, 0f);
        fadeOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fadeOutTarget.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        fadeOut.setInterpolator(new LinearInterpolator());

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(fadeInTarget, View.ALPHA, 0f, 1f);
        fadeIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                fadeInTarget.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        fadeIn.setInterpolator(new LinearInterpolator());
        mAnimationSet.setDuration(duration);
        mAnimationSet.playTogether(fadeOut, fadeIn);
        mAnimationSet.start();
    }

    private class PreguntaAzure extends AsyncTask<String, Integer, String> {
        String resultado = "";

        protected String doInBackground(String... params) {
            String resul="";

// Obtener la conexión

            String direc ="http://sareta.somee.com/MicroMoreREST/IniciaSocio/"+params[0]+"/"+params[1];

            HttpURLConnection MiConexion = null;

            try {
                // Construir los datos a enviar

                URL url = new URL(direc);
                MiConexion = (HttpURLConnection) url.openConnection();
                MiConexion.setRequestMethod("POST");
                MiConexion.setRequestProperty("Content-Type", "application/json");

                MiConexion.setDoInput(true);
                MiConexion.setDoOutput(true);

                OutputStream out = new BufferedOutputStream(MiConexion.getOutputStream());


                out.flush();
                out.close();

                resul= Utilidades.LectorRespuestasSTRING(MiConexion);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (MiConexion != null)
                    MiConexion.disconnect();
            }

            return resul;
        }


        protected void onPostExecute(String result) {
            if (result.equals("1")) {

                SharedPreferences.Editor editorial = amadas.edit();

/*
                editorial.putString(getResources().getString(R.string.PropiedadSocio), ETX1.getText().toString());
                editorial.putString(getResources().getString(R.string.PropiedadVersion), "A");
                editorial.putString(getResources().getString(R.string.PropiedadLatitudHito1), String.valueOf(LatitudHito1));
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudHito1), String.valueOf(LonguitudHito1));
                editorial.putString(getResources().getString(R.string.PropiedadLatitudHito2), String.valueOf(LatitudHito2));
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudHito2), String.valueOf(LonguitudHito2));
                editorial.putString(getResources().getString(R.string.PropiedadLatitudHito3), String.valueOf(LatitudHito3));
                editorial.putString(getResources().getString(R.string.PropiedadLonguitudHito3), String.valueOf(LonguitudHito3));
                editorial.putString(getResources().getString(R.string.PropiedadNombreUbicacion1), "Ubicacion1");
                editorial.putString(getResources().getString(R.string.PropiedadNombreUbicacion2), "Ubicacion2");
                editorial.putString(getResources().getString(R.string.PropiedadNombreUbicacion3), "Ubicacion3");
                editorial.putInt(getResources().getString(R.string.PropiedadRadioEnMetrosHito1), 10);
                editorial.putInt(getResources().getString(R.string.PropiedadRadioEnMetrosHito2), 10);
                editorial.putInt(getResources().getString(R.string.PropiedadRadioEnMetrosHito3), 10);
                editorial.putLong(getResources().getString(R.string.PropiedadFechaCambioAceiteMotor), Tiempos.TiempoActualTicks());
                editorial.putLong(getResources().getString(R.string.PropiedadFechaCambioAceiteCaja), Tiempos.TiempoActualTicks());
                editorial.putLong(getResources().getString(R.string.PropiedadFechaCambioFrenos), Tiempos.TiempoActualTicks());
                editorial.putLong(getResources().getString(R.string.PropiedadFechaInspeccionGNV), Tiempos.TiempoActualTicks());

                editorial.putString(getResources().getString(R.string.PropiedadTipoMapa), "a");


                editorial.apply();
*/
                TareaRegistrarEnServidorLaClaveFCMSocio calmita = new TareaRegistrarEnServidorLaClaveFCMSocio();
               calmita.execute(amadas.getString(getResources().getString(R.string.PropiedadNombre), ""));

            } else {
                Toast.makeText(Iniciamox.this, "Proceso FALLIDO", Toast.LENGTH_LONG).show();

                Iniciamox.this.finish();
            }
        }
    }

    public class TareaRegistrarEnServidorLaClaveFCMSocio extends AsyncTask<String, Integer, String> {
        String msg = "";
        @Override
        protected String doInBackground(String... params) {


            ClaveFCM = FirebaseInstanceId.getInstance().getToken();

            //Nos registramos en nuestro servidor
            boolean registrado = RegistroClaveFcmSocioEnServidor(params[0], ClaveFCM);

            if (registrado) {
                msg = "Exitoso";
            }
            return msg;
        }

        protected void onPostExecute(String result) {
            if (result.equals("Exitoso")) {
                Toast.makeText(Iniciamox.this, "PROCESO EXITOSO", Toast.LENGTH_LONG).show();

                Iniciamox.this.finish();
            } else {
                Toast.makeText(Iniciamox.this, "PROCESO FALLIDO", Toast.LENGTH_LONG).show();

                Iniciamox.this.finish();
            }

        }
    }
}
