package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * Represents an event triggered when an incorrect welding operation occurs.
 * This event is typically used to notify the {@link ViewAPI} and execute the
 * handling logic specific to this situation.
 *
 * The {@code IncorrectWelding} event uses the Visitor pattern, allowing various
 * implementations of {@link EventVisitor} to process it according to the
 * application's requirements.
 *
 * Responsibilities of this class include:
 * - Calling the appropriate method in the {@link ViewAPI} to handle an incorrect tile.
 * - Using the {@link EventVisitor} to enable further custom handling logic for this event type.
 */
public record IncorrectWelding() implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.incorrectTile();
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForIncorrectWelding(this, viewAPI);
    }
}
