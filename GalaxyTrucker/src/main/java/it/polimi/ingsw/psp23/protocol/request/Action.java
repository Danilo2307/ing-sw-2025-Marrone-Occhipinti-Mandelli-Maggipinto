package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.protocol.response.EventVisitor;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

import java.io.Serializable;

public interface Action extends Serializable {

    public <T> T call(ActionVisitor<T> actionVisitor, String username);

}
