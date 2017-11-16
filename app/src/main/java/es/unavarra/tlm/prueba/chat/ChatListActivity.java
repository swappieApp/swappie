package es.unavarra.tlm.prueba.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;

import es.unavarra.tlm.prueba.R;
import es.unavarra.tlm.prueba.chat.network.ChatListHandler;

public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // Se hace una petición asíncrona para obtener la lista de chats.
        AsyncHttpClient client = new AsyncHttpClient();

        // Se toma el ID del usuario.
        SharedPreferences settings = getSharedPreferences("Config", 0);
        int id = settings.getInt("id", 0);

        // Se hace la petición al servidor.
        client.get(this, "https://aux.swappie.tk/api/SwappieChat/public/index.php/api/chats/" + id + "", new ChatListHandler(this));

    }
}
