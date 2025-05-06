package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

import java.io.Serializable;

public interface Event extends Serializable {

    public <T> T call(EventVisitor<T> eventVisitor, TuiApplication tuiApplication);

    public String toString();

}
