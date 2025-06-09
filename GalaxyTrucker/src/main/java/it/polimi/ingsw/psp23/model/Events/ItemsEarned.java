package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class ItemsEarned extends Event {

    public ItemsEarned(GameStatus newStatus) {
        super(newStatus);
    }

    @Override
    public String describe(int gameId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bravo, ti sei meritato le merci!\n");
        return sb.toString();
    }

}
