package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.network.messages.Message;

/*
 * Thread necessario AL SERVER per stare sempre in attesa dei messaggi in arrivo da un client, quindi ci saranno tante
 * istanze di StartListeningForClientThread quanti saranno i client. In questa classe chiameremo la risposta
 * specifica di ogni ClientHandler in base all'id della connessione
 */
public class StartListeningForClientThread extends Thread {

    private boolean haveToListen = true;
    private final Object lock = new Object();
    private String connectionID = null;

    StartListeningForClientThread(String connectionID) {
        this.connectionID = connectionID;
    }


    public void stopListening() {
        synchronized (lock) {
            haveToListen = false;
        }
    }

    @Override
    public void run() {
        while(true){

            synchronized (lock) {
                if (!haveToListen) {
                    break;
                }
            }
            Message receivedMessage = Server.getInstance().receiveMessage(connectionID);
            System.out.println("Message read in class StartListeningForClientThread: " + receivedMessage);
            Users.getInstance().getClientHandler(connectionID).handleMessage(receivedMessage);

            synchronized (Server.getInstance().getClients()) {
                if (Server.getInstance().getClients().size() == 1 && Server.getInstance().getServerSocket().isClosed()) {
                    // Se è chiusa e siamo nel primo player riapriamo la serversocket
                    Server.getInstance().setServerSocket("localhost", 8000);
                    // In questo punto permettiamo al Server di accettare nuovi client
                    ConnectionThread.getInstance().start();
                }
            }

        }
    }
}
