package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;

import java.io.Serializable;
import java.util.List;

public interface Action extends Serializable {

    public <T> T call(ActionVisitor<T> actionVisitor, String username);

    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar);

    @Override
    public String toString();

    public List<DirectMessage> getDm();

    public List<BroadcastMessage> getBm();

}
