package it.polimi.ingsw.psp23.network.messages.fromclient;


import it.polimi.ingsw.psp23.network.messages.Message;

/**
 * Client request to establish a new game lobby with specified parameters.
 */
public final class CreateLobbyMsg extends Message {
    private final String lobbyTitle;
    private final int playerLimit;

    /**
     * @param lobbyTitle  the desired name for the new lobby
     * @param playerLimit the maximum number of players allowed
     */
    public CreateLobbyMsg(String lobbyTitle, int playerLimit) {
        this.lobbyTitle = lobbyTitle;
        this.playerLimit = playerLimit;
    }

    /**
     * @return the lobby's title as requested by the client
     */
    public String getLobbyTitle() {
        return lobbyTitle;
    }

    /**
     * @return the maximum player count for the lobby
     */
    public int getPlayerLimit() {
        return playerLimit;
    }
}
