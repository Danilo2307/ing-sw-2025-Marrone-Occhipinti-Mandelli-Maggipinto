package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.exceptions.LobbyUnavailableException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;

import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

// Questa è l'azione che invia il client per comunicare se vuole creare una nuova partita o partecipare ad una partita esistente

public record UserDecision(int choice) implements Action {

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
