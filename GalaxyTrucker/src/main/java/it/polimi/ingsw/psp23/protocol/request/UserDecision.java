package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.LobbyUnavailableException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.Server;

import java.util.ArrayList;
import java.util.List;


public record UserDecision(int choice) implements Action {

    /**
     * Handles the action initiated by the client to either create a new game or join an existing one.
     *
     * @param username the username of the client making the decision
     * @throws LobbyUnavailableException if the provided choice is invalid or if no available lobbies match the choice
     */
    public void handle(String username){

        List<Integer> idsAvailable = new ArrayList<>();

        for(Game g : Server.getInstance().getGames()){
            if(g.getGameStatus() == GameStatus.Setup) {
                idsAvailable.add(g.getId());
            }
        }

        if(choice < 0  || !idsAvailable.contains(choice)){
            throw new LobbyUnavailableException("Invalid choice");
        }
    }

    @Override
    public <T> T call(ActionVisitor<T> actionVisitor, String username){
        return actionVisitor.visitForUserDecision(this, username);
    }

    @Override
    public <T> T call(ActionVisitorSinglePar<T> actionVisitorSinglePar){
        return null;
    }


    public int getChoice() {
        return choice;
    }

}
