package it.polimi.ingsw.psp23.network.socket;

import java.util.ArrayList;
import java.util.List;


/**
 * The Users class implements a singleton design pattern,
 * managing a collection of active ClientHandler objects. This class is
 * responsible for creating, storing, retrieving, and handling the
 * ClientHandler instances which represent connected clients.
 */
public class Users {
    private static Users instance = null;

    // Questa variabile potrebbe anche essere una hashmap per rendere più rapida la ricerca del client di nostro
    // interesse
    private final List<ClientHandler> clients;

    Users() {
        clients = new ArrayList<>();
    }

    public static synchronized Users getInstance() {
        if (instance == null) {
            instance = new Users();
        }
        return instance;
    }

    public void createClientHandler(String connectionID) {
        System.out.println("aggiunta connessione alla lista di clienthandler con esito: " + clients.add(new ClientHandler(connectionID)));
    }

    /**
     * Retrieves the {@code ClientHandler} instance associated with the specified connection ID.
     *
     * @param connectionID the unique connection ID identifying the client handler
     * @return the {@code ClientHandler} associated with the specified connection ID,
     *         or {@code null} if no matching client handler is found
     */
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
