package com.adviento.atuservicio;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class Servicia extends AppCompatActivity implements View.OnClickListener{

    TextView TXV10;
    ImageButton IMBTN10;
    ArrayList<String[]> Orden;
    MiAyudanteSQLite ayuquito ;
    Typeface TFAmaranthRegular ;
    Typeface TFAmaranthBold;
    Dialog MiDialogo = null;
    int ServicioElegido;
    Boolean inter= false;
    Handler Chispea = new Handler();
    Runnable caifas = new Runnable() {
        public void run() {
            Flasheo();
            if (inter) {
                IMBTN10.setAlpha(1f);
            } else {
                IMBTN10.setAlpha(0.3f);
            }
            Chispea.postDelayed(caifas,600);
        }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicia);

        ayuquito= new MiAyudanteSQLite(this);

        IMBTN10 = (ImageButton)findViewById(R.id.IMBTN10);
        TXV10 = (TextView) findViewById(R.id.TXV10);

        IMBTN10.setOnClickListener(this);

        String Amaranth_Regular = "Vermut/Amaranth-Regular.ttf";
        String Amaranth_Bold = "Vermut/Amaranth-Bold.ttf";

        TFAmaranthRegular = Typeface.createFromAsset(getAssets(),Amaranth_Regular);
        TFAmaranthBold= Typeface.createFromAsset(getAssets(),Amaranth_Bold);
        TXV10.setTypeface(TFAmaranthBold);

        Chispea.postDelayed(caifas,300);


    }

    @Override
    public void onClick(View view) {
            // con este tema personalizado evitamos los bordes por defecto
             MiDialogo = new Dialog(this,R.style.Theme_Dialog_Translucent);
            //deshabilitamos el título por defecto
            MiDialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //establecemos el contenido de nuestro dialog
            MiDialogo.setContentView(R.layout.dialog);

            final GridView GRD10 = (GridView) MiDialogo.findViewById(R.id.DIAGRD10);
            GRD10.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {


                    String casino = String.valueOf(id);
                    ServicioElegido =(int)id;
                    ayuquito.AbrirBase();

                    TXV10.setVisibility(View.VISIBLE);
                    TXV10.setText(Orden.get(i)[1]);

                    TXV10.setOnClickListener(Servicia.this);
                    IMBTN10.setVisibility(View.INVISIBLE);
                    MiDialogo.dismiss();


                }
            });

            PoblarGrid(GRD10);
            MiDialogo.show();


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
    public void  PoblarGrid (GridView grid){
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
            } while(pepone.moveToNext());
        }

        MiAdaptadorCarrito cartucho = new MiAdaptadorCarrito(this,Orden);
        grid.setAdapter(cartucho);

    }
    public void  Flasheo(){

        if(inter) {
            inter = false;
        }else {inter = true;  }
    }
}

