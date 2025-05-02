package it.polimi.ingsw.psp23.protocol.request;

import it.polimi.ingsw.psp23.model.Game.Game;

public record RegisterNumPlayers(int number) implements Action{

    public void handle(int number) {
        Game.getInstance().setNumRequestedPlayers(number);
    }

}
