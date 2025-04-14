package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.network.client.*;
import it.polimi.ingsw.psp23.network.common.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote interface that exposes the available operations of the UserStub for RMI clients.
 */
public interface RMIServerInterface extends Remote {

    /**
     * Registers the client's remote callback interface used by the server to send asynchronous events.
     * This enables the server to notify the client in real-time via push-based communication.
     *
     * @param clientPushCallback the client's remote implementation of the push interface
     * @throws RemoteException if a communication error occurs during registration
     */
    void registerClientPushCallback(PushServiceOfClientRMI clientPushCallback) throws RemoteException;

    /**
     * Sends a heartbeat signal to notify that the client is still active.
     *
     * @param heartbeat the heartbeat object to send
     * @throws RemoteException if a remote communication error occurs
     */
    void sendHeartbeat(Heartbeat heartbeat) throws RemoteException;

    /////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * Username setup phase
     */

    /**
     * Assigns a username to the current user session.
     *
     * @param username the desired username
     * @return true if the assignment was successful, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    Boolean setUsernameRMI(String username) throws RemoteException;

    /////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * Lobby creation and selection phase
     */

    /**
     * Creates a new game lobby with the given parameters.
     *
     * @param name the lobby's name
     * @param maxPlayers the maximum allowed number of players
     * @return true if the lobby was successfully created, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    Boolean createLobbyRMI(String name, Integer maxPlayers) throws RemoteException;

    /**
     * Attempts to join a lobby using its unique identifier.
     *
     * @param lobbyUUID the UUID of the target lobby
     * @return true if the lobby was successfully joined, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    Boolean joinLobbyRMI(String lobbyUUID) throws RemoteException;

//    /**
//     * Retrieves the list of available lobbies that can be joined.
//     *
//     * @return a list of available lobbies; may be null depending on the controller specifications
//     * @throws RemoteException if a remote communication error occurs
//     */
// TODO per extra:  List<ListOfLobbyToJoinMessage.LobbyInfo> getListOfLobbyToJoinRMI() throws RemoteException;

    /////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * Lobby interaction phase
     */

//    /**
//     * Retrieves information about the current lobby.
//     *
//     * @return the lobby's details, or null if not available (see controller docs)
//     * @throws RemoteException if a remote communication error occurs
//     */
// TODO per conoscere dettagli di una partita già creata  LobbyInfo getLobbyInfoRMI() throws RemoteException;

    /**
     * Starts the game in the current lobby.
     *
     * @return true if the lobby was successfully started, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    Boolean startLobbyRMI() throws RemoteException;

    /**
     * Exits the user from the current lobby session.
     *
     * @return true if the user left the lobby successfully, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    Boolean exitLobbyRMI() throws RemoteException;
}
