package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.controller.ServerActionHandler;
import it.polimi.ingsw.psp23.network.messages.GetActionVisitor;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.network.messages.Message;

/**
 * The ClientHandler class is responsible for managing the server-side representation of a connected client.
 * It acts as a "virtual" client within the server, handling communication and actions initiated by the client.
 * Each instance of ClientHandler is associated with a single client connection.
 */
public class ClientHandler {
    private StartListeningForClientThread clientThread = null;
    private final String connectionID;
    private String username;
    private ServerActionHandler serverActionHandler;

    ClientHandler(String connectionID) {
        this.connectionID = connectionID;
        clientThread = new StartListeningForClientThread(connectionID);
        clientThread.start();
        username = Server.getInstance().getUsernameForConnection(connectionID);
        serverActionHandler = new ServerActionHandler(username);
    }

    /**
     * Sends the specified message to the associated client through the server.
     *
     * @param message the message to be sent to the client; must be an instance of the {@code Message} class
     */
    public void send(Message message) {
        Server.getInstance().sendMessage(message, connectionID);
        System.out.println("Il ClientHandler ha inviato il messaggio: " + message + " da parte della connessione: " + connectionID);
    }

    /**
     * Processes an incoming message, converts it into a corresponding action,
     * and delegates the action to the serverActionHandler for execution.
     *
     * @param message the incoming message from the client that encapsulates
     *                the action or request to be processed
     */
    public void handleMessage(Message message) {
        Action action = message.call(new GetActionVisitor());
        serverActionHandler.handleAction(action);
    }

    /**
     * Retrieves the unique connection ID associated with this client handler.
     *
     * @return the unique connection ID representing the client connection.
     */
    public String getConnectionID() {
        return connectionID;
    }

    /**
     * Sets the username of the client associated with this ClientHandler.
     *
     * @param username the username to associate with this client
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the username associated with this client handler.
     *
     * @return the username of the client connected to this handler
     */
    public String getUsername() {
        return username;
    }



}
