package it.polimi.ingsw.psp23.network.messages;

public interface MessageVisitor<T> {

    public T visitForDirectMessage(DirectMessage directMessage);

    public T visitForBroadcastMessage(BroadcastMessage broadcastMessage);

    public T visitForActionMessage(ActionMessage actionMessage);

}
