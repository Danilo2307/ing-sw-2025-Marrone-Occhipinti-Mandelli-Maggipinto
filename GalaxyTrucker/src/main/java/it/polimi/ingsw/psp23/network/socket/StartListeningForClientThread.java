package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.network.messages.Message;

/**
 * Represents a thread responsible for continuously listening for incoming messages
 * from a specific client in a server-client communication system. Each connected client
 * will have its own instance of this thread. This thread delegates message handling
 * to the appropriate ClientHandler based on the connection ID.
 * This thread starts listening for messages sent by the associated client upon being
 * started and processes them until it is instructed to stop listening or the connection
 * is terminated.
 * Thread safety is managed using a lock to coordinate access to the listening control
 * flag. The thread stops listening and terminates its execution when either the
 * `stopListening` method or the `stopThread` method is called.
 */
public class StartListeningForClientThread extends Thread {

    private boolean haveToListen = true;
    private final Object lock = new Object();
    private String connectionID = null;
    private boolean running = true;

    StartListeningForClientThread(String connectionID) {
        this.connectionID = connectionID;
    }


    public void stopListening() {
        synchronized (lock) {
            haveToListen = false;
        }
    }

    /**
     * Continuously listens for and processes messages from a specific client connection.
     * This method is executed when the thread starts, and it runs in a loop until the
     * thread is explicitly stopped using the `stopListening` or `stopThread` methods,
     * or when a null message is received, indicating the termination of the connection.
     *
     * The loop:
     * - Checks if the thread is allowed to continue listening by acquiring a lock on
     *   the `lock` object and verifying the `haveToListen` flag. If listening has been
     *   stopped, the loop exits.
     * - Receives a message from the server using the connection ID.
     * - If a valid message is received, it delegates the message to the appropriate
     *   `ClientHandler` for processing.
     * - If no message is received (null), it assumes the connection is terminated and
     *   stops the thread.
     *
     * Thread safety is ensured by synchronizing access to the listening control flag using
     * the `lock` object.
     */
    @Override
    public void run() {
        while(running){

            synchronized (lock) {
                if (!haveToListen) {
                    break;
                }
            }
            Message receivedMessage = Server.getInstance().receiveMessage(connectionID);
            System.out.println("Message read in class StartListeningForClientThread: " + receivedMessage);
            if(receivedMessage != null) {
                Users.getInstance().getClientHandler(connectionID).handleMessage(receivedMessage);
            }
            else{
                running = false;
            }



        }
    }

    public void stopThread(){
        running = false;
    }

}
