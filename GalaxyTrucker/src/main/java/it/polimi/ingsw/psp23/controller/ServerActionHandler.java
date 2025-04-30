package it.polimi.ingsw.psp23.controller;

import it.polimi.ingsw.psp23.protocol.request.*;


public class ServerActionHandler {

    String username;

    public ServerActionHandler(String username) {
        this.username = username;
    }

    public void handleAction(Action a) {

        a.call(new HandleActionVisitor(), username);

        /// TODO: raccolgo eccezioni lanciate dalla call

    }
}
