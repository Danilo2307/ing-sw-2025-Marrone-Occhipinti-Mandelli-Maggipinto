package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.exceptions.LobbyUnavailableException;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.Server;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.response.LobbyUnavailable;
import it.polimi.ingsw.psp23.view.ClientEventHandler;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;


/**
 * ClientRMI is a concrete implementation of the Client class that manages
 * client-server communication and registration using Java RMI (Remote Method Invocation).
 *
 * The class facilitates interactions with the game server and client registry
 * by leveraging RMI interfaces, handling remote communication and callbacks.
 * It also provides functionality to register players, send actions, and manage
 * the remote client lifecycle.
 *
 * The ClientRMI depends on:
 * - {@link ClientRMIHandlerInterface} for server interactions.
 * - {@link ClientRegistryInterface} for managing client registry and mappings.
 * - {@link ClientEventHandler} for handling events from the server.
 */
public class ClientRMI extends Client {
    private final ClientRMIHandlerInterface gameServer;
    private final ClientRegistryInterface clientRegistry;
    private String username;
    private final String nameConnection;
    private final Registry registry;

    /**
     * Initializes the ClientRMI class, establishes registry connections, registers the client
     * with the game server and client registry, and sets up a callback mechanism for handling
     * server events via RMI (Remote Method Invocation).
     *
     * @param host the hostname or IP address of the remote registry where the server is located
     * @param port the port number on which the remote registry is listening
     * @param username the username of the client connecting to the server
     * @param handler the event handler responsible for managing events received from the server
     * @throws Exception if there is an issue connecting to the registry, performing a lookup, or registering the client
     */
    public ClientRMI(String host, int port, String username, ClientEventHandler handler) throws Exception {
        this.username = username;

        registry = LocateRegistry.getRegistry(host, port);

        this.clientRegistry = (ClientRegistryInterface) registry.lookup("ClientRegistry");
        this.gameServer = (ClientRMIHandlerInterface) registry.lookup("GameServer");

        // 3. Esporta il callback del client
        ClientCallback callback = new ClientCallback(handler);
        // ClientCallbackInterface callbackStub = (ClientCallbackInterface) UnicastRemoteObject.exportObject(callback, 0);
        ClientCallbackInterface callbackStub = callback;

        // 4. Registra il callback nel ClientRegistry
        this.nameConnection = UUID.randomUUID().toString();
        try {
            gameServer.registerClient(username, nameConnection, callbackStub);
        }
        catch (LobbyUnavailableException e) {
            gameServer.sendToUser(nameConnection, new DirectMessage(new LobbyUnavailable()));
        }

    }

    /**
     * Retrieves the game server interface associated with this client. The game server
     * interface allows for interaction with the server in a distributed system using
     * RMI (Remote Method Invocation).
     *
     * @return the {@link ClientRMIHandlerInterface} that provides methods to interact
     *         with the remote game server for operations such as sending actions, managing
     *         game state, and facilitating client-server communication.
     */
    public ClientRMIHandlerInterface getGameServer() {
        return gameServer;
    }

    /**
     * Closes the RMI client by performing cleanup operations:
     * unbinding the client's game server from the RMI registry
     * to ensure proper resource release and server deregistration.
     *
     * @throws RemoteException if a communication-related error occurs during the unbinding process
     * @throws NotBoundException if the game server is not currently bound in the registry
     */
    public void close() throws RemoteException, NotBoundException {
        registry.unbind("GameServer");
        try {
            Server.getInstance().close();
        } catch (RuntimeException e) {

        }
    }

    /**
     * Sends an action to the remote game server. This method facilitates the delegation
     * of a player's action by invoking the remote method on the game server with the
     * client's username and connection details.
     *
     * @param action the action to be sent to the server, encapsulating the details of
     *               the operation or intent of the player to be executed on the game server
     * @throws RemoteException if a communication-related error occurs during the
     *                         remote method invocation
     */
    @Override
    public void sendAction(Action action) throws RemoteException {
        gameServer.sendAction(username, nameConnection, action);
    }

    /**
     * Sets the username of the client and registers the client with the client registry.
     * This method associates the provided username with the current connection and
     * enables the client to be tracked and identified in the system.
     *
     * @param username the unique identifier or nickname to be assigned to the client
     * @throws RemoteException if a communication-related error occurs during the execution
     */
    @Override
    public void setUsername(String username) throws RemoteException {
        this.username = username;
        clientRegistry.addPlayer(username, nameConnection);
    }

    /**
     * Retrieves the name of the connection associated with this client.
     *
     * @return the name of the connection as a String
     * @throws RemoteException if there is a communication-related error during the remote method invocation
     */
    @Override
    public String getNameConnection() throws RemoteException{
        return nameConnection;
    }

    /**
     * Determines if the client implements RMI (Remote Method Invocation) for
     * communication with the game server.
     *
     * @return true if the client utilizes RMI for its communication mechanism,
     *         false otherwise.
     */
    @Override
    public boolean isRmi(){
        return true;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
