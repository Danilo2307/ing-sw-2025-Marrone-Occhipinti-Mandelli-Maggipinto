package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class CosmicCreditsEarned extends Event {

    public CosmicCreditsEarned(GameStatus newStatus) {
        super(newStatus);
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bravo, ti sei meritato i crediti cosmici!\n");
        return sb.toString();
    }
}
