package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.ViewAPI;

/**
 * Represents an event that encapsulates the action of drawing a new card in the system.
 * This class is part of the event-based system, implementing the {@link Event} interface.
 *
 * The {@code NewCardDrawn} record holds the card's unique identifier and description, enabling the
 * application to process and display this information on the user interface or to manage it through
 * the event visitor pattern.
 *
 * This class defines the following key methods:
 * - {@code handle(ViewAPI)}: Invokes the corresponding {@link ViewAPI} method to visually represent
 *   the card that was drawn, using the provided card ID and description.
 * - {@code call(EventVisitor, ViewAPI)}: Implements the visitor pattern by delegating processing
 *   of this specific event type to the {@link EventVisitor}'s {@code visitForNewCardDrawn} method.
 *
 * Instances of this class are immutable due to its {@code record} implementation, ensuring thread safety
 * when used in concurrent environments.
 *
 * Fields:
 * - {@code id}: The unique identifier of the newly drawn card.
 * - {@code description}: The description or details of the newly drawn card.
 */
public record NewCardDrawn(int id, String descrption) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showNewCard(id, descrption);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForNewCardDrawn(this, viewAPI);
    }

}
