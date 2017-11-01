package es.unavarra.tlm.prueba.PantallaPrincipal;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

import es.unavarra.tlm.prueba.PantallaPrincipal.model.Producto;
import link.fls.swipestack.SwipeStack;

public class SwipeStackCardListener implements SwipeStack.SwipeStackListener{

    private Activity activity;
    private ArrayList<Producto> productos;

    public SwipeStackCardListener(Activity activity, ArrayList<Producto> productos) {
        this.activity = activity;
        this.productos = productos;
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String descripcion = productos.get(position).getDescription();
        String localizacion = productos.get(position).getLocation();

        Log.d("Main", "Descripción: " + descripcion + "  Localización: " + localizacion);
    }

    @Override
    public void onViewSwipedToRight(int position) {
        //ClasePeticionRest peticion = new ClasePeticionRest();
        //int idUsuario = 1234;
        //int idObjeto = 9876;
        //peticion.guardarSwipe(idUsuario, idObjeto, true);

        //ClasePeticionRest.GuardarSwipe guardarSwipe = new ClasePeticionRest.GuardarSwipe(activity, 1234, 9876, true);

        String descripcion = productos.get(position).getDescription();
        String localizacion = productos.get(position).getLocation();

        Log.d("Main", "Descripción: " + descripcion + "  Localización: " + localizacion);
    }

    @Override
    public void onStackEmpty() {

    }

}
