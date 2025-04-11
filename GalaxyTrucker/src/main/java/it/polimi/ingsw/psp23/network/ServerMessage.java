package it.polimi.ingsw.psp23.network;

import it.polimi.ingsw.psp23.model.enumeration.ServerMessageType;

import java.util.Arrays;

public class ServerMessage {
    private final ServerMessageType type;
    private final Object[] messageContent;

    public ServerMessage(ServerMessageType type, Object ... messageContent) {
        this.type = type;
        this.messageContent = messageContent;
    }

    public ServerMessageType getType() {
        return type;
    }

    public Object[] getMessageContent() {
        return Arrays.copyOf(messageContent, messageContent.length);
    }
}
