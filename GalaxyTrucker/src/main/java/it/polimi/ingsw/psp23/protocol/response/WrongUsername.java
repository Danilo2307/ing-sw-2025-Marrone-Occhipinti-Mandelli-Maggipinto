package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.messages.ActionMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.request.SetUsername;
import it.polimi.ingsw.psp23.view.TUI.IOManager;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.Scanner;

public record WrongUsername() implements Event {

    public void handle(ViewAPI view) {
        view.showWrongUsername();
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForWrongUsername(this, viewAPI);
    }

}
