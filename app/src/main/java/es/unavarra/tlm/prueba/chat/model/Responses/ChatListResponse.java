package es.unavarra.tlm.prueba.chat.model.Responses;

import java.util.List;

import es.unavarra.tlm.prueba.chat.model.Objects.Chat;

public class ChatListResponse {

    private int count;
    private List<Chat> chats;

    public ChatListResponse(int count, List<Chat> chats) {
        this.count = count;
        this.chats = chats;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }
}
