package it.polimi.ingsw.psp23.network.messages;

import it.polimi.ingsw.psp23.protocol.request.Action;


/**
 * The {@code GetActionVisitor} is an implementation of the {@link MessageVisitor} interface, designed
 * to retrieve an {@link Action} object from messages implementing the {@link Message} class hierarchy.
 *
 * This class adheres to the Visitor design pattern, providing specialized visit methods
 * for processing {@link DirectMessage}, {@link BroadcastMessage}, and {@link ActionMessage}.
 *
 * For {@link ActionMessage} objects, the visitor retrieves the contained {@link Action}
 * via the {@link ActionMessage#getAction()} method.
 * For other message types, this visitor currently returns {@code null}.
 */
public class GetActionVisitor implements MessageVisitor<Action> {

    public Action visitForDirectMessage(DirectMessage directMessage){
        return null;
    }

    public Action visitForBroadcastMessage(BroadcastMessage broadcastMessage){
        return null;
    }

    public Action visitForActionMessage(ActionMessage actionMessage){
        return actionMessage.getAction();
    }

}
