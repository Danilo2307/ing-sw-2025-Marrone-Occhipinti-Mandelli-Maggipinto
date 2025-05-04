// Questa classe rappresenta il player lato client
package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.exceptions.PlayerExistsException;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.GetEventVisitor;
import it.polimi.ingsw.psp23.protocol.request.SetUsername;
import it.polimi.ingsw.psp23.network.messages.ActionMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.view.TUI.ClientEventHandler;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    SocketHandler socketHandler;
    StartListeningForServerThread startListeningForServerThread;


    // Nel costruttore verrà eseguita la connessione tra client e server
    public Client(String serverIP, int port, String username, ClientEventHandler clientEventHandler) {



        try {
            Socket socket = new Socket(serverIP, port);
            socketHandler = new SocketHandler(socket);

            //message = socketHandler.readMessage();
            startListeningForServerThread = new StartListeningForServerThread(socketHandler, clientEventHandler, this);
            startListeningForServerThread.start();
            //if (!message.call(new GetEventVisitor()).toString().equals("Appropriate Username")) {
            // this.close();
            //throw new PlayerExistsException("Lanciata dal costruttore di client");
            //}
        }
        catch (IOException e) {
            throw new RuntimeException("Errore class Client constructor: " + e.getMessage());
        }

    }


    // Metodo incaricato dell'inoltro dei messaggi
    public void sendMessage(Message message) {
        socketHandler.sendMessage(message);
    }

    public void handleMessage(Message message) {

    }

    // Avremmo potuto anche usare i try-with-resources negli altri blocchi, ma fare chiusure esplicite rende più robusto
    // il codice
    public void close() {
        socketHandler.close();
        startListeningForServerThread.stopThread();
    }

    public void setUsername(String username) {
        Message message = new ActionMessage(new SetUsername(username));
        socketHandler.sendMessage(message);
    }

}
