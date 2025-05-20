package it.polimi.ingsw.psp23.network.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client {
    private final String username;

    /**
     * Costruttore RMI.
     * @param host indirizzo del server RMI
     * @param port porta del registry (es. 1099)
     * @param username username del giocatore
     * @throws Exception in caso di errori RMI
     */
    public Client(String host, int port, String username) throws Exception {
        this.username = username;

        // 1) Recupera il registry e fai lookup del server
        Registry reg = LocateRegistry.getRegistry(host, port);


    }
}
