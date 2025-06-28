package it.polimi.ingsw.psp23.network.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;


/**
 * Defines a remote interface for managing the registry of clients in a distributed system using RMI.
 * This interface provides methods for registering, unregistering, and retrieving clients or players,
 * as well as managing the associations between callback interfaces, usernames, and connection names.
 * All methods are designed to be accessed and invoked remotely, allowing coordination
 * in a multi-client environment.
 *
 * All methods are designed to handle remote communication errors by throwing {@link RemoteException}.
 */
public interface ClientRegistryInterface extends Remote {

    /**
     * Registers a client with the server using the specified username and callback interface.
     * The registration allows the server to communicate with the client through the provided callback.
     * This method is designed to be invoked remotely and may throw a {@link RemoteException} in case
     * of communication errors.
     *
     * @param username the unique identifier of the client to be registered
     * @param callback the remote callback interface implementation provided by the client
     * @throws RemoteException if a communication-related error occurs during the execution
     */
    void registerClient(String username, ClientCallbackInterface callback) throws RemoteException;

    /**
     * Unregisters a client from the server using the specified connection name.
     * Once unregistered, the server will no longer communicate with the client.
     *
     * @param nameConnection the unique connection identifier of the client to be unregistered
     * @throws RemoteException if a communication-related error occurs during the execution
     */
    void unregisterClient(String nameConnection) throws RemoteException;

    /**
     * Retrieves all currently registered clients as a collection of their callback interfaces.
     * This method provides access to the callback implementations that allow the server
     * to communicate with the clients remotely.
     *
     * @return a collection of {@link ClientCallbackInterface} instances representing the registered clients
     * @throws RemoteException if a communication-related error occurs during execution
     */
    Collection<ClientCallbackInterface> getAllClients() throws RemoteException;

    /**
     * Adds a player to the registry using their username and connection identifier.
     * The method associates the provided username with the corresponding connection name,
     * enabling player tracking and communication within the system.
     *
     * @param username the unique nickname or identifier of the player to be added
     * @param nameConnection the connection identifier associated with the player
     * @throws RemoteException if a communication-related error occurs during execution
     */
    void addPlayer(String username, String nameConnection) throws RemoteException;

    Collection<String> getAllPlayers() throws RemoteException;

    /** Restituisce il callback per un singolo username (o null) */
    ClientCallbackInterface getClient(String username) throws RemoteException;

    String getPlayerConnectionFromNickname(String nickname) throws RemoteException;

    String getPlayerNicknameFromConnection(String connection) throws RemoteException;

    String getNameConnectionFromCallback(ClientCallbackInterface c) throws RemoteException;
}
