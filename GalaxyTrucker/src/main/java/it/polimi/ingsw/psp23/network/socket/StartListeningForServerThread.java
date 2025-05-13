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


public class StartListeningForServerThread extends Thread {

    private final SocketHandler socketHandler;
    private final ClientEventHandler clientEventHandler;
    private final Client client;
    private boolean running = true;

    public StartListeningForServerThread(SocketHandler socketHandler, ClientEventHandler clientEventHandler, Client client) {
        this.socketHandler = socketHandler;
        this.clientEventHandler = clientEventHandler;
        this.client = client;
    }

    // TODO: il seguente metodo potrebbe essere migliorato aggiungendo ad esempio un timeout nel caso in cui la
    //       connessione dovesse cadere in maniera inappropriata, causando un mancato aggiornamento del flag isClosed
    @Override
    public void run() {
        try{
            while(running){
                Message message = socketHandler.readMessage();
                if(message != null) {

                    // Dopo che ci arriva un messaggio dobbiamo interpretarlo. Se è un messaggio di tipo "observer"
                    // allora chiamiamo il metodo dell'interfaccia messageObserver, altrimenti si chiama il semplice
                    // handleMessage presente nel client
                    // TODO: cambia switch con visitor
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
        } catch (SocketTimeoutException e) {

        }
        catch(Exception e){
            throw new RuntimeException("Errore in run di StartListeningForServerThread: " + e.getMessage());
        }
    }

    public void stopThread(){
        running = false;
    }

}
