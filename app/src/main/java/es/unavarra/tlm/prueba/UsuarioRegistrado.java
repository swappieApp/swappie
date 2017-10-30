package es.unavarra.tlm.prueba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.LocaleDisplayNames;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import es.unavarra.tlm.prueba.model.Producto;

import java.util.ArrayList;

import link.fls.swipestack.SwipeStack;

public class UsuarioRegistrado extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    URL imageUrl;
    TextView txt;
    CircleImageView img;

    private String metodo;

    private SwipeStack pilaCartas;
    private AdaptadorProductos adaptadorProductos;
    private ArrayList<Producto> productos;
    private int posicionActual;

    private PopupWindow popUpWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_registrado);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Cogemos los datos de facebook que hemos guardado

        //String name = getIntent().getStringExtra("name");
        //String[] name2 = name.split(" ");
        //String first_name = name2[0];
        //String last_name = name2[1];
        //String email = getIntent().getStringExtra("email");
        //String id = getIntent().getStringExtra("id");

        SharedPreferences settings = getSharedPreferences("Config", 0);
        metodo = settings.getString("metodo","");

        if (metodo.equals("google")){

            boolean sesion = settings.getBoolean("sesion", false);
            String name = settings.getString("nombre", "");
            String imagen = settings.getString("foto","");
            String email = settings.getString("email","");

            View headerView = navigationView.getHeaderView(0);
            txt = (TextView) headerView.findViewById(R.id.nameUser);
            txt.setText(name);

            txt = (TextView) headerView.findViewById(R.id.emailUser);
            txt.setText(email);

            img = (CircleImageView) headerView.findViewById(R.id.imageUser);

            try {
                URL url= new URL(imagen);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                img.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }else if (metodo.equals("facebook")) {


            boolean sesion = settings.getBoolean("sesion", false);
            String name = settings.getString("nombre", "");
            String id = settings.getString("id", "");
            String[] name2 = name.split(" ");
            String first_name = name2[0];
            String last_name = name2[1];
            String email = settings.getString("email","");

            Log.d("etiqueta", String.valueOf(sesion));
            Log.d("etiqueta", name);

            View headerView = navigationView.getHeaderView(0);
            txt = (TextView) headerView.findViewById(R.id.nameUser);
            txt.setText(first_name + " " + last_name);

            txt = (TextView) headerView.findViewById(R.id.emailUser);
            txt.setText(email);

            img = (CircleImageView) headerView.findViewById(R.id.imageUser);
            try {
                imageUrl = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                img.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (metodo.equals("email")){

            boolean sesion = settings.getBoolean("sesion", false);
            String name = settings.getString("nombre", "");
            String apellidos = settings.getString("apellidos","");
            String email = settings.getString("email","");

            View headerView = navigationView.getHeaderView(0);
            txt = (TextView) headerView.findViewById(R.id.nameUser);
            txt.setText(name + " " + apellidos);

            txt = (TextView) headerView.findViewById(R.id.emailUser);
            txt.setText(email);

            img = (CircleImageView) headerView.findViewById(R.id.imageUser);
            try {
                imageUrl = new URL("https://www.viawater.nl/files/default-user.png");
                Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                img.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //SEGUNDA PARTE
        // Se establece la barra de herramientas como la barra de app de la actividad.


        /* Se puede borrar el nombre de la APP de la barra con:

                 getSupportActionBar().setDisplayShowTitleEnabled(false);
         */


        pilaCartas = (SwipeStack) findViewById(R.id.pila_cartas);

        cargarDatos();

        posicionActual = 0;

        // Se asigna el listener que controla los movimientos de swipe left y swipe right.
        pilaCartas.setListener(new SwipeStack.SwipeStackListener() {
            @Override
            public void onViewSwipedToLeft(int position) {
                posicionActual = position + 1;
            }

            @Override
            public void onViewSwipedToRight(int position) {
                posicionActual = position + 1;
            }

            @Override
            public void onStackEmpty() {

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.usuario_registrado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_tutorial_registrado) {

            Intent intent = new Intent(this, Tutorial.class);
            startActivity(intent);

        }else if(id == R.id.nav_close) {

            if (metodo.equals("google")) {

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                SharedPreferences info = getSharedPreferences("Config", 0);
                SharedPreferences.Editor editor = info.edit();
                editor.putBoolean("sesion", false);
                editor.commit();
                Intent intent = new Intent(this, Navigation_drawer.class);
                startActivity(intent);
                finish();

            } else if (metodo.equals("facebook")) {
                LoginManager.getInstance().logOut();
                SharedPreferences info = getSharedPreferences("Config", 0);
                SharedPreferences.Editor editor = info.edit();
                editor.putBoolean("sesion", false);
                editor.commit();
                Intent intent = new Intent(this, Navigation_drawer.class);
                startActivity(intent);
                finish();

            } else if (metodo.equals("email")){

                SharedPreferences info = getSharedPreferences("Config", 0);
                SharedPreferences.Editor editor = info.edit();
                editor.putBoolean("sesion", false);
                editor.commit();
                Intent intent = new Intent(this, Navigation_drawer.class);
                startActivity(intent);
                finish();
            }

        }else if (id == R.id.nav_contact2) {


            LayoutInflater inflater = (LayoutInflater) UsuarioRegistrado.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup, (ViewGroup) findViewById(R.id.popup_element));

            popUpWindow = new PopupWindow(layout, 1000, 700, true);
            popUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            cancelButton.setOnClickListener(cancel_button_click_listener);

        }else if(id==R.id.nav_help_us_2){

            sendEmail();

       // int id = item.getItemId();

       // if (id == R.id.nav_camera) {
            // Handle the camera action
       // } else if (id == R.id.nav_gallery) {

       // } else if (id == R.id.nav_slideshow) {

       // } else if (id == R.id.nav_manage) {

       // } else if (id == R.id.nav_share) {

       // } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cargarDatos() {
        productos = new ArrayList<>();

        productos.add(new Producto(R.drawable.reloj, "Reloj de mujer", "Pamplona"));
        productos.add(new Producto(R.drawable.portatil, "Portatil Lenovo", "Burlada"));
        productos.add(new Producto(R.drawable.conversorvgahdmi, "Conversor VGA/HDMI", "Barañain"));
        productos.add(new Producto(R.drawable.ps4, "PS4 y mando", "Noáin"));
        productos.add(new Producto(R.drawable.armarioarchivador, "Armario archivador", "Mendillorri"));
        productos.add(new Producto(R.drawable.hoverboard, "Hoverboard", "Huarte"));
        productos.add(new Producto(R.drawable.yamahar6, "Yamaha R6", "Zizur Mayor"));
        productos.add(new Producto(R.drawable.gabrielgarciamarquez, "Cien años de soledad", "Pamplona"));
        productos.add(new Producto(R.drawable.cargador, "Cargador", "Barañain"));
        productos.add(new Producto(R.drawable.minibillar, "Mini-Billar", "Villava"));
        productos.add(new Producto(R.drawable.monitorpc, "Monitor PC", "Burlada"));
        productos.add(new Producto(R.drawable.cafetera, "Cafetera", "Zizur Mayor"));
        productos.add(new Producto(R.drawable.bici, "Bici Pinarello", "Pamplona"));
        productos.add(new Producto(R.drawable.cascomoto, "Casco moto", "Ripagaina"));
        productos.add(new Producto(R.drawable.guitarraelectrica, "Guitarra eléctrica", "Barañain"));

        adaptadorProductos = new AdaptadorProductos(this, productos);
        pilaCartas.setAdapter(adaptadorProductos);
    }

    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            popUpWindow.dismiss();
        }
    };

    protected void sendEmail() {
        String[] TO = {"swappieapp2017@gmail.com"}; //aquí pon tu correo
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
// Esto podrás modificarlo si quieres, el asunto y el cuerpo del mensaje
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Escribe aquí tu mensaje");

        try {
            startActivity(Intent.createChooser(emailIntent, "Indícanos qué te gustaría modificar de la aplicación"));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(UsuarioRegistrado.this,
                    "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
        }
    }

    public void abrirCamara(View view){

        Intent intent = new Intent(this, Camara.class);
        startActivity(intent);

    }


}
