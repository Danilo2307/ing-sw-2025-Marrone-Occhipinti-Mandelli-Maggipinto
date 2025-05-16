package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.ActiveShieldVisitor;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

public record ActivateShield(int sx, int sy, int bx, int by) implements Action{

    public void handle(String username){
        Game game = Game.getInstance();
        Board truck = game.getCurrentPlayer().getTruck();
        Component[][] nave = truck.getShip();
        int shieldIndex = truck.getShields().indexOf(nave[sx][sy]);
        if(game.getGameStatus() != GameStatus.ENDTHIRD_COMBATZONE && game.getGameStatus() != GameStatus.INIT_METEORSWARM && game.getGameStatus() != GameStatus.END_PIRATES){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Card currentCard = game.getCurrentCard();
        if(shieldIndex == -1){
            Server.getInstance().sendMessage(username, new DirectMessage(new StringResponse("Non hai selezionato uno scudo\n")));
        }
        else {
            if (truck.getShields().get(shieldIndex).isActive()) {
                Server.getInstance().sendMessage(username, new DirectMessage(new StringResponse("Lo scudo è già stato attivato!\n")));
            } else {
                ActiveShieldVisitor activeShield = new ActiveShieldVisitor();
                currentCard.call(activeShield, username, sx, sy);
                game.getPlayerFromNickname(username).getTruck().reduceBatteries(bx, by, 1);
            }
        }
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForActivateShield(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
