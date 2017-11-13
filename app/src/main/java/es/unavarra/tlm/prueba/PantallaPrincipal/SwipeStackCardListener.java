package es.unavarra.tlm.prueba.PantallaPrincipal;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import es.unavarra.tlm.prueba.ClasePeticionRest;
import es.unavarra.tlm.prueba.model.Producto;
import link.fls.swipestack.SwipeStack;

public class SwipeStackCardListener implements SwipeStack.SwipeStackListener{

    private ArrayList<Producto> productos;
    Activity activity;
    int idUsuario;
    SharedPreferences settings;

    public SwipeStackCardListener(Activity activity, ArrayList<Producto> productos) {
        this.activity = activity;
        this.productos = productos;
        this.settings = activity.getSharedPreferences("Config", 0);
        this.idUsuario = settings.getInt("id", 0);

    }


    @Override
    public void onViewSwipedToLeft(int position) {
        int idObjeto = productos.get(position).getId();

        Log.d("variables", settings.getString("nombre","")+" hace swipe left y su id es "+String.valueOf(this.idUsuario));

        new ClasePeticionRest.GuardarSwipe(activity, idUsuario, idObjeto, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        String descripcion = productos.get(position).getDescription();
        Log.d("etiqueta", "SWIPE ->> Descripción: " + descripcion + "  ID: " + idObjeto);
    }

    @Override
    public void onViewSwipedToRight(int position) {
        int idObjeto = productos.get(position).getId();

        new ClasePeticionRest.GuardarSwipe(activity, idUsuario, idObjeto, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        String descripcion = productos.get(position).getDescription();
        Log.d("etiqueta", "SWIPE ->> Descripción: " + descripcion + "  ID: " + idObjeto);
    }

    @Override
    public void onStackEmpty() {
        Log.e("etiqueta", "Stack vacio");
    }

}
