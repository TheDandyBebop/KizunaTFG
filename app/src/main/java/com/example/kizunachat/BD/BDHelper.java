package com.example.kizunachat.BD;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kizunachat.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BDHelper extends SQLiteOpenHelper {
    Context contexto;

    public BDHelper(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
        this.contexto = contexto;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            InputStream ficheroSQL = contexto.getResources().openRawResource(R.raw.bdchats);
            BufferedReader linea = new BufferedReader(new InputStreamReader(ficheroSQL));

            while (true){
                String orden = linea.readLine();
                if (orden == null) break;
                db.execSQL(orden);
            }
            linea.close();
        }catch (SQLException | IOException e){

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS contactos");
        onCreate(db);
    }
}
