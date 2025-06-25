package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.visitor.BuyShipVisitor;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;

public record BuyShip() implements Action {

    /**
     * Handles the action to buy a ship in the "Abandoned Ship" phase of the game.
     *
     * @param username the username of the player performing the action
     * @throws InvalidActionException if the action is attempted outside the "Abandoned Ship" phase
     */
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        if(game.getGameStatus() != GameStatus.INIT_ABANDONEDSHIP){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Card currentCard = game.getCurrentCard();
        BuyShipVisitor buyShip = new BuyShipVisitor();
        currentCard.call(buyShip, username);
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForBuyShip(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }

}
