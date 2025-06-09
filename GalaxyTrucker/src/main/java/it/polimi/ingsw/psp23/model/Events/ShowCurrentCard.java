package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.cards.Card;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class ShowCurrentCard extends Event {

    Card currentCard;

    public ShowCurrentCard(GameStatus newStatus, Card card) {
        super(newStatus);
        this.currentCard = card;
    }

    @Override
    public String describe(int gameId) {
        StringBuilder sb = new StringBuilder();
        sb.append(currentCard.toString()).append("\n");
        return sb.toString();
    }

}
