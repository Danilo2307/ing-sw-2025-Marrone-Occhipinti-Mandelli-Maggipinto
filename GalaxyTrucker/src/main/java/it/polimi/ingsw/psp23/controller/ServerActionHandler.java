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
        Game game = Game.getInstance();

        switch (a) {
            case DrawFromHeap draw -> {
                Player p = game.getPlayerFromNickname(username);
                Component drawn = p.chooseTileFromHeap();
                /// non includo username nell'evento perchè la scelta del canale di comunicazione preciso per quel client lo gestirò prima
                DirectMessage dm = new DirectMessage(new TileResponse(drawn));
                Server.getInstance().sendMessage(username, dm);
            }
            case DrawFromFaceUp draw -> {
                Player p = game.getPlayerFromNickname(username);
                try {
                    Component drawn = p.chooseCardUncovered(draw.x(), draw.version());
                    DirectMessage dm = new DirectMessage(new TileResponse(drawn));
                    Server.getInstance().sendMessage(username, dm);
                }
                catch (NoTileException | IndexOutOfBoundsException exception) {
                    DirectMessage dm = new DirectMessage(new StringResponse(exception.getMessage()));
                    Server.getInstance().sendMessage(username, dm);
                }
            }
            case RequestUncovered uncovered -> {
                DirectMessage dm = new DirectMessage(new UncoveredListResponse(game.getUncovered(), game.getLastUncoveredVersion()));
                Server.getInstance().sendMessage(username, dm);
            }
            case AddTile add -> {
                Player p = game.getPlayerFromNickname(username);
                p.addTile(add.x(), add.y());
            }
            case ReleaseTile release -> {
                Player p = game.getPlayerFromNickname(username);
                p.discardComponent();
            }
            case RotateTile rotate -> {
                Player p = game.getPlayerFromNickname(username);
                p.rotateTileInHand();
            }
            case TurnHourglass turn -> {
                /// ASK DANILO METODO GIRA CLESSIDRA
            }
            case RequestShip requestShip -> {
                Player p = game.getPlayerFromNickname(username);
                Component[][] ship = p.getTruck().getShip();
                DirectMessage dm = new DirectMessage(new ShipResponse(ship));
                Server.getInstance().sendMessage(username, dm);
            }
            case RequestTileInfo targetTile -> {
                Player p = game.getPlayerFromNickname(username);
                Component target = p.getTruck().getTile(targetTile.x(), targetTile.y());
                DirectMessage dm = new DirectMessage(new TileResponse(target));
                Server.getInstance().sendMessage(username, dm);
            }
            default -> System.out.println("Unknown action");
        }
    }
}
