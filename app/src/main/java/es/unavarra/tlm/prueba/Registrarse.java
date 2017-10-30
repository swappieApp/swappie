package es.unavarra.tlm.prueba;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

import es.unavarra.tlm.prueba.ClasePeticionRest;

public class Registrarse extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);


    }



    public void clickRegistrarse(View view){

        EditText edit = (EditText)findViewById(R.id.editText);
        EditText edit2 = (EditText)findViewById(R.id.editText2);
        EditText edit3 = (EditText)findViewById(R.id.editText3);
        EditText edit4 = (EditText)findViewById(R.id.editText4);

        String nombre = edit.getText().toString();
        String apellidos = edit2.getText().toString();
        String email = edit3.getText().toString();
        String pass = edit4.getText().toString();
        String ubicacion = "Pamplona";
        String metodo="email";

        Integer result = new ClasePeticionRest.GuardarUsuario(getApplicationContext(),nombre,apellidos,email,pass,ubicacion,metodo).doInBackground();
        new ClasePeticionRest.GuardarUsuario(getApplicationContext(),nombre,apellidos,email,pass,ubicacion,metodo).onPostExecute(result);

        Log.d("resultado", String.valueOf(result));


        //Añadimos a SharedPreferences
        SharedPreferences info = getSharedPreferences("Config", 0);
        SharedPreferences.Editor editor = info.edit();
        editor.putString("metodo",metodo);
        editor.putBoolean("sesion", true);
        editor.putString("nombre",nombre);
        editor.putString("apellidos",apellidos);
        editor.putString("email",email);

        editor.commit();

        //Iniciamos la sesion
        Intent intent = new Intent(this, UsuarioRegistrado.class);
        startActivity(intent);
        finish();

    }
}
