package es.unavarra.tlm.prueba.chat;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;

import es.unavarra.tlm.prueba.chat.model.Objects.Chat;

public class ChatListItemClickListener implements AdapterView.OnItemClickListener {

    private Activity activity;
    private List<Chat> chats;

    public ChatListItemClickListener(Activity activity, List<Chat> chats) {
        this.activity = activity;
        this.chats = chats;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this.activity, ChatActivity.class);
        intent.putExtra("chatId", chats.get(position).getId());
        this.activity.startActivity(intent);
    }

}
