package es.unavarra.tlm.prueba.chat.model.Responses;

import es.unavarra.tlm.prueba.chat.model.Objects.Description;

public class SendNewMessageToChatResponse {

    private Description description;

    public SendNewMessageToChatResponse(Description description) {
        this.description = description;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

}
