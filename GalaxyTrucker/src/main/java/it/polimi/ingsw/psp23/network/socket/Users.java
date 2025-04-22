package it.polimi.ingsw.psp23.network.socket;

import java.util.ArrayList;
import java.util.List;

public class Users {
    private static Users instance = null;

    // Questa variabile potrebbe anche essere una hashmap per rendere più rapida la ricerca del client di nostro
    // interesse
    private final List<ClientHandler> clients;

    Users() {
        clients = new ArrayList<>();
    }

    public static Users getInstance() {
        if (instance == null) {
            instance = new Users();
        }
        return instance;
    }

    public void createClientHandler(String connectionID) {
        System.out.println("aggiunta connessione alla lista di clienthandler con esito: " + clients.add(new ClientHandler(connectionID)));
    }

    public ClientHandler getClientHandler(String connectionID) {
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getConnectionID().equals(connectionID)) {
                return clientHandler;
            }
        }
        return null;
    }

    public List<ClientHandler> getClients() {
        return new ArrayList<>(clients);
    }

}
