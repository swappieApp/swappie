package es.unavarra.tlm.swappie.PantallaPrincipal;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import es.unavarra.tlm.swappie.PantallaPrincipal.model.Producto;
import es.unavarra.tlm.swappie.R;
import link.fls.swipestack.SwipeStack;

public class PantallaPrincipalActivity extends AppCompatActivity {

    private SwipeStack pilaCartas;
    private AdaptadorProductos adaptadorProductos;
    private ArrayList<Producto> productos;
    private int posicionActual;

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.navview);

        setSupportActionBar(toolbar);
        String title = toolbar.getTitle().toString();
        SpannableString s = new SpannableString("Swappie");
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.action_bar_items)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("Main", "Navigation View abierta.");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("Main", "Navigation View cerrada.");
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }



        /* Se puede borrar el nombre de la APP de la barra con:

                 getSupportActionBar().setDisplayShowTitleEnabled(false);
         */

        pilaCartas = (SwipeStack) findViewById(R.id.pila_cartas);

        cargarDatos();

        posicionActual = 0;

        // Se asigna el listener que controla los movimientos de swipe left y swipe right.
        pilaCartas.setListener(new SwipeStackCardListener(this, productos));

        // Se asigna el listener que controla los movimientos de swipe left y swipe right.
        /*pilaCartas.setListener(new SwipeStack.SwipeStackListener() {
            @Override
            public void onViewSwipedToLeft(int position) {
                posicionActual = position + 1;
            }

            @Override
            public void onViewSwipedToRight(int position) {
                //ClasePeticionRest peticion = new ClasePeticionRest();
                //int idUsuario = 1234;
                //int idObjeto = 9876;
                //peticion.guardarSwipe(idUsuario, idObjeto, true);

                posicionActual = position + 1;
            }

            @Override
            public void onStackEmpty() {

            }
        });*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void cargarDatos() {
        productos = new ArrayList<>();

        productos.add(new Producto(R.drawable.reloj, "Reloj de mujer", "Pamplona"));
        productos.add(new Producto(R.drawable.portatil, "Portatil Lenovo", "Burlada"));
        productos.add(new Producto(R.drawable.hoverboard, "Hoverboard", "Huarte"));
        productos.add(new Producto(R.drawable.conversorvgahdmi, "Conversor VGA/HDMI", "Barañain"));
        productos.add(new Producto(R.drawable.armarioarchivador, "Armario archivador", "Mendillorri"));
        productos.add(new Producto(R.drawable.minibillar, "Mini-Billar", "Villava"));
        productos.add(new Producto(R.drawable.monitorpc, "Monitor PC", "Burlada"));
        productos.add(new Producto(R.drawable.ps4, "PS4 y mando", "Noáin"));
        productos.add(new Producto(R.drawable.yamahar6, "Yamaha R6", "Zizur Mayor"));
        productos.add(new Producto(R.drawable.gabrielgarciamarquez, "Cien años de soledad", "Pamplona"));
        productos.add(new Producto(R.drawable.cargador, "Cargador", "Barañain"));
        productos.add(new Producto(R.drawable.cafetera, "Cafetera", "Zizur Mayor"));
        productos.add(new Producto(R.drawable.bici, "Bici Pinarello", "Pamplona"));
        productos.add(new Producto(R.drawable.cascomoto, "Casco moto", "Ripagaina"));
        productos.add(new Producto(R.drawable.guitarraelectrica, "Guitarra eléctrica", "Barañain"));

        adaptadorProductos = new AdaptadorProductos(this, productos);
        pilaCartas.setAdapter(adaptadorProductos);
    }

}
