package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.controller.ServerActionHandler;
import it.polimi.ingsw.psp23.events.Action;
import it.polimi.ingsw.psp23.network.messages.Message;

/*
 * Questa classe è responsabile della gestione lato Server del client, è come se nel server ci fosse una
 * versione "virtuale" del client da gestire in base alla connessione
 */
public class ClientHandler {
    private StartListeningForClientThread clientThread = null;
    private final String connectionID;
    private String username;
    private ServerActionHandler serverActionHandler;

    ClientHandler(String connectionID) {
        this.connectionID = connectionID;
        clientThread = new StartListeningForClientThread(connectionID);
        clientThread.start();
        username = Server.getInstance().getUsernameForConnection(connectionID);
        serverActionHandler = new ServerActionHandler(username);
    }

    public void send(Message message) {
        Server.getInstance().sendMessage(message, connectionID);
        System.out.println("Il ClientHandler ha inviato il messaggio: " + message + " da parte della connessione: " + connectionID);
    }

    public void handleMessage(Message message) {
        Action action = message.getAction();
        serverActionHandler.handleAction(action);
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
