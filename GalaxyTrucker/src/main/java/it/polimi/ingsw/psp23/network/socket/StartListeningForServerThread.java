/*
 * Classe fondamentale lato client per ascoltare i messaggi provenienti dal server
 */

package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.network.messages.Message;


public class StartListeningForServerThread extends Thread {

    private final SocketHandler socketHandler;
    private final MessageObserver messageObserver;

    public StartListeningForServerThread(SocketHandler socketHandler, MessageObserver messageObserver) {
        this.socketHandler = socketHandler;
        this.messageObserver = messageObserver;
    }

    // TODO: il seguente metodo potrebbe essere migliorato aggiungendo ad esempio un timeout nel caso in cui la
    //       connessione dovesse cadere in maniera inappropriata, causando un mancato aggiornamento del flag isClosed
    @Override
    public void run() {
        try{
            while(true){
                Message message = socketHandler.readMessage();
                if(message != null) {
                    messageObserver.messageReceived(message);
                }
            }
        }
        catch(Exception e){
            throw new RuntimeException("Errore in run di StartListeningForServerThread: " + e.getMessage());
        }
    }
}
