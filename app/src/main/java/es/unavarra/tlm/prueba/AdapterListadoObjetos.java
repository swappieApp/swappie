package es.unavarra.tlm.prueba;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import es.unavarra.tlm.prueba.model.Producto;
import es.unavarra.tlm.prueba.model.Producto;

/**
 * Created by ibai on 10/19/17.
 */

public class AdapterListadoObjetos extends BaseAdapter {

    List<Producto> productos;
    LayoutInflater inflater;
    Activity activity;

    public AdapterListadoObjetos(Activity activity, List<Producto> productos) {
        this.productos = productos;
        this.activity = activity;
        inflater = LayoutInflater.from(this.activity);
        for (int i=0;i<productos.size();i++){
            Log.d("productos",productos.get(i).getDescription());
        }
    }

    @Override
    public int getCount() {
        return productos.size();
    }

    @Override
    public Producto getItem(int i) {
        return productos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AuxProducto auxProducto;

        if (view == null) {
            view = inflater.inflate(R.layout.listado_objetos_list_item, viewGroup, false);
            auxProducto = new AuxProducto(view);
            view.setTag(auxProducto);
        } else {
            auxProducto = (AuxProducto) view.getTag();
        }


        Producto producto = getItem(i);

        auxProducto.descripcionProducto.setText(producto.getDescription());
        //auxProducto.delete.setImageResource(R.drawable.trash);
        auxProducto.delete.setOnClickListener(new BorrarObjeto(activity, producto));

        return view;
    }

    private class AuxProducto {
        TextView descripcionProducto;
        ImageView delete;

        public AuxProducto(View view) {
            descripcionProducto = view.findViewById(R.id.DescripcionListadoObjetos);
            delete = view.findViewById(R.id.BotonBorrarObjeto);
        }
    }
}
