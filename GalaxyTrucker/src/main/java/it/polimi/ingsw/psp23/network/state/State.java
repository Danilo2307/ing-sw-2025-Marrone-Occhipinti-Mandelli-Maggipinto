package it.polimi.ingsw.psp23.network.state;

import it.polimi.ingsw.psp23.network.ClientInfo;
import it.polimi.ingsw.psp23.network.messages.fromclient.*;

/**
 * Abstract class representing a generic user state on the server.
 * Defines default behavior for unsupported messages.
 */
public abstract class State {
    protected final ClientInfo user;
    protected final StateType stateType;

    public State(ClientInfo user, StateType stateType) {
        this.user = user;
        this.stateType = stateType;
    }

    public StateType getStateType() {
        return stateType;
    }

    /**
     * Responds with a generic error message when the received message is invalid
     * or not supported in the current state.
     */
    public void defaultSendUnkownError() {
        this.user.send(new UnknownErrorMessage());
    }

    // ===== Generic message handlers (usually allowed in all states) =====

    /**
     * Handles a request from the client to set or update the username.
     * @param usernameRequest the message containing the username details
     */
    public void handleUsernameSubmission(SetUsernameMsg usernameRequest) {
        this.defaultSendUnkownError();
    }

    /**
     * Handles the client's request to create a new lobby with specific settings.
     * @param createLobbyRequest the lobby configuration sent by the client
     */
    public void handleLobbyCreationInfo(CreateLobbyMsg createLobbyRequest) {
        this.defaultSendUnkownError();
    }

    /**
     * Processes a request from the client to join a selected lobby.
     * @param joinLobbyRequest the lobby join request from the client
     */
    public void handleLobbyJoinRequest(SelectLobbyMsg joinLobbyRequest) {
        this.defaultSendUnkownError();
    }

    /**
     * Responds to a client query to fetch the list of available lobbies.
     * @param lobbyListRequest the request for available lobbies
     */
    public void handleLobbyListQuery(RequestLobbyListMsg lobbyListRequest) {
        this.defaultSendUnkownError();
    }

    /**
     * Handles the client's request for detailed information about a lobby.
     * @param lobbyDetailsRequest the request for lobby details
     */
    public void handleLobbyDetailsRequest(LobbyDetailsMsg lobbyDetailsRequest) {
        this.defaultSendUnkownError();
    }

    /**
     * Executes the command from the client to start the lobby session.
     * @param lobbyStartRequest the start lobby command issued by the client
     */
    public void handleStartLobbyCommand(InitiateLobbyMsg lobbyStartRequest) {
        this.defaultSendUnkownError();
    }

    /**
     * Manages the client’s request to exit from the current lobby.
     * @param exitLobbyRequest the message indicating the client wants to leave the lobby
     */
    public void handleLobbyExit(ExitLobbyMsg exitLobbyRequest) {
        this.defaultSendUnkownError();
    }

    /**
     * Handles the client’s intention to leave the ongoing match.
     * @param exitMatchRequest the request from the client to exit the match
     */
    public void handleMatchExit(LeaveMatchMsg exitMatchRequest) {
        this.defaultSendUnkownError();
    }

    //TODO: aggiungere tutti i messaggi per gestire le decisioni dell'utente
}
