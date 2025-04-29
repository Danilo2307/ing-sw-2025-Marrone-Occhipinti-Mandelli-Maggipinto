package it.polimi.ingsw.psp23.protocol.request;

import java.io.Serializable;

public interface Action extends Serializable {

    public <T> T call(ActionVisitor<T> actionVisitor, String username);

}
