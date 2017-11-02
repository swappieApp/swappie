package es.unavarra.tlm.prueba;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.share.widget.ShareDialog;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.id.button1;
import static es.unavarra.tlm.prueba.R.string.email;

public class Invitado extends AppCompatActivity {

    URL imageUrl;
    //SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitado);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //prefs = getSharedPreferences("es.unavarra.tlm", Context.MODE_PRIVATE);
        //String name = prefs.getString("es.tlm.unavarra.nombre","");
        //String email= prefs.getString("es.unavarra.tlm.email","hola");
        //String id = prefs.getString("es.unavarra.tlm.id","");
        //String birthday = prefs.getString("es.unavarra.tlm.birthday","");

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String id = getIntent().getStringExtra("id");




        ImageView img = (ImageView) findViewById(R.id.imageView2);

            try {
                imageUrl = new URL("https://graph.facebook.com/"+id+"/picture?type=large");
                Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                img.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        TextView text = (TextView)findViewById(R.id.email_fb);
        text.setText("Bienvenido "+email);

        }



    public void logout(View view) {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    }


