package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.visitor.ActiveShieldVisitor;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

public record ActivateShield(int sx, int sy, int bx, int by) implements Action{


    /**
     * Handles the activation of a shield for the current player in the game.
     * This method first verifies the validity of the action based on the current game state,
     * checks if the specified shield exists and is not already activated, and then activates the shield
     * while adjusting the player's battery level accordingly.
     * If the game is not in a state where the action is allowed, an exception is thrown.
     * If the shield is already active or not selected, appropriate messages are sent to the user.
     *
     * @param username the username of the player attempting to activate the shield
     * @throws InvalidActionException if the action is attempted at an invalid game state
     */
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
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
