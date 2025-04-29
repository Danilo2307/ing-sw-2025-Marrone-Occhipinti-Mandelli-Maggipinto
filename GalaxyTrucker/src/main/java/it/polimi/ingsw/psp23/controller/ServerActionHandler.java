package it.polimi.ingsw.psp23.controller;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.protocol.request.*;
import it.polimi.ingsw.psp23.protocol.response.ShipResponse;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;
import it.polimi.ingsw.psp23.protocol.response.TileResponse;
import it.polimi.ingsw.psp23.protocol.response.UncoveredListResponse;
import it.polimi.ingsw.psp23.exceptions.NoTileException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;

public class ServerActionHandler {

    String username;

    public ServerActionHandler(String username) {
        this.username = username;
    }

    public void handleAction(Action a) {

        a.call(new HandleActionVisitor(), username);

    }
}
