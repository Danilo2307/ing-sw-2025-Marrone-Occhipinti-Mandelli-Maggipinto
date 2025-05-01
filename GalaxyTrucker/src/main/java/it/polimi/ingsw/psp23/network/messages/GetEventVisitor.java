package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.response.Event;

public class GetEventVisitor implements MessageVisitor<Event> {

    public Event visitForDirectMessage(DirectMessage directMessage){
        return directMessage.getEvent();
    }

    public Event visitForBroadcastMessage(BroadcastMessage broadcastMessage){
        return broadcastMessage.getEvent();
    }

    public Event visitForActionMessage(ActionMessage actionMessage){
        return null;
    }
}
