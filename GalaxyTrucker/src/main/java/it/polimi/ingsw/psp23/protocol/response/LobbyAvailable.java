package it.polimi.ingsw.psp23.protocol.response;

import it.polimi.ingsw.psp23.view.ViewAPI;

import java.util.List;


/**
 * Represents an event that provides information about the available lobbies.
 * The available lobbies are represented as a list of matches, where each match
 * is described by a list of integers containing relevant match data.
 *
 * This event can be handled by invoking the {@link #handle(ViewAPI)} method, which
 * uses a {@link ViewAPI} instance to display the available lobbies.
 *
 * The event also supports the Visitor pattern through the {@link #call(EventVisitor, ViewAPI)}
 * method, allowing specific handling based on the event type by the provided {@link EventVisitor}.
 *
 * @param matchesAvailable a list of matches, where each match is represented by a list of integers
 *                         containing associated match information
 */
public record LobbyAvailable(List<List<Integer>> matchesAvailable) implements Event {

    public void handle(ViewAPI viewAPI) {
        viewAPI.showAvailableLobbies(matchesAvailable);
    }

    @Override
    public <T> T call(EventVisitor<T> eventVisitor, ViewAPI viewAPI){
        return eventVisitor.visitForLobbyAvailable(this, viewAPI);
    }
}
