package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.messages.SelectCannonsMessage;

/*
 * Questa classe è responsabile della gestione lato Server del client, è come se nel server ci fosse una
 * versione "virtuale" del client da gestire in base alla connessione
 */
public class ClientHandler {
    private StartListeningForClientThread clientThread = null;
    private final String connectionID;
    private String username = null;

    ClientHandler(String connectionID) {
        this.connectionID = connectionID;
        clientThread = new StartListeningForClientThread(connectionID);
        clientThread.start();
    }

    public void send(Message message) {
        Server.getInstance().sendMessage(message, connectionID);
        System.out.println("Il ClientHandler ha inviato il messaggio: " + message + " da parte della connessione: " + connectionID);
    }

    public void handleMessage(Message message) {
        switch(message){
            case SelectCannonsMessage m -> System.out.println("è un select cannon");
            default -> System.out.println("<UNK> un select cannon");
        }
    }

    public String getConnectionID() {
        return connectionID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }



}
