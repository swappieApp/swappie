package es.unavarra.tlm.prueba.chat.network;

import android.app.Activity;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.unavarra.tlm.prueba.R;
import es.unavarra.tlm.prueba.chat.ChatAdapter;
import es.unavarra.tlm.prueba.chat.ChatListItemClickListener;
import es.unavarra.tlm.prueba.chat.model.Objects.Chat;
import es.unavarra.tlm.prueba.chat.model.Responses.ChatListResponse;

public class ChatListHandler extends AsyncHttpResponseHandler {

    private Activity activity;
    private ListView chatList;

    public ChatListHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();

        // Respuesta del servidor.
        ChatListResponse response = gson.fromJson(new String(responseBody), ChatListResponse.class);

        // Se selecciona la ListView para la lista de chats.
        chatList = (ListView) activity.findViewById(R.id.chat_list);

        // ArrayList que contiene los chats.
        List<Chat> chats = new ArrayList<>();

        // Se llena el ArrayList anterior con los chats enviados por el servidor.
        for (int i = 0; i < response.getCount(); i++) {
            chats.add(response.getChats().get(i));
        }

        // Se asigna el adaptador a la ListView.
        chatList.setAdapter(new ChatAdapter(activity, chats));

        // Listener para abrir cada chat.
        chatList.setOnItemClickListener(new ChatListItemClickListener(activity, chats));

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        error.printStackTrace(System.out);
    }

}
