package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.exceptions.GameException;
import it.polimi.ingsw.psp23.exceptions.InvalidCoordinatesException;
import it.polimi.ingsw.psp23.exceptions.PlayerExistsException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.ConnectionThread;
import it.polimi.ingsw.psp23.network.Server;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.request.HandleActionVisitor;
import it.polimi.ingsw.psp23.protocol.response.ErrorResponse;
import it.polimi.ingsw.psp23.protocol.response.IncorrectWelding;
import it.polimi.ingsw.psp23.protocol.response.MatchAbandoned;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * ClientRMIHandler is a server-side handler for managing client interactions via Remote Method Invocation (RMI).
 *
 * This class is responsible for implementing the methods defined in the {@link ClientRMIHandlerInterface} interface.
 * It handles operations such as:
 * - Registering and maintaining client connections.
 * - Sending messages to individual clients or broadcasting messages to multiple clients.
 * - Managing game-related data, including levels, status, and player information.
 * - Initiating game phases and monitoring connection health via heartbeats.
 * - Managing disconnections and providing information about available games.
 *
 * The class extends {@link UnicastRemoteObject} to allow its methods to be invoked remotely and relies on
 * the {@link ClientRegistryInterface} for managing client registration and data retrieval.
 */
public class ClientRMIHandler extends UnicastRemoteObject implements ClientRMIHandlerInterface {
    private final ClientRegistryInterface registry;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public ClientRMIHandler(ClientRegistryInterface registry) throws RemoteException{
        super();
        this.registry = registry;
    }

    /**
     * Registers a client with the server, enabling communication through a remote callback
     * interface. This method adds the client to the server's registry and initializes the
     * communication, such as periodic pings for connection maintenance. If the server conditions
     * for accepting clients are not met, an exception may be thrown to indicate unavailability.
     *
     * @param username the unique identifier of the user requesting registration
     * @param nameConnection a string representing the connection name for the client
     * @param callback the client's remote callback interface implementation
     * @throws RemoteException if an error occurs during the remote communication process
     */
    @Override
    public void registerClient(String username, String nameConnection, ClientCallbackInterface callback) throws RemoteException{

        // List<String> usersConnected = UsersConnected.getInstance().getClients();

        //synchronized (usersConnected) {
            //UsersConnected.getInstance().addClient(nameConnection);
            registry.registerClient(nameConnection, callback);
            System.out.println("Client connected: " + nameConnection);

            /*if(UsersConnected.getInstance().getClients().size() == 1){

                //socketHandler.sendMessage(new DirectMessage(new SelectLevel()));
                Message message = (new DirectMessage(new SelectLevel()));

                callback.onReceivedMessage(message);

                // System.out.println(message.toString());

            }*/


            /*if(UsersConnected.getInstance().getClients().size() != 1 && Game.getInstance().getNumRequestedPlayers() == -1){
                UsersConnected.getInstance().removeClient(nameConnection);
                throw new LobbyUnavailableException("lobby is unavailable");
            }*/

        //}

        initializePing();
    }

    /**
     * Sends a heartbeat signal to indicate that the client is active and connected.
     * This method is typically called periodically by the client to maintain their
     * presence in the system and prevent disconnection due to inactivity.
     *
     * @param username the unique identifier of the client sending the heartbeat signal
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     */
    @Override
    public void heartbeat(String username) throws RemoteException{}


    /**
     * Sends a message to all clients listed in the provided username list.
     * For each username in the list, the method attempts to deliver the message by invoking
     * the {@code sendToNickname} method. If a client does not respond, it is assumed offline
     * and will be deregistered.
     *
     * @param msg the {@link Message} object to be sent to all clients
     * @param listaUsername the list of usernames representing the clients to whom the message should be sent
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     */
    @Override
    public void sendToAllClients(Message msg, List<String> listaUsername) throws RemoteException {
        for (String u : listaUsername) {
            try {
                sendToNickname(u, msg);
            } catch (RemoteException e) {
                // il client probabilmente è offline: deregistralo
                System.err.println("Client non risponde, rimuovo dallo stub list.");
            }
        }
    }

    /**
     * Sends a {@link Message} to a specific user identified by their connection name.
     * The method retrieves the {@link ClientCallbackInterface} associated with the specified
     * connection name from the client registry and attempts to deliver the message via a remote call.
     * If the specified client is not registered or communication fails, appropriate error handling is performed.
     *
     * @param nameConnection the unique connection name of the user to whom the message should be sent
     * @param msg the {@link Message} object to be delivered to the specified user
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     */
    @Override
    public void sendToUser(String nameConnection, Message msg) throws RemoteException {
        ClientCallbackInterface callback = registry.getClient(nameConnection);
        if (callback == null) {
            System.err.println("sendToUser: nessun client registrato con username \"" + nameConnection + "\"");
            return;
        }
        try {
            callback.onReceivedMessage(msg);
        } catch (RemoteException e) {
            // Se il client non risponde, rimuovilo dalla lista
            e.printStackTrace();
            System.err.println("sendToUser: impossibile inviare a \"" + nameConnection + "\": " + e.getMessage());
        }
    }

    /**
     * Sends a {@link Message} to a user identified by their nickname.
     * This method first retrieves the connection name associated with the specified nickname
     * using the registry. Then, it attempts to send the message by invoking the remote
     * {@code onReceivedMessage} method of the corresponding {@link ClientCallbackInterface}.
     * If the client is not registered or the communication fails, appropriate error handling
     * is performed.
     *
     * @param username the nickname of the user to whom the message should be sent
     * @param msg the {@link Message} object to be delivered to the specified user
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     */
    @Override
    public void sendToNickname(String username, Message msg) throws RemoteException {
        String nameConnection = registry.getPlayerConnectionFromNickname(username);
        ClientCallbackInterface callback = registry.getClient(nameConnection);
        if (callback == null) {
            System.err.println("sendToUser: nessun client registrato con username \"" + nameConnection + "\"");
            return;
        }
        try {
            callback.onReceivedMessage(msg);
        } catch (RemoteException e) {
            // Se il client non risponde, rimuovilo dalla lista
            e.printStackTrace();
            System.err.println("sendToUser: impossibile inviare a \"" + nameConnection + "\": " + e.getMessage());
        }
    }

    /**
     * Sends an action request to the server for processing. This method allows
     * a client to perform a specific action within the game, identified by the
     * provided parameters. If an error occurs during the action execution, an
     * appropriate error response is sent back to the client.
     *
     * @param username the unique identifier of the user performing the action
     * @param nameConnection the unique connection name of the user
     * @param action the action to be executed on behalf of the client
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     */
    @Override
    public void sendAction(String username, String nameConnection, Action action) throws RemoteException{
        try {
            action.call(new HandleActionVisitor(), username);
        }
        catch(GameException e) {
            DirectMessage dm = new DirectMessage(new ErrorResponse(e.getMessage()));
            sendToUser(nameConnection, dm);
        }
        catch (InvalidCoordinatesException invalidCoordinatesException) {
            DirectMessage dm = new DirectMessage(new ErrorResponse(invalidCoordinatesException.getMessage()));
            Server.getInstance().sendMessage(username, dm);
            DirectMessage dm1 = new DirectMessage(new IncorrectWelding());
            Server.getInstance().sendMessage(username, dm1);
        }
    }

    /**
     * Sets the level of the game that the client is interacting with.
     * This method registers a new game with the specified level and updates
     * the server's game and user connections information accordingly.
     *
     * @param level the level of the game to be set
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     */
    @Override
    public void setGameLevel(int level) throws RemoteException {
        UsersConnected.getInstance().addGame();
        int gameIdConsidering = Server.getInstance().getGamesSize();
        Server.getInstance().addGame(new Game(level, gameIdConsidering));
    }

    /**
     * Retrieves the game level for a specific game identified by its ID.
     * This method interacts with the server to fetch the current level
     * of the specified game.
     *
     * @param gameId the unique identifier of the game whose level is being requested
     * @return the current level of the specified game
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     */
    @Override
    public int getGameLevel(int gameId) throws RemoteException {
        return Server.getInstance().getGame(gameId).getLevel();
    }

    /**
     * Retrieves the current status of a specific game identified by its ID.
     *
     * @param gameId the unique identifier of the game whose status is being requested
     * @return the current {@link GameStatus} of the specified game
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     */
    @Override
    public GameStatus getGameStatus(int gameId) throws RemoteException {
        return Server.getInstance().getGame(gameId).getGameStatus();
    }

    /**
     * Retrieves the number of players currently connected to a specific game.
     * This method uses the provided game ID to query the player connection registry
     * and determine the number of active connections for that game.
     *
     * @param gameId the unique identifier of the game for which to retrieve the number of connected players
     * @return the number of players currently connected to the specified game
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     */
    @Override
    public int getNumPlayersConnected(int gameId) throws RemoteException {
        return UsersConnected.getInstance().getClients(gameId).size();
    }

    /**
     * Associates a username with a game in the system. This method adds the provided
     * username to the game specified by its ID. If the username already exists in the
     * system, a PlayerExistsException is thrown.
     *
     * @param username the unique identifier for the player to be added
     * @param gameId the unique identifier of the game to which the player should be associated
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     * @throws PlayerExistsException if the username already exists in the system
     */
    @Override
    public void setPlayerUsername(String username, int gameId) throws RemoteException{
        // Controller.getInstance().addPlayerToGame(username);
        if(UsersConnected.getInstance().usernameAlreadyExists(username)){
            throw new PlayerExistsException("");
        }
        UsersConnected.getInstance().addClient(username, gameId);
        UsersConnected.getInstance().getGameFromUsername(username).getController().addPlayerToGame(username);
    }

    /**
     * Sets the desired number of players requested for a game session by a user.
     * The method updates the game configuration for the user and ensures the server is
     * actively listening for connections if it is not already doing so.
     *
     * @param num the number of players requested for the game
     * @param username the unique identifier of the user requesting the change
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     */
    @Override
    public void setNumRequestedPlayers(int num, String username) throws RemoteException{
        UsersConnected.getInstance().getGameFromUsername(username).setNumRequestedPlayers(num);
        if(!ConnectionThread.getInstance().isListening()) {
            ConnectionThread.getInstance().start();
        }
    }

    /**
     * Retrieves the number of players requested for a specific game identified by its ID.
     * This method interacts with the server to fetch the requested number of players for the game.
     *
     * @param gameId the unique identifier of the game for which the number of requested players is being retrieved
     * @return the number of players requested for the specified game
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     */
    @Override
    public int getNumRequestedPlayers(int gameId) throws RemoteException{
        return Server.getInstance().getGame(gameId).getNumRequestedPlayers();
    }

    /**
     * Initiates the building phase for a specific game identified by its unique ID.
     * This method triggers the corresponding game controller to manage the building phase.
     *
     * @param gameId the unique identifier of the game for which the building phase is to be started
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     */
    @Override
    public void startBuildingPhase(int gameId) throws RemoteException{
        Server.getInstance().getGame(gameId).getController().startBuildingPhase();
    }

    /**
     * Retrieves the total number of active games managed by the server.
     *
     * @return the current count of active games
     * @throws RemoteException if a communication-related error occurs during the remote method invocation
     */
    @Override
    public int getGamesSize() throws RemoteException{
        return Server.getInstance().getGamesSize();
    }

    /**
     * Retrieves a list of available games that are currently in the setup phase
     * and have a specified number of players requested. Each available game is
     * represented as a list of integers containing its ID, the current number
     * of players, the number of requested players, and the game level.
     *
     * @return a list of lists, where each inner list represents an available game
     *         with its relevant details. If no games are available, an empty list
     *         is returned.
     * @throws RemoteException if an error occurs during remote communication.
     */
    @Override
    public synchronized List<List<Integer>> getGamesAvailables() throws RemoteException{
        ArrayList<List<Integer>> matchesAvailable = new ArrayList<>();
        for(Game g : Server.getInstance().getGames()){
            if(g.getGameStatus() == GameStatus.Setup && g.getNumRequestedPlayers() != -1) {
                List<Integer> info = new ArrayList<>();
                info.add(g.getId());
                info.add(g.getPlayers().size());
                info.add(g.getNumRequestedPlayers());
                info.add(g.getLevel());
                matchesAvailable.add(info);
            }
        }
        return matchesAvailable;
    }

    /**
     * Initializes a periodic ping mechanism to monitor the availability of connected clients.
     * This method schedules a task to send heartbeat messages to all registered clients
     * at a fixed interval. If a client does not respond to the heartbeat due to a
     * RemoteException, it attempts to determine the client's details (such as connection name
     * and nickname) and proceeds to handle their disconnection, potentially ending the ongoing game.
     *
     * @throws RemoteException if an exception occurs while accessing the client registry
     *                         or during heartbeat communication.
     */
    private void initializePing() throws RemoteException {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                for (ClientCallbackInterface client : registry.getAllClients()) {
                    try {
                        client.sendHeartbeat();
                    } catch (RemoteException e) {
                        String nameConnection = registry.getNameConnectionFromCallback(client);
                        String nickname = registry.getPlayerNicknameFromConnection(nameConnection);
                        int gameId = UsersConnected.getInstance().getGameFromUsername(nickname).getId();
                        Server.getInstance().notifyAllObservers(new BroadcastMessage(new MatchAbandoned("La partita è terminata perchè un player è uscito")), gameId);
                        Server.getInstance().disconnectAll(gameId, nickname);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Disconnects all players provided in the list by unregistering
     * their respective client connections.
     *
     * @param players a list of player nicknames whose connections need to be disconnected
     * @throws RemoteException if a remote communication error occurs during disconnection
     */
    public void disconnectAll(List<String> players) throws RemoteException {
        for(String player : players) {
            String nameConnection = registry.getPlayerConnectionFromNickname(player);
            registry.unregisterClient(nameConnection);
        }
    }
}
