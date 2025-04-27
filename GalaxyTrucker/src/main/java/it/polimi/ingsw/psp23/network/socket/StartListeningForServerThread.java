/*
 * Classe fondamentale lato client per ascoltare i messaggi provenienti dal server
 */

package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.messages.UpdateStateMessage;


public class StartListeningForServerThread extends Thread {

    private final SocketHandler socketHandler;
    private final MessageObserver messageObserver;
    private final Client client;

    public StartListeningForServerThread(SocketHandler socketHandler, MessageObserver messageObserver, Client client) {
        this.socketHandler = socketHandler;
        this.messageObserver = messageObserver;
        this.client = client;
    }

    // TODO: il seguente metodo potrebbe essere migliorato aggiungendo ad esempio un timeout nel caso in cui la
    //       connessione dovesse cadere in maniera inappropriata, causando un mancato aggiornamento del flag isClosed
    @Override
    public void run() {
        try{
            while(true){
                Message message = socketHandler.readMessage();
                if(message != null) {

                    // Dopo che ci arriva un messaggio dobbiamo interpretarlo. Se è un messaggio di tipo "observer"
                    // allora chiamiamo il metodo dell'interfaccia messageObserver, altrimenti si chiama il semplice
                    // handleMessage presente nel client
                    switch(message){
                        case UpdateStateMessage m -> messageObserver.messageReceived(message);
                        default -> client.handleMessage(message);
                    }

                }
            }
        }
        catch(Exception e){
            throw new RuntimeException("Errore in run di StartListeningForServerThread: " + e.getMessage());
        }
    }
}
