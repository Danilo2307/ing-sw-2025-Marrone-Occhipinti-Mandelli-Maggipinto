package it.polimi.ingsw.psp23.network;

import it.polimi.ingsw.psp23.model.Game.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * The UsersConnected class is a singleton responsible for managing connected users
 * and their associations with specific games. Users are stored in a thread-safe
 * manner using a HashMap where each key represents a game ID, and the value is
 * a list of usernames associated with that game.
 */
public class UsersConnected {
    public static UsersConnected instance;
    // Gli interi della hashmap sono 0-based
    HashMap<Integer, List<String>>  clients;

    /**
     * Initializes a new instance of the UsersConnected class.
     *
     * This constructor sets up the internal data structure for storing the
     * associations between game IDs and connected users. Specifically, it
     * initializes a HashMap that maps each game ID to a list of usernames.
     */
    public UsersConnected() {
        clients = new HashMap<>();
    }

    /**
     * Returns the singleton instance of the UsersConnected class.
     * If the instance does not already exist, it initializes it and returns the same instance
     * on subsequent calls. This method is thread-safe.
     *
     * @return the singleton instance of the UsersConnected class.
     */
    public static synchronized UsersConnected getInstance() {
        if(instance == null){
            instance = new UsersConnected();
        }
        return instance;
    }

    /**
     * Adds a client represented by the specified username to the list of usernames
     * associated with the provided game ID.
     *
     * @param username the username of the client to be added
     * @param id the ID of the game to which the client is to be added
     */
    public void addClient(String username, int id){
        clients.get(id).add(username);
    }

    /**
     * Adds a new game entry to the internal mapping of connected clients.
     * Each game is represented by a unique game ID, which is automatically
     * assigned based on the current size of the internal map. The new game
     * entry is initialized with an empty list of client usernames.
     */
    public void addGame(){
        clients.put(clients.size(), new ArrayList<>());
    }

    /**
     * Removes a client identified by their username from the list of clients associated with a specific game.
     *
     * @param username the username of the client to be removed
     * @param id the unique identifier of the game from which the client is to be removed
     */
    public void removeClient(String username, int id){
        clients.get(id).remove(username);
    }

    /**
     * Retrieves the list of usernames associated with a specific game ID.
     *
     * @param id the game ID for which the list of usernames is to be retrieved
     * @return a list of usernames associated with the specified game ID,
     *         or null if no such game ID exists
     */
    public List<String> getClients(int id){
        return clients.get(id);
    }

    /**
     * Retrieves the map of game IDs and their associated connected users.
     *
     * @return a HashMap where each key is a game ID (Integer) and the corresponding value
     *         is a List of usernames (List<String>) associated with that game.
     */
    public HashMap<Integer, List<String>> getMap(){
        return clients;
    }

    /**
     * Retrieves the game associated with the given username.
     *
     * @param username the username to search for within the connected clients.
     * @return the Game instance associated with the username, or null if no such game exists.
     */
    public Game getGameFromUsername(String username){
        for(Integer i : clients.keySet()){
            if(clients.get(i).contains(username)){
                return Server.getInstance().getGame(i);
            }
        }
        return null;
    }

    /**
     * Checks whether the given username already exists in the system.
     * This method iterates through all connected games and verifies if
     * any of them contains the specified username.
     *
     * @param username the username to check for existence.
     * @return true if the username already exists in any game; false otherwise.
     */
    public boolean usernameAlreadyExists(String username){
        for(Integer i : clients.keySet()){
            if(clients.get(i).contains(username)){
                return true;
            }
        }
        return false;
    }

}
