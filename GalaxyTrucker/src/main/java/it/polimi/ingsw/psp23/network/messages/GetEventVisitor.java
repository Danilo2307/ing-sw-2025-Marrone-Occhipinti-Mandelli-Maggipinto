package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.protocol.response.Event;



/**
 * Implements the {@link MessageVisitor} interface and provides concrete implementations for handling
 * different types of messages in order to extract their encapsulated {@link Event}.
 *
 * This class defines the Visitor behavior for processing {@link DirectMessage},
 * {@link BroadcastMessage}, and {@link ActionMessage} instances.
 *
 * When visiting {@link DirectMessage} and {@link BroadcastMessage}, the corresponding {@link Event}
 * encapsulated within the message is returned. For {@link ActionMessage}, {@code null} is returned
 * as it does not encapsulate an {@link Event}.
 */
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
