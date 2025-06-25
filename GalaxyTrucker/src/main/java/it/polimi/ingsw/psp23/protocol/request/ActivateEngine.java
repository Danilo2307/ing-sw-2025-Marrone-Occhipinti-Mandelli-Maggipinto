package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.InvalidActionException;
import it.polimi.ingsw.psp23.model.Game.Board;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.cards.visitor.ActiveEngineVisitor;
import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.response.StringResponse;

public record ActivateEngine(int ex, int ey, int bx, int by) implements Action {


    /**
     * Handles the activation of a double engine in the player's ship during specific game phases.
     *
     * The method checks if the action can be executed based on the current game phase, verifies
     * the validity of the selected engine, and executes the activation logic if requirements are met.
     * If the engine is already active or no valid engine is selected, the method sends an error message
     * to the player.
     *
     * @param username the username of the player attempting to activate an engine
     * @throws InvalidActionException if the game phase does not allow engine activation
     */
    public void handle(String username){
        Game game = UsersConnected.getInstance().getGameFromUsername(username);
        Board truck = game.getCurrentPlayer().getTruck();
        Component[][] nave = truck.getShip();
        int engineIndex = truck.getEngines().indexOf(nave[ex][ey]);
        if(game.getGameStatus() != GameStatus.SECOND_COMBATZONE && game.getGameStatus() != GameStatus.THIRD_COMBATZONE && game.getGameStatus() != GameStatus.INIT_OPENSPACE){
            throw new InvalidActionException("Non puoi eseguire questa azione in questo momento");
        }
        Card currentCard = game.getCurrentCard();
        if(engineIndex == -1){
            Server.getInstance().sendMessage(username, new DirectMessage(new StringResponse("Non hai selezionato un motore\n")));
        }
        else {
            if (truck.getEngines().get(engineIndex).isActive()) {
                Server.getInstance().sendMessage(username, new DirectMessage(new StringResponse("Il motore è già stato attivato!\n")));
            } else {
                ActiveEngineVisitor activeEngine = new ActiveEngineVisitor();
                currentCard.call(activeEngine, username, ex, ey);
                game.getPlayerFromNickname(username).getTruck().reduceBatteries(bx, by, 1);
            }
        }
    }


    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForActivateEngine(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


}

