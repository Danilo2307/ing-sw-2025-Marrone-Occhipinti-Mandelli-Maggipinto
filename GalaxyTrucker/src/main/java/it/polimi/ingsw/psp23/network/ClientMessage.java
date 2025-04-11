package it.polimi.ingsw.psp23.network;

import it.polimi.ingsw.psp23.model.enumeration.ClientMessageType;

import java.util.Arrays;

public class ClientMessage {
    private final ClientMessageType type;
    private final Object[] messageContent;

    public ClientMessage(ClientMessageType type, Object ... messageContent) {
        this.type = type;
        this.messageContent = messageContent;
    }

    public ClientMessageType getType() {
        return type;
    }

    public Object[] getMessageContent() {
        return Arrays.copyOf(messageContent, messageContent.length);
    }
}
