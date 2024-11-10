package com.example.proyectologinbasico;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declarar atributos de la clase o variables de la instancia
    EditText edtFec, edtHor, edtDir;
    ListView lista;
    private EditText usuarioEditText;
    private EditText contrasenaEditText;
    private Spinner spinner;

    // Variables para reproducir los sonidos
    private MediaPlayer mpAgregar, mpModificar, mpEliminar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtFec = findViewById(R.id.edtFec);
        edtHor = findViewById(R.id.edtHor);
        edtDir = findViewById(R.id.edtDir);
        lista = findViewById(R.id.lstLista);
        CargarLista();

        usuarioEditText = findViewById(R.id.usuario);
        contrasenaEditText = findViewById(R.id.contrasena);
        spinner = findViewById(R.id.spinnerRoles);

        // Configurar los sonidos para cada botón
        mpAgregar = MediaPlayer.create(this, R.raw.sonido_agregar);
        mpModificar = MediaPlayer.create(this, R.raw.sonido_modificar);
        mpEliminar = MediaPlayer.create(this, R.raw.sonido_eliminar);

        // Creamos un arreglo para almacenar las opciones
        String[] roles = {"Usuario", "Programador"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar recursos de MediaPlayer al destruir la actividad
        if (mpAgregar != null) mpAgregar.release();
        if (mpModificar != null) mpModificar.release();
        if (mpEliminar != null) mpEliminar.release();
    }

    public void onClickAcceder(View view) {
        String user = usuarioEditText.getText().toString().trim();
        String pass = contrasenaEditText.getText().toString().trim();
        String rol = spinner.getSelectedItem().toString();

        if (user.isEmpty()) {
            Toast.makeText(this, "Ingrese el Usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user.equals("Binraiden") && pass.equals("1234") && rol.equals("Usuario")) {
            Intent intent = new Intent(this, Usuario.class);
            startActivity(intent);
        } else if (user.equals("Rekiem") && pass.equals("1980") && rol.equals("Programador")) {
            Intent intent = new Intent(this, Admin.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Credenciales Incorrectas", Toast.LENGTH_SHORT).show();
        }
    }

    public void CargarLista() {
        DataHelper dh = new DataHelper(this, "notificacion.db", null, 1);
        SQLiteDatabase bd = dh.getWritableDatabase();
        Cursor c = bd.rawQuery("SELECT fecha, hora, direccion FROM notificacion", null);
        String[] arr = new String[c.getCount()];

        if (c.moveToFirst()) {
            int i = 0;
            do {
                String linea = "||" + c.getString(0) + "||" + c.getString(1)
                        + "||" + c.getString(2) + "||";
                arr[i] = linea;
                i++;
            } while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, arr);
            lista.setAdapter(adapter);
        }

        c.close();  // Cerramos el cursor
        bd.close();
    }

    public void onClickAgregar(View view) {
        // Reproducir sonido
        if (mpAgregar != null) mpAgregar.start();

        DataHelper dh = new DataHelper(this, "notificacion.db", null, 1);
        SQLiteDatabase bd = dh.getWritableDatabase();
        ContentValues reg = new ContentValues();
        reg.put("fecha", edtFec.getText().toString());
        reg.put("hora", edtHor.getText().toString());
        reg.put("direccion", edtDir.getText().toString());

        long resp = bd.insert("notificacion", null, reg);
        bd.close();

        if (resp == -1) {
            Toast.makeText(this, "No se registró", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Registro Agregado", Toast.LENGTH_LONG).show();
        }

        CargarLista();
        limpiar();
    }

    public void limpiar() {
        edtFec.setText("");
        edtHor.setText("");
        edtDir.setText("");
    }

    public void onClickModificar(View view) {
        // Reproducir sonido
        if (mpModificar != null) mpModificar.start();

        DataHelper dh = new DataHelper(this, "notificacion.db", null, 1);
        SQLiteDatabase bd = dh.getWritableDatabase();
        ContentValues reg = new ContentValues();
        reg.put("fecha", edtFec.getText().toString());
        reg.put("hora", edtHor.getText().toString());
        reg.put("direccion", edtDir.getText().toString());

        long respuesta = bd.update("notificacion", reg, "fecha=?", new String[]{edtFec.getText().toString()});
        bd.close();

        if (respuesta == -1) {
            Toast.makeText(this, "Dato no Modificado", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Dato Modificado", Toast.LENGTH_LONG).show();
        }

        limpiar();
        CargarLista();
    }

    public void onClickEliminar(View view) {
        // Reproducir sonido
        if (mpEliminar != null) mpEliminar.start();

        DataHelper dh = new DataHelper(this, "notificacion.db", null, 1);
        SQLiteDatabase bd = dh.getWritableDatabase();

        String FechaEliminar = edtFec.getText().toString();
        long respuesta = bd.delete("notificacion", "fecha=?", new String[]{FechaEliminar});
        bd.close();

        if (respuesta == -1) {
            Toast.makeText(this, "Dato no Encontrado", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Dato Eliminado", Toast.LENGTH_LONG).show();
        }

        limpiar();
        CargarLista();
    }
}

