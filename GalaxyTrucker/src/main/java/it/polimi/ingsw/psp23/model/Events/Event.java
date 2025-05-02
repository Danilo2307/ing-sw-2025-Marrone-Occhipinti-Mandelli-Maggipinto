package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public abstract class Event {
    private final GameStatus newStatus;

    protected Event(GameStatus newStatus) {
        this.newStatus = newStatus;
    }

    public GameStatus getNewStatus() { return newStatus; }

    /** Metodo astratto per generare la descrizione rivolta all’UI */
    public abstract String describe();
}
