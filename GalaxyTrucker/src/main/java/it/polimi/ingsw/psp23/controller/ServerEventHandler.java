package it.polimi.ingsw.psp23.controller;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.events.*;
import it.polimi.ingsw.psp23.events.server.ShipResponse;
import it.polimi.ingsw.psp23.events.server.TileResponse;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.components.Component;

public class ServerEventHandler {

    public void handleEvent(Event e) {
        Game game = Game.getInstance();
        switch (e) {
            case DrawFromHeap draw -> {
                Player p = game.getPlayerFromNickname(draw.username());
                Component drawn = p.chooseTileFromHeap();
                /// non includo username nell'evento perchè la scelta del canale di comunicazione preciso per quel client lo gestirò prima
                connection.sendToClient(new TileResponse(drawn));
            }
            case DrawFromFaceUp draw -> {
                Player p = game.getPlayerFromNickname(draw.username());
                Component drawn = p.chooseCardUncovered(draw.x());
                connection.sendToCLient(new TileResponse(drawn));
            }
            case AddTile add -> {
                Player p = game.getPlayerFromNickname(add.username());
                p.addTile(add.x(), add.y());
            }
            case ReleaseTile release -> {
                Player p = game.getPlayerFromNickname(release.username());
                p.discardComponent();
            }
            case RotateTile rotate -> {
                Player p = game.getPlayerFromNickname(rotate.username());
                p.rotateTileInHand();
            }
            case TurnHourglass turn -> {
                /// ASK DANILO METODO GIRA CLESSIDRA
            }
            case RequestShip requestShip -> {
                Player p = game.getPlayerFromNickname(requestShip.username());
                Component[][] ship = p.getTruck().getShip();
                connection.sendToClient(new ShipResponse(ship));
            }
            case RequestTileInfo targetTile -> {
                Player p = game.getPlayerFromNickname(targetTile.username());
                Component target = p.getTruck().getTile(targetTile.x(), targetTile.y());
                connection.sendToClient(new TileResponse(target));
            }
        }
    }
}
