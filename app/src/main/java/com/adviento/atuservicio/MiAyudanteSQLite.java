package com.adviento.atuservicio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by oficina on 28/11/2017.
 */

public class MiAyudanteSQLite extends SQLiteOpenHelper{

    Context ctx;

    public MiAyudanteSQLite(Context context) {
        super(context,"Servidos.db",null, 4);

        ctx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase BD) {

        BD.execSQL("CREATE TABLE Servicios (IdServicio INTEGER Primary Key autoincrement, Servicio Text not null , Icono text ,Vario1 text )");
        BD.execSQL("CREATE TABLE Departamentos (IdDepartamento INTEGER Primary Key autoincrement, Departamento Text not null , Vario1 text )");

     BD.execSQL("CREATE TABLE  Ciudades (IdCiudad INTEGER Primary Key autoincrement,IdDepart INTEGER NOT NULL, Ciudad Text not null, Latitud Double not null , Longuitud Double not null , Vario1 Text, Vario2 Text)");

 // Busca datos de los departamentos en el fichero asset Ciudades.sql y los introduce en la tabla Ciudades

     PoblarTablasInicio(BD,"Servicios.sql");

// Busca datos de los departamentos en el fichero asset Departamentos.sql y los introduce en la tabla Departamentos

    PoblarTablasInicio(BD,"Departamentos.sql");

// Busca datos de los departamentos en el fichero asset Ciudades.sql y los introduce en la tabla Ciudades

    PoblarTablasInicio(BD,"Ciudades.sql");

    }

    @Override
    public void onUpgrade(SQLiteDatabase BD, int i, int i1) {

        BD.execSQL("DROP TABLE  if exists Departamentos");
        BD.execSQL("DROP TABLE  if exists Ciudades");
        BD.execSQL("DROP TABLE  if exists Servicios");
        onCreate(BD);

    }


    SQLiteDatabase basecita;
    MiAyudanteSQLite  ayucodeBase;

    // Metodos para manejar la base de Datos

        public void AbrirBase  (){

            ayucodeBase = new MiAyudanteSQLite(ctx);
            basecita=ayucodeBase.getWritableDatabase();

        }

        public void  CerrarBase (){

            basecita.close();
        }

    // Metodos para manipular los datos de la base de datos

        public long  InsertarDepartamentos ( String departamento) throws Exception{

            ContentValues valores = new ContentValues();
            valores.put("Departamento",departamento);

            return   basecita.insert("Departamentos",null,valores);
        }

        public int UltimoRegistro() {

          ayucodeBase.AbrirBase();
            Cursor cursor = basecita.rawQuery("select MAX(IdDepartamento) from Departamentos", null);
           cursor.moveToFirst();
           int numeroII = cursor.getInt(0);
           basecita.close();


            return numeroII;
        }

        public void metida(){


            basecita.execSQL("INSERT INTO Departamentos (Departamento) Values ('La Paz');");



        }

    public long CuantosHay (){

        long ahora = 0;
        Cursor melia= basecita.rawQuery("select count(Ciudad) from Ciudades", null);
        melia.moveToFirst();
        ahora = Long.parseLong(melia.getString(0));

        return ahora;

    }

    public Cursor PueblaDepartamentos (){

        return  basecita.rawQuery("select  IdDepartamento as _id, Departamento, Vario1 from Departamentos", null);
    }
    public Cursor PueblaServicios (){

        return  basecita.rawQuery("select  IdServicio as _id, Servicio, Icono from Servicios", null);
    }


    public Cursor PueblaCiudadesDeDepartamento (String iddepart){

        String misql=" Select IdCiudad as _id, Ciudad from Ciudades where IdDepart =" +iddepart;
        return  basecita.rawQuery(misql, null);
    }
    public String BuscaCiudad(String idciudad){

        String misql=" Select * from Ciudades where IdCiudad =" +idciudad;
        Cursor andelo = basecita.rawQuery(misql, null);
        andelo.moveToFirst();

       String capello=  andelo.getString(2);
        return  capello;
    }
    public String BuscaServicio(String idservicio){

        String misql=" Select * from Servicios where IdServicio =" +idservicio;
        Cursor andelo = basecita.rawQuery(misql, null);
        andelo.moveToFirst();

        String cap=  andelo.getString(1);
        return  cap;
    }

    public void PoblarTablasInicio(SQLiteDatabase BaseDatos , String NombreFicheroAsset){
        InputStream is = null;
        try {
            is = ctx.getAssets().open(NombreFicheroAsset);
            if (is != null) {
                BaseDatos.beginTransaction();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                while (!TextUtils.isEmpty(line)) {
                    BaseDatos.execSQL(line);
                    line = reader.readLine();
                }
                BaseDatos.setTransactionSuccessful();

            }
        } catch (Exception ex) {
            // Muestra log
        } finally {
            BaseDatos.endTransaction();
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Muestra log
                }
            }
        }
    }
}
