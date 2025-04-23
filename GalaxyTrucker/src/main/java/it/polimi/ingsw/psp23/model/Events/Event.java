package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.model.enumeration.EventType;

//classe che definisce la struttura dell'evento
public class Event {

    private final EventType type;
    private final Object data;

    public Event(EventType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public EventType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
