package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.protocol.request.ActionVisitor;

public interface MessageInterface {

    public <T> T call(MessageVisitor<T> messageVisitor);

}
