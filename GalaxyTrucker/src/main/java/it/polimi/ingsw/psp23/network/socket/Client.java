// Questa classe rappresenta il player lato client
package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.messages.fromclient.SetUsernameMsg;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

public class Client{

    SocketHandler socketHandler;
    StartListeningForServerThread startListeningForServerThread;


    // Nel costruttore verrà eseguita la connessione tra client e server
    Client(String serverIP, int port, String username, MessageObserver messageObserver) {

        Message message = new SetUsernameMsg(username);

        try {
            Socket socket = new Socket(serverIP, port);
            socketHandler = new SocketHandler(socket);
            Message usernameMsg = new SetUsernameMsg(username);
            socketHandler.sendMessage(message);
            startListeningForServerThread = new StartListeningForServerThread(socketHandler, messageObserver);
            startListeningForServerThread.start();
        }
        catch (IOException e) {
            throw new RuntimeException("Errore class Client constructor: " + e.getMessage());
        }

    }


    // Metodo incaricato dell'inoltro dei messaggi
    public void sendMessage(Message message) {
        socketHandler.sendMessage(message);
    }


    // Avremmo potuto anche usare i try-with-resources negli altri blocchi, ma fare chiusure esplicite rende più robusto
    // il codice
    public void close() {

    }

}
