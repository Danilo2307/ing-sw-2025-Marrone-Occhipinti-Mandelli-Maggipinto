package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.model.helpers.CannonShot;
import it.polimi.ingsw.psp23.view.ViewAPI;


/**
 * Represents an event indicating that a cannon shot has been fired and is incoming.
 * This event contains information about the position of the shot and its properties.
 * It is used to notify the appropriate components of the game system that a cannon shot
 * needs to be displayed or processed.
 *
 * This class implements the {@link Event} interface and is part of the event-driven
 * game architecture.
 *
 * @param coord       The coordinate where the cannon shot is incoming.
 * @param cannonShot  The details of the cannon shot, including its size and direction.
 */
public record CannonShotIncoming(int coord, CannonShot cannonShot) implements Event {

    public void handle(ViewAPI viewAPI){
        viewAPI.showCannonShot(coord, cannonShot);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForCannonShotIncoming(this, viewAPI);
    }

}
