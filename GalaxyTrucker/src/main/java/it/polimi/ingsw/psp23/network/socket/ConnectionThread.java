package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.network.Server;

import java.util.UUID;

/**
 * ConnectionThread is a singleton class that extends Thread and is responsible for managing
 * player connections to the game server. This thread listens for new client connections, assigns them
 * unique identifiers, and creates client handlers for managing their sessions.
 *
 * To start the server and begin listening for new clients, the getInstance() method is used
 * to retrieve the singleton instance, and the start() method is called on it.
 */ /*
 * Thread used to connect players to my game. When the game starts, we have to call "ConnectionThread.getInstance().start();"
 * so that the server starts listening for new clients
 * ConnectionThread È SINGLETON!!!!
 */
public class ConnectionThread extends Thread{

    public static ConnectionThread instance = null;

    public boolean listening = false;

    public ConnectionThread(){}

    public static ConnectionThread getInstance(){
        if(instance == null){
            instance = new ConnectionThread();
        }
        return instance;
    }

    /**
     * Executes the main logic of the ConnectionThread. This method is invoked when the thread
     * starts and is responsible for continuously listening for new client connections.
     *
     * The method performs the following tasks in an infinite loop:
     * 1. Sets the listening flag to true, indicating that the thread is active.
     * 2. Generates a unique connection ID for each new client.
     * 3. Registers the client connection by invoking the Server's connectClients method.
     * 4. Creates a client handler for managing the session of the newly connected client
     *    by calling the related functionality in the Users class.
     *
     * This method should be run in a separate thread, as it executes an infinite loop
     * for handling client connections.
     */
    @Override
    public void run() {
        listening = true;
        while(true){

            String connectionId = UUID.randomUUID().toString();

            Server.getInstance().connectClients(connectionId);

            Users.getInstance().createClientHandler(connectionId);

        }
    }

    /**
     * Checks if the server is currently listening for new client connections.
     *
     * @return true if the server is actively listening for incoming connections, false otherwise.
     */
    public boolean isListening(){
        return listening;
    }
}
