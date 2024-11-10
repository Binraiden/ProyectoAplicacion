package com.example.proyectologinbasico;

//Importo librerias para usar SQLite
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.jetbrains.annotations.Nullable;

//Declaramos una clase publica que hereda de SQLiteOpenHelper

public class DataHelper extends SQLiteOpenHelper{

    //Creamos un constructor de la Clase
    public DataHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);

    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE notificacion(fecha INTEGER PRIMARY KEY, hora TEXT, direccion TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS notificacion");
        db.execSQL("CREATE TABLE notificacion(fecha INTEGER PRIMARY KEY, hora TEXT, direccion TEXT)");
    }


}
