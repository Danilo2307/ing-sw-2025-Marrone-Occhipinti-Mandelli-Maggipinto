package it.polimi.ingsw.psp23;

import it.polimi.ingsw.psp23.network.socket.ConnectionThread;
import it.polimi.ingsw.psp23.network.socket.Server;


public class MainServer {
    public static void main(String[] args) {
        Server.getInstance(8000);
        ConnectionThread connectionThread = new ConnectionThread();
        connectionThread.start();

    }
}
