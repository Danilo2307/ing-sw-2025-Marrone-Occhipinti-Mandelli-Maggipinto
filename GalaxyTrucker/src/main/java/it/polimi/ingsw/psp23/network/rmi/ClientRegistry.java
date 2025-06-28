package it.polimi.ingsw.psp23.network.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;


/**
 * The ClientRegistry class is a remote implementation for managing the registry of clients
 * and players in a distributed system using RMI. It maintains mappings between client connection
 * names, callbacks, and usernames. This provides functionalities to register, unregister,
 * and retrieve client or player details.
 *
 * Features:
 * - Maintains a map of registered clients' callback references.
 * - Tracks players and their associated connections.
 * - Provides operations to add, remove, or retrieve client information.
 * - Supports bidirectional search for usernames and connections.
 *
 * This class extends {@link UnicastRemoteObject} to enable remote invocation and implements
 * the {@link ClientRegistryInterface} to define registry-related operations.
 */
public class ClientRegistry extends UnicastRemoteObject implements ClientRegistryInterface{
    /**
     * Implementazione del registry che mantiene mappa username → stub callback.
     */
    private final Map<String,ClientCallbackInterface> clients = new ConcurrentHashMap<>();
    private final Map<String,String> players = new ConcurrentHashMap<>();

        public ClientRegistry() throws RemoteException {
            super();
        }

        /**
         * Registers a client in the server's client registry.
         * This method associates a client callback interface with the provided unique connection name,
         * allowing the server to communicate with the client through RMI.
         *
         * @param nameConnection the unique identifier for the client connection
         * @param callback the callback interface implementation that enables server-to-client communication
         * @throws RemoteException if an RMI-related communication error occurs
         */
        @Override
        public void registerClient(String nameConnection, ClientCallbackInterface callback) throws RemoteException {
            clients.put(nameConnection, callback);
            System.out.println("ClientRegistry: registrato " + nameConnection);
        }

        /**
         * Unregisters a client from the system by their connection name.
         * This method disconnects the client, removes their entry from the client registry,
         * and logs the deregistration event.
         *
         * @param nameConnection the unique identifier for the client connection to be removed
         * @throws RemoteException if a remote communication error occurs during the operation
         */
        @Override
        public void unregisterClient(String nameConnection) throws RemoteException {
            clients.get(nameConnection).disconnectClient();
            clients.remove(nameConnection);
            System.out.println("ClientRegistry: deregistrato " + nameConnection);
        }

        /**
         * Retrieves all registered clients from the client registry.
         *
         * @return a collection of clients represented as {@link ClientCallbackInterface} instances,
         *         corresponding to the currently registered clients in the system.
         * @throws RemoteException if a remote communication error occurs during the operation.
         */
        @Override
        public Collection<ClientCallbackInterface> getAllClients() throws RemoteException {
            return clients.values();
        }

        /**
         * Adds a new player to the system by associating a username with a connection name.
         * This method updates the internal player records to include the new player.
         *
         * @param username the username of the player to be added
         * @param nameConnection the unique identifier for the player's connection
         * @throws RemoteException if a remote communication error occurs during the operation
         */
        @Override
        public void addPlayer(String username, String nameConnection) throws RemoteException{
            players.put(nameConnection, username);
        }

        /**
         * Retrieves all registered player's nicknames from the player registry.
         *
         * @return a collection of strings representing the nicknames of all registered players.
         * @throws RemoteException if a remote communication error occurs during the operation.
         */
        @Override
        public Collection<String> getAllPlayers() throws RemoteException{
            return players.values();
        }

        /**
         * Retrieves the client callback interface associated with a specific connection name.
         * This method allows access to the client callback interface registered under the provided
         * connection name, enabling server-to-client communication via RMI.
         *
         * @param nameConnection the unique identifier associated with a client's connection
         * @return the {@link ClientCallbackInterface} instance corresponding to the specified connection name,
         *         or null if no client is registered with the given name
         * @throws RemoteException if a remote communication error occurs during the operation
         */
        @Override
        public ClientCallbackInterface getClient(String nameConnection) throws RemoteException {
            return clients.get(nameConnection);
        }

        /**
         * Retrieves the connection name associated with a given player's nickname.
         *
         * @param nickname the nickname of the player whose connection name is to be retrieved
         * @return the connection name associated with the specified nickname,
         *         or null if no matching player is found
         * @throws RemoteException if a remote communication error occurs during the operation
         */
        @Override
        public String getPlayerConnectionFromNickname(String nickname) throws RemoteException{
            return  players.entrySet()
                    .stream()
                    .filter(e -> Objects.equals(e.getValue(), nickname))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
        }

        /**
         * Retrieves the player's nickname associated with the specified connection.
         *
         * @param connection the unique identifier for a player's connection
         * @return the nickname of the player associated with the given connection,
         *         or null if no player is found for the specified connection
         * @throws RemoteException if a remote communication error occurs during the operation
         */
        @Override
        public String getPlayerNicknameFromConnection(String connection) throws RemoteException{
            return  players.get(connection);
        }


         /**
          * Retrieves the connection name associated with a given client callback interface.
          * This method searches through the client registry to find the connection name
          * that corresponds to the specified callback interface.
          *
          * @param c the {@link ClientCallbackInterface} whose associated connection name is to be retrieved
          * @return the connection name associated with the specified callback interface,
          *         or null if no match is found
          * @throws RemoteException if a remote communication error occurs during the operation
          */
         @Override
         public String getNameConnectionFromCallback(ClientCallbackInterface c) throws RemoteException{
            for(String s : clients.keySet()){
                if(clients.get(s).equals(c)){
                    return s;
                }
            }
            return null;
        }
}
