// Questa classe rappresenta il player lato client
package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.rmi.ClientRMIHandlerInterface;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.request.SetUsername;
import it.polimi.ingsw.psp23.network.messages.ActionMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.view.ClientEventHandler;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;


/**
 * Represents a client-side socket connection to a server. The {@code ClientSocket}
 * class is responsible for establishing a connection to the server, managing the
 * connection, sending messages, and handling communication via a helper thread.
 * It extends the {@code Client} abstract class.
 *
 * The class uses the {@code SocketHandler} to handle reading and writing messages
 * through the socket and a {@code StartListeningForServerThread} to continuously
 * listen for and process messages from the server.
 */
public class ClientSocket extends Client {

    SocketHandler socketHandler;
    StartListeningForServerThread startListeningForServerThread;


    /**
     * Constructs a new ClientSocket that establishes a connection to the server
     * and initializes the necessary components for communication and handling events.
     *
     * @param serverIP the IP address of the server to connect to
     * @param port the port number on the server to connect to
     * @param username the username of the client connecting to the server
     * @param clientEventHandler the event handler responsible for processing events received from the server
     * @throws RuntimeException if an error occurs while creating the socket connection
     */
    // Nel costruttore verrà eseguita la connessione tra client e server
    public ClientSocket(String serverIP, int port, String username, ClientEventHandler clientEventHandler) {



        try {
            Socket socket = new Socket(serverIP, port);
            socketHandler = new SocketHandler(socket);

            //message = socketHandler.readMessage();
            startListeningForServerThread = new StartListeningForServerThread(socketHandler, clientEventHandler, this);

        }
        catch (IOException e) {
            throw new RuntimeException("Errore class Client constructor: " + e.getMessage());
        }

    }

    /**
     * Starts the thread responsible for continuously listening for messages from the server.
     * This method initiates the {@code StartListeningForServerThread} instance,
     * which handles incoming server communication.
     * It is essential to call this method after successfully creating the connection
     * to ensure the client begins receiving and processing server messages.
     */
    public void avvia(){
        startListeningForServerThread.start();
    }

    /**
     * Sends a message to the server using the associated socket handler.
     *
     * @param message the {@code Message} object that represents the content being sent to the server
     */
    // Metodo incaricato dell'inoltro dei messaggi
    public void sendMessage(Message message) {
        socketHandler.sendMessage(message);
    }

    /**
     * Reads a message from the server using the associated socket handler.
     * This method blocks until a message is received or a timeout occurs.
     *
     * @return the {@code Message} object received from the server
     * @throws SocketTimeoutException if a timeout occurs while waiting for a message
     */
    public Message readMessage() throws SocketTimeoutException {
        return socketHandler.readMessage();
    }

    /**
     * Processes the provided message received from the server or client.
     *
     * @param message the {@code Message} object representing the content to be handled;
     *                it contains information about the action or communication that needs to be processed
     */
    public void handleMessage(Message message) {

    }

    /**
     * Closes the client's socket connection and stops the server listening thread.
     *
     * This method ensures that the associated socket handler is closed explicitly
     * and the thread responsible for server communication is terminated.
     */
    // Avremmo potuto anche usare i try-with-resources negli altri blocchi, ma fare chiusure esplicite rende più robusto
    // il codice
    public void close() {
        socketHandler.close();
        startListeningForServerThread.stopThread();
    }

    /**
     * Sets the username for the client and sends the corresponding action to the server.
     * This method creates a {@code SetUsername} action containing the specified username,
     * wraps it in an {@code ActionMessage}, and sends it via the client's socket handler.
     *
     * @param username the username to be assigned to the client and communicated to the server
     */
    public void setUsername(String username) {
        Message message = new ActionMessage(new SetUsername(username));
        socketHandler.sendMessage(message);
    }

    /**
     * Retrieves the instance of {@code SocketHandler} associated with this client.
     *
     * @return the {@code SocketHandler} object responsible for managing socket communication
     */
    public SocketHandler getSocketHandler() {
        return socketHandler;
    }

    /**
     * Retrieves the socket associated with the client's connection.
     *
     * @return the {@code Socket} object managed by the socket handler
     */
    public Socket getSocket() {
        return socketHandler.socket;
    }

    /**
     * Stops the thread responsible for listening to incoming messages from the server.
     *
     * This method halts the {@code StartListeningForServerThread} instance by invoking
     * its {@code stopThread} method. It ensures that the client no longer processes
     * server communications. This can be useful when the client is shutting down or
     * no longer needs to monitor messages from the server.
     */
    public void stopListeningForServerThread() {
        startListeningForServerThread.stopThread();
    }

    /**
     * Sends an {@link Action} to the server by encapsulating it in an {@link ActionMessage},
     * which is then forwarded using the {@code sendMessage} method.
     *
     * @param action the {@link Action} object to be sent to the server; it represents
     *               an operation or command that is to be processed remotely
     */
    @Override
    public void sendAction(Action action){
        ActionMessage actionMessage = new ActionMessage(action);
        sendMessage(actionMessage);
    }

    /**
     * Retrieves the username associated with the current client socket.
     *
     * @return the username as a {@code String}, or {@code null} if no username is set
     */
    @Override
    public String getUsername() {
        return socketHandler.getUsername();
    }

}
