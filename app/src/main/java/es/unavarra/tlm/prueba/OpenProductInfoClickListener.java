package es.unavarra.tlm.prueba;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import es.unavarra.tlm.prueba.model.Producto;

public class OpenProductInfoClickListener implements View.OnClickListener {

    private Activity activity;
    private Producto producto;

    public OpenProductInfoClickListener(Activity activity, Producto producto) {
        this.activity = activity;
        this.producto = producto;
    }

    @Override
    public void onClick(View v) {
        String description = "Descripción: " + producto.getDescription();
        String location = "Localización: " + producto.getLocation();
        Toast.makeText(activity, description + "   " + location, Toast.LENGTH_SHORT);
    }
}
