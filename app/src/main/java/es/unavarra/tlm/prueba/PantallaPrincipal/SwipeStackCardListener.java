package es.unavarra.tlm.prueba.PantallaPrincipal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import es.unavarra.tlm.prueba.ClasePeticionRest;
import es.unavarra.tlm.prueba.MainActivity;
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

        if (this.idUsuario != 0) {
            new ClasePeticionRest.GuardarSwipe(activity, this.idUsuario, idObjeto, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            new ClasePeticionRest.CogerObjetoAleatorioSwipe(activity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        String descripcion = productos.get(position).getDescription();
        Log.d("etiqueta", "SWIPE ->> Descripción: " + descripcion + "  ID: " + idObjeto + " DECISION: false ID_USUARIO: " + this.idUsuario);
    }

    @Override
    public void onViewSwipedToRight(int position) {
        int idObjeto = productos.get(position).getId();

        if (this.idUsuario != 0) {
            new ClasePeticionRest.GuardarSwipe(activity, this.idUsuario, idObjeto, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            ClasePeticionRest.mostrarCustomToast(activity);
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }

        String descripcion = productos.get(position).getDescription();
        Log.d("etiqueta", "SWIPE ->> Descripción: " + descripcion + "  ID: " + idObjeto + " DECISION: true ID_USUARIO: " + this.idUsuario);
    }

    @Override
    public void onStackEmpty() {
        Log.e("etiqueta", "Stack vacio");
    }

}
