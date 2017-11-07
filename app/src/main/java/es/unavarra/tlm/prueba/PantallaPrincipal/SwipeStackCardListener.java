package es.unavarra.tlm.prueba.PantallaPrincipal;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

import es.unavarra.tlm.prueba.model.Producto2;
import link.fls.swipestack.SwipeStack;

public class SwipeStackCardListener implements SwipeStack.SwipeStackListener{

    private Activity activity;
    private ArrayList<Producto2> productos;

    public SwipeStackCardListener(Activity activity, ArrayList<Producto2> productos) {
        this.activity = activity;
        this.productos = productos;
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String descripcion = productos.get(position).getDescription();
        int id = productos.get(position).getId();

        Log.d("Swipe", "Descripción: " + descripcion + "  ID: " + id);
    }

    @Override
    public void onViewSwipedToRight(int position) {

        String descripcion = productos.get(position).getDescription();
        int id = productos.get(position).getId();

        Log.d("Swipe", "Descripción: " + descripcion + "  ID: " + id);
    }

    @Override
    public void onStackEmpty() {
        Log.e("etiqueta", "Stack vacio");
    }

}
