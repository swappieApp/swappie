package es.unavarra.tlm.prueba;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import es.unavarra.tlm.prueba.model.Producto;

public class ListadoObjetos extends AppCompatActivity {

    public static ArrayList<Producto> productos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_objetos);
        productos.clear();

        SharedPreferences settings = getSharedPreferences("Config", 0);
        new ClasePeticionRest.CogerInfoObjetos(this, settings.getInt("id", 0));

    }
}
