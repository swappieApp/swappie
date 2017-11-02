package es.unavarra.tlm.prueba;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class IniciarSesion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
    }

    public void iniciarSesion(View view){

        ProgressDialog dialog = new ProgressDialog(IniciarSesion.this);
        dialog.setMessage("Please wait");
        dialog.show();

        EditText edit = (EditText)findViewById(R.id.editText);
        EditText edit2 = (EditText)findViewById(R.id.editText2);


        String email = edit.getText().toString();
        String pass = edit2.getText().toString();
        String metodo = "email";



    }
}
