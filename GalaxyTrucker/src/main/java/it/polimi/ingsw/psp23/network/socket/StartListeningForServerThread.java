/*
 * Classe fondamentale lato client per ascoltare i messaggi provenienti dal server
 */

package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.protocol.response.Event;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.view.ClientEventHandler;

import java.net.SocketTimeoutException;


/**
 * A thread for listening to and processing messages from the server. This class continuously
 * reads incoming messages from the server using the provided {@code SocketHandler}, interprets
 * the type of message, and delegates handling to the {@code ClientEventHandler}.
 *
 * The thread runs until the {@code stopThread()} method is called or an error is encountered.
 */
public class StartListeningForServerThread extends Thread {

    private final SocketHandler socketHandler;
    private final ClientEventHandler clientEventHandler;
    private final ClientSocket client;
    private boolean running = true;

    public StartListeningForServerThread(SocketHandler socketHandler, ClientEventHandler clientEventHandler, ClientSocket client) {
        this.socketHandler = socketHandler;
        this.clientEventHandler = clientEventHandler;
        this.client = client;
    }

    /**
     * Continuously listens for and processes messages received from the server.
     * The method uses the {@code SocketHandler} to read messages from the input stream
     * and delegates the handling of messages based on their type.
     *
     * Functionality:
     * - The thread keeps running while the {@code running} flag is true.
     * - Reads incoming {@code Message} objects from the server.
     * - If a message is received, interprets its type and delegates handling to the {@code ClientEventHandler}.
     * - Handles message types {@code BroadcastMessage} and {@code DirectMessage}, invoking the handler
     *   with the associated event.
     * - If an unexpected exception occurs during execution, logs the stack trace and throws a {@link RuntimeException}.
     *
     * Note:
     * - In case of connection errors during message reading (e.g., unexpected disconnections),
     *   the {@code running} flag is set to false to terminate the thread execution.
     *
     * This method is executed when the thread is started and will continue running in a loop
     * until explicitly stopped or an exception halts the process.
     *
     * @throws RuntimeException if an unexpected error occurs during the execution of the method.
     */
    @Override
    public void run() {
        try{
            while(running){
                Message message = null;
                try {
                    message = socketHandler.readMessage();
                }catch(Exception e){
                    running = false;
                }
                if(running && message != null) {

                    // Dopo che ci arriva un messaggio dobbiamo interpretarlo. Se è un messaggio di tipo "observer"
                    // allora chiamiamo il metodo dell'interfaccia messageObserver, altrimenti si chiama il semplice
                    // handleMessage presente nel client
                    switch(message){
                        case BroadcastMessage m -> {
                            Event event = m.getEvent();
                            clientEventHandler.handle(event);
                        }
                        case DirectMessage m -> {
                            Event event = m.getEvent();
                            clientEventHandler.handle(event);
                        }
                        default -> System.out.println("not handled message");
                    }

                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Errore in run di StartListeningForServerThread: " + e.getMessage());
        }
    }

    public void stopThread(){
        running = false;
    }

}
