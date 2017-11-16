package es.unavarra.tlm.prueba.PantallaPrincipal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import android.widget.Toast;

import es.unavarra.tlm.prueba.Camara;
import es.unavarra.tlm.prueba.ClasePeticionRest;
import es.unavarra.tlm.prueba.R;
import es.unavarra.tlm.prueba.Tutorial;

import es.unavarra.tlm.prueba.chat.ChatListActivity;
import es.unavarra.tlm.prueba.model.Producto;

public class UsuarioRegistrado extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    URL imageUrl;
    TextView txt;
    CircleImageView img;

    private String metodo;          // Contiene el método que utilizó el usuario para registrarse.

    private DrawerLayout drawer;

    private PopupWindow popUpWindow;

    public static ArrayList<Producto> productos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        productos.clear();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_registrado);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Se selecciona el menú lateral de navegación.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Se identifica el método que utilizó el usuario para registrarse.
        SharedPreferences settings = getSharedPreferences("Config", 0);
        metodo = settings.getString("metodo","");
        Log.d("elmetodo",metodo);

        if (metodo.equals("google")){

            // Se extrae la información almacenada en Shared Preferences.
            boolean sesion = settings.getBoolean("sesion", false);
            String name = settings.getString("nombre", "");
            String apellidos = settings.getString("apellidos","");
            String imagen = settings.getString("foto","");
            String email = settings.getString("email","");

            // Se selecciona la cabecera del menú lateral de navegación.
            View headerView = navigationView.getHeaderView(0);

            // Se asigna el nombre del usuario al campo correspondiente.
            txt = (TextView) headerView.findViewById(R.id.nameUser);
            txt.setText(name + " " + apellidos);

            // Se asigna el email del usuario al campo correspondiente.
            txt = (TextView) headerView.findViewById(R.id.emailUser);
            txt.setText(email);

            // Se asigna la imagen de perfil del usuario al campo correspondiente.
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

            // Se extrae la información almacenada en Shared Preferences.
            boolean sesion = settings.getBoolean("sesion", false);
            String name = settings.getString("nombre", "");
            String idFacebook = settings.getString("idFacebook", "");
            String apellidos = settings.getString("apellidos","");
            String email = settings.getString("email","");

            Log.d("etiqueta", String.valueOf(sesion));
            Log.d("etiqueta", name);

            // Se selecciona la cabecera del menú lateral de navegación.
            View headerView = navigationView.getHeaderView(0);

            // Se asigna el nombre completo del usuario al campo correspondiente.
            txt = (TextView) headerView.findViewById(R.id.nameUser);
            txt.setText(name + " " + apellidos);

            // Se asigna el email del usuario al campo correspondiente.
            txt = (TextView) headerView.findViewById(R.id.emailUser);
            txt.setText(email);

            // Se asigna la imagen de perfil del usuario al campo correspondiente.
            img = (CircleImageView) headerView.findViewById(R.id.imageUser);
            try {
                imageUrl = new URL("https://graph.facebook.com/" + idFacebook + "/picture?type=large");
                Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                img.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (metodo.equals("email")){

            // Se extrae la información almacenada en Shared Preferences.
            boolean sesion = settings.getBoolean("sesion", false);
            String name = settings.getString("nombre", "");
            String apellidos = settings.getString("apellidos","");
            String email = settings.getString("email","");

            Log.d("etiqueta",name);
            Log.d("etiqueta",apellidos);
            Log.d("etiqueta",email);

            // Se selecciona la cabecera del menú lateral de navegación.
            View headerView = navigationView.getHeaderView(0);

            // Se asigna el nombre completo del usuario al campo correspondiente.
            txt = (TextView) headerView.findViewById(R.id.nameUser);
            txt.setText(name + " " + apellidos);

            // Se asigna el email del usuario al campo correspondiente.
            txt = (TextView) headerView.findViewById(R.id.emailUser);
            txt.setText(email);

            // Se asigna la imagen de perfil del usuario al campo correspondiente.
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

        //Log.e("etiqueta", "id_usuario:"+settings.getInt("id", 0));
        new ClasePeticionRest.CogerObjetosInicio(this, settings.getInt("id", 0)+"").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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

    // Con este método se controlan los clicks en los items del menú lateral de navegación.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_tutorial_registrado) {       // Click en el tutorial.

            Intent intent = new Intent(this, Tutorial.class);
            startActivity(intent);

        } else if (id == R.id.nav_close) {          // Click en cerrar sesión.

            if (metodo.equals("google")) {
                // Se registró con Google.


                SharedPreferences info = getSharedPreferences("Config", 0);
                SharedPreferences.Editor editor = info.edit();
                editor.clear();

                // Se borra la sesión de Shared Preferences.
                editor.putBoolean("sesion", false);
                editor.commit();

                // Se carga el layout de invitado, es decir, sin funcionalidades de usuario.
                Intent intent = new Intent(this, Navigation_drawer.class);
                startActivity(intent);

                finish();

            } else if (metodo.equals("facebook")) {
                // Se registró con Facebook.

                LoginManager.getInstance().logOut();

                SharedPreferences info = getSharedPreferences("Config", 0);
                SharedPreferences.Editor editor = info.edit();
                editor.clear();

                // Se borra la sesión de Shared Preferences.
                editor.putBoolean("sesion", false);
                editor.commit();

                Log.d("variables","HE CERRADO LA SESIÓN CON FACEBOOK");
                // Se carga el layout de invitado, es decir, sin funcionalidades de usuario.
                Intent intent = new Intent(this, Navigation_drawer.class);
                startActivity(intent);

                finish();

            } else if (metodo.equals("email")){
                // Se registró manualmente.

                SharedPreferences info = getSharedPreferences("Config", 0);
                SharedPreferences.Editor editor = info.edit();
                editor.clear();

                // Se borra la sesión de Shared Preferences.
                editor.putBoolean("sesion", false);
                editor.commit();

                // Se carga el layout de invitado, es decir, sin funcionalidades de usuario.
                Intent intent = new Intent(this, Navigation_drawer.class);
                startActivity(intent);

                finish();
            }

        } else if (id == R.id.nav_contact2) {
            // Click en contacto.

            LayoutInflater inflater = (LayoutInflater) UsuarioRegistrado.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup, (ViewGroup) findViewById(R.id.popup_element));

            popUpWindow = new PopupWindow(layout, 1000, 700, true);
            popUpWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            cancelButton.setOnClickListener(cancel_button_click_listener);

        } else if (id==R.id.nav_help_us_2) {
            // Click en Envíanos tus comentarios.

            sendEmail();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public void openChat(View view) {

        Intent intent = new Intent(this, ChatListActivity.class);
        startActivity(intent);

    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

}
