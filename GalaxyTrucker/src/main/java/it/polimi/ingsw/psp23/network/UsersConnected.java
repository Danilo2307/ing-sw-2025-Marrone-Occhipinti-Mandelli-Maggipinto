package it.polimi.ingsw.psp23.network;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.socket.ClientSocket;
import it.polimi.ingsw.psp23.network.socket.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// UsersConnected è un'istanza SINGLETON
public class UsersConnected {
    public static UsersConnected instance;
    // Gli interi della hashmap sono 0-based
    HashMap<Integer, List<String>>  clients;

    public UsersConnected() {
        clients = new HashMap<>();
    }

    public static synchronized UsersConnected getInstance() {
        if(instance == null){
            instance = new UsersConnected();
        }
        return instance;
    }

    public void addClient(String username, int id){
        clients.get(id).add(username);
    }

    public void addGame(){
        clients.put(clients.size(), new ArrayList<>());
    }

    public void removeClient(String username, int id){
        clients.get(id).remove(username);
    }

    public List<String> getClients(int id){
        return clients.get(id);
    }

    public HashMap<Integer, List<String>> getMap(){
        return clients;
    }

    public Game getGameFromUsername(String username){
        for(Integer i : clients.keySet()){
            if(clients.get(i).contains(username)){
                return Server.getInstance().getGame(i);
            }
        }
        return null;
    }

    public boolean usernameAlreadyExists(String username){
        for(Integer i : clients.keySet()){
            if(clients.get(i).contains(username)){
                return true;
            }
        }
        return false;
    }

}
