package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.network.Server;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.protocol.response.MatchAbandoned;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;


/**
 * The SocketHandler class is responsible for managing socket communication
 * between a client and a server over Object Streams. It provides methods
 * for reading and sending messages, and ensures thread-safe operations
 * through synchronization mechanisms.
 */
public class SocketHandler {

    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    // Gli oggetti di lock mi permettono di inviare/ricevere un messaggio alla volta
    final Object lockLettura = new Object();

    final Object lockScrittura = new Object();

    String username = null;

    /**
     * Creates a new instance of the SocketHandler class to manage communication
     * with the specified socket. This involves setting up ObjectOutputStream
     * and ObjectInputStream for data transmission and reception.
     *
     * @param socket the socket to be used for communication. The provided socket
     *               must already be connected to ensure proper functionality.
     *               It enables data input and output streams for message handling.
     * @throws RuntimeException if there are issues during the initialization
     *                          of the ObjectOutputStream or ObjectInputStream.
     */
    public SocketHandler(Socket socket){

        this.socket = socket;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Problema nell'istanziazione di 'out' in SocketHandler " + e.getMessage());
        }

        try {
            // Attenzione perchè quest'istruzione mette tutto in pausa fino a quando non gli arriva l'header
            // per la connessione da parte del client tra l'outputstream(facendo "flush") e questo inputstream.
            // A questo scopo serve l'istruzione "socket.setSoTimeout(5000);" presente nel metodo connectClients
            // del server, di modo che, nel caso in cui non dovesse arrivare
            // l'header in tempo non avremo un'attesa infinita, bensì questa terminerà
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Problema nell'istanziazione di 'in' in SocketHandler " + e.getMessage());
        }

        System.out.println("SocketHandler istanziato correttamente");

    }


    /**
     * Reads a message sent through the input stream associated with the current socket connection.
     * The method waits for incoming data and attempts to deserialize it into a {@code Message} object.
     * The method uses synchronization to ensure thread-safe access to the input stream.
     *
     * @return the {@code Message} object received, deserialized from the input stream.
     * @throws SocketTimeoutException if a timeout occurs or the socket connection is deemed unreliable.
     * @throws RuntimeException for other unexpected deserialization errors.
     */
    public Message readMessage() throws SocketTimeoutException {
        synchronized (lockLettura) {
            Message received = null;
            try {
                received = (Message)in.readObject();
                return received;

            }catch(SocketTimeoutException e){
                throw new SocketTimeoutException("Problema(SocketTimeoutException) in readMessage in SocketHandler " + e.getMessage());
            }
            catch (IOException e) {
                // e.printStackTrace();
                int gameId = UsersConnected.getInstance().getGameFromUsername(username).getId();
                Server.getInstance().notifyAllObservers(new BroadcastMessage(new MatchAbandoned("La partita è terminata perchè un player è uscito")), gameId);
                Server.getInstance().disconnectAll(gameId, username);
                throw new SocketTimeoutException("stop listening");
                // throw new RuntimeException("Problema(IOException) in readMessage in SocketHandler " + e.getMessage());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Problema(ClassNotFoundException) in readMessage in SocketHandler " + e.getMessage());
            }
        }
    }


    /**
     * Sends a {@code Message} object through the output stream associated with the current socket connection.
     * The method ensures thread-safe access to the shared output stream by synchronizing on a lock.
     * It resets, writes, and flushes the object to the output stream to complete the transmission.
     *
     * @param message the {@code Message} object to be sent. The message must implement the
     *                {@code Serializable} interface to allow for proper serialization.
     * @return {@code true} if the message was successfully sent.
     * @throws RuntimeException if any {@code IOException} occurs during the reset, write, or flush operations
     *                          of the output stream.
     */
    public boolean sendMessage(Message message) {
        synchronized (lockScrittura){
            try {
                out.reset();
            } catch (IOException e) {
                throw new RuntimeException("Problema(IOException) in reset di 'out' in writeMessage in SocketHandler " + e.getMessage());
            }
            try {
                // System.out.println("Sending " + message);
                out.writeObject(message);
            } catch (IOException e) {
                throw new RuntimeException("Problema(IOException) in writeObject di 'out' in writeMessage in SocketHandler " + e.getMessage());
            }
            try {
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException("Problema(IOException) in flush di 'out' in writeMessage in SocketHandler " + e.getMessage());
            }
        }
        return true;
    }

    /**
     * Closes the resources associated with this SocketHandler instance.
     * Ensures proper closure of the input stream, output stream, and socket associated with the handler.
     * This method aims to clean up resources and ensure the SocketHandler object is left in a consistent state by
     * nullifying its references to the underlying resources.
     */
    public void close(){
        try {
            this.in.close();
        }
        // In caso di eccezioni non succede niente
        catch (IOException ignored) {

        }
        /*
        Tramite il finally sono sicuro che, sia che la chiusura fallisca o venga fatta correttamente, il mio oggetto
        sia posto a null
        */
        finally {
            this.in = null;
        }

        try {
            this.out.close();
        } catch (IOException ignored) {

        } finally {
            this.out = null;
        }

        try {
            this.socket.close();
        } catch (IOException ignored) {

        } finally {
            this.socket = null;
        }
    }

    /**
     * Sets the username for the current SocketHandler instance.
     * This username can be used to identify the client being managed by this handler.
     *
     * @param username the username to be associated with this SocketHandler instance.
     *                 Must be a non-null and non-empty String.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
