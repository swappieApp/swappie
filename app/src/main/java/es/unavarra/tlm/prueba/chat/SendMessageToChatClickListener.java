package es.unavarra.tlm.prueba.chat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.entity.StringEntity;
import es.unavarra.tlm.prueba.chat.model.Requests.SendNewMessageToChatRequest;
import es.unavarra.tlm.prueba.chat.network.SendNewMessageToChatHandler;

public class SendMessageToChatClickListener implements View.OnClickListener {

    private Activity activity;
    private EditText keyboard;
    private int id;             // ID del chat al que se quiere enviar el mensaje.

    public SendMessageToChatClickListener(Activity activity, EditText keyboard, int id) {
        this.activity = activity;
        this.keyboard = keyboard;
        this.id = id;
    }

    @Override
    public void onClick(View v) {

        // Se comprueba si hay algo escrito en el teclado.
        if (keyboard.getText().toString().equals("")) {
            Toast.makeText(activity, "No puedes enviar un mensaje en blanco", Toast.LENGTH_SHORT).show();
        } else {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date today = new Date();

            Gson gson =  new Gson();

            // Se toma el ID del usuario, que es el ID de quien escribe el mensaje.
            SharedPreferences settings = activity.getSharedPreferences("Config", 0);

            AsyncHttpClient client = new AsyncHttpClient();
            try {
                client.post(activity,
                        "https://aux.swappie.tk/api/SwappieChat/public/index.php/api/chat/" + id + "/message",
                        new StringEntity(gson.toJson(new SendNewMessageToChatRequest(settings.getInt("id", 0), keyboard.getText().toString(), dt.format(today)))),
                        "application/json", new SendNewMessageToChatHandler(activity, keyboard, id));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d("Main", "try-catch");
            }
        }

    }

}
