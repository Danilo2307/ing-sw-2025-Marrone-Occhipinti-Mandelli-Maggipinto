package it.polimi.ingsw.psp23.network;

import it.polimi.ingsw.psp23.network.socket.ClientSocket;

import java.util.ArrayList;
import java.util.List;

// UsersConnected è un'istanza SINGLETON
public class UsersConnected {
    public static UsersConnected instance;
    List<String> clients;

    public UsersConnected() {
        clients = new ArrayList<>();
    }

    public static synchronized UsersConnected getInstance() {
        if(instance == null){
            instance = new UsersConnected();
        }
        return instance;
    }

    public void addClient(String username){
        clients.add(username);
    }

    public void removeClient(String username){
        clients.remove(username);
    }

    public List<String> getClients(){
        return clients;
    }

}
