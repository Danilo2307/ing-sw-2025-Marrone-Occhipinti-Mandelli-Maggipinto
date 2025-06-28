package it.polimi.ingsw.psp23.network.messages;


public interface MessageInterface {

    public <T> T call(MessageVisitor<T> messageVisitor);

}
