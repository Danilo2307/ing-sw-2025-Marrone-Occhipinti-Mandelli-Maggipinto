package it.polimi.ingsw.psp23.network;

import it.polimi.ingsw.psp23.exceptions.LobbyUnavailableException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.GetActionVisitor;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.rmi.ClientRMIHandlerInterface;
import it.polimi.ingsw.psp23.network.socket.SocketHandler;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.request.HandleActionVisitor;
import it.polimi.ingsw.psp23.protocol.request.SetUsernameActionVisitor;
import it.polimi.ingsw.psp23.protocol.request.UserDecision;
import it.polimi.ingsw.psp23.protocol.response.*;

import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;




/**
 * The Server class is responsible for managing server-side operations,
 * including handling client connections, sending and receiving messages,
 * and managing game sessions.
 *
 * Fields:
 * - serverSocket: Represents the ServerSocket used for client connections.
 * - clients: Stores the connected clients along with their respective connection handlers.
 * - rmiServer: Interface for handling RMI (Remote Method Invocation) server functionalities.
 * - games: Stores the list of games managed by the server.
 */
public class Server {

    private static Server instance;
    private ServerSocket serverSocket;
    private final HashMap<String, SocketHandler> clients;
    private final ClientRMIHandlerInterface rmiServer;
    private final HashMap<Integer, Game> games;


    /**
     * Constructs a new {@code Server} instance, initializing a server socket bound to the
     * specified port and address. The server will listen for incoming client connections.
     * This constructor also initializes internal data structures for managing connected
     * clients and ongoing games.
     *
     * @param port the port number on which the server socket will listen for connections
     * @throws RuntimeException if there is an error initializing the server socket
     */
    // Qui creo il server
    Server(int port) {
        try {

            serverSocket = new ServerSocket(port, 10, InetAddress.getByName("localhost"));

            this.serverSocket.setReuseAddress(true);

            this.rmiServer = null;

            clients = new HashMap<>();

            games = new HashMap<>();

            System.out.println("Server started at port " + port + " and with address " + serverSocket.getInetAddress());

        } catch (IOException e) {
            throw new RuntimeException("Could not load the server: error in constructor of class Server" + e.getMessage() + ")");
        }
    }

    /**
     * Constructs a new {@code Server} instance. This initializes a server socket bound to the
     * specified port and address. The server manages incoming client connections and communication
     * through the provided RMI handler interface.
     *
     * @param port the port number on which the server socket will listen for incoming connections
     * @param host the hostname or IP address to bind the server socket
     * @param rmiServer the RMI handler interface used for handling remote client operations
     * @throws RuntimeException if there is an error initializing the server socket
     */
    Server(int port, String host, ClientRMIHandlerInterface rmiServer) {
        try {

            this.rmiServer = rmiServer;

            serverSocket = new ServerSocket(port, 10, InetAddress.getByName(host));

            this.serverSocket.setReuseAddress(true);

            clients = new HashMap<>();

            games = new HashMap<>();

            System.out.println("Server started at port " + port + " and with address " + serverSocket.getInetAddress());

        } catch (IOException e) {
            throw new RuntimeException("Could not load the server: error in constructor of class Server" + e.getMessage() + ")");
        }
    }

    /**
     * Returns the singleton instance of the {@code Server} class.
     * If the instance has not been initialized yet, a {@code RuntimeException} is thrown.
     *
     * @return the singleton {@code Server} instance
     * @throws RuntimeException if the server instance has not been initialized
     */
    public static synchronized Server getInstance() {

        if (instance == null) {
            throw new RuntimeException("The server has not been initialized yet(from getInstance in class Server)");
        } else {
            return instance;
        }
    }

    /**
     * Returns the singleton instance of the {@code Server} class, creating it
     * if it has not yet been instantiated. The server is initialized to listen
     * on the specified port if no instance currently exists.
     *
     * @param port the port number on which the server socket will listen for connections
     * @return the singleton {@code Server} instance
     */
    public static synchronized Server getInstance(int port) {
        if (instance == null) {
            instance = new Server(port);
        }

        return instance;
    }

    /**
     * Returns the singleton instance of the {@code Server} class.
     * If the instance has not been created, it initializes the server with the specified host, port,
     * and RMI handler interface.
     *
     * @param host      the hostname or IP address to bind the server socket
     * @param port      the port number on which the server socket will listen for connections
     * @param serverRMI the RMI handler interface used for handling remote client operations
     * @return the singleton {@code Server} instance
     */
    public static synchronized Server getInstance(String host, int port, ClientRMIHandlerInterface serverRMI) {
        if (instance == null) {
            instance = new Server(port, host, serverRMI);
        }
        return instance;
    }

    /**
     * Closes the server socket, releasing any resources associated with it.
     * This method ensures that the server socket is properly shut down,
     * preventing any further client connections. If an I/O exception occurs
     * during the closing operation, an error message will be logged to the console.
     */
    public void close() {
        try {
            serverSocket.close();
        }
        catch (IOException e) {
            System.out.println("Error closing server: " + e.getMessage());
        }
    }

    /**
     * Disconnects all players related to the specified game and username.
     * This method handles removing the specified player from both the active
     * and inactive player lists of the game and ensures all relevant client connections
     * are closed. A message is sent to affected clients indicating the game has ended.
     *
     * @param gameId   the unique identifier of the game from which players should be disconnected
     * @param username the username of the player to be removed and whose related players' connections
     *                 need to be terminated
     */
    public void disconnectAll(int gameId, String username) {

        synchronized (games.get(gameId)) {
            Game game = games.get(gameId);
            Iterator<Player> it = game.getPlayers().iterator();
            while (it.hasNext()) {
                Player p = it.next();
                if (p.getNickname().equals(username)) {
                    it.remove();  // rimuove in modo sicuro l’elemento corrente
                    break;        // esce se vuoi rimuovere solo il primo match
                }
            }
            Iterator<Player> it2 = game.getPlayersNotOnFlight().iterator();
            while (it2.hasNext()) {
                Player p = it2.next();
                if (p.getNickname().equals(username)) {
                    it2.remove();  // rimuove in modo sicuro l’elemento corrente
                    break;        // esce se vuoi rimuovere solo il primo match
                }
            }
            List<String> players = games.get(gameId).getPlayers().stream().map(Player::getNickname).collect(Collectors.toList());
            List<String> notOnFlight = games.get(gameId).getPlayersNotOnFlight().stream().map(Player::getNickname).toList();
            players.addAll(notOnFlight);
            try {
                for (String s : clients.keySet()) {
                    String user = getUsernameForConnection(s);
                    if (players.contains(user)) {
                        try {
                            SocketHandler socketHandler = clients.get(s);
                            socketHandler.sendMessage(new DirectMessage(new MatchAbandoned("La partita è terminata perchè un player è uscito\n")));
                            socketHandler.close();
                            System.out.println("Chiuso client: " + s);
                        } catch (RuntimeException e) {
                            System.out.println("Eccezione lanciata nel disconnectAll di Server" + e.getMessage());
                        }
                        players.remove(getUsernameForConnection(s));
                    }
                }
                rmiServer.disconnectAll(players);
            } catch (RemoteException e) {
                System.out.println("Messaggio inviato con esito negativo");
            }
        }
    }

    public synchronized ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * Handles the connection of new clients to the server, assigns them to available games,
     * or facilitates the creation of new games. Validates the client's choices and ensures
     * they are appropriately added to game lobbies based on their selection.
     *
     * @param nameConnection The unique identifier for the client connection.
     *                        Used to identify the client within the server system.
     */
    public void connectClients(String nameConnection) {
        if (clients.containsKey(nameConnection)) {
            throw new RuntimeException("La connessione è già presente!!");
        }
        synchronized (serverSocket) {
            try {

                Socket socket = serverSocket.accept();
                SocketHandler socketHandler = new SocketHandler(socket);

                // Per prima cosa il server comunica al client che entra gli id delle partite disponibili a cui partecipare
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
                socketHandler.sendMessage(new DirectMessage(new LobbyAvailable(matchesAvailable)));

                /*Adesso devo adottare due comportamenti diversi in base alla scelta del client, ovvero se vorrà
                creare una nuova partita o partecipare ad una partita già esistente.
                A questo proposito al client vengono mostrati gli id delle partite in corso in modo che lui possa
                scegliere a quale partecipare o creare direttamente un'altra partita
                */
                int choice = 0;
                boolean loop = false;
                do {
                    UserDecision userChoice = (UserDecision) socketHandler.readMessage().call(new GetActionVisitor());
                    choice = userChoice.getChoice();   // arriva 1-based, per questo faccio sempre choice-1
                    if(choice != 0 && (!games.keySet().contains(choice - 1) || games.get(choice - 1).getGameStatus() != GameStatus.Setup)){
                        loop = true;
                        socketHandler.sendMessage(new DirectMessage(new ErrorResponse("Inserimento non valido")));
                    }
                    else{
                        loop = false;
                    }
                } while(loop);

                int gameIdConsidering = 0;
                // Vuol dire che il player vuole creare una partita
                if (choice == 0){
                    gameIdConsidering = games.size();
                    UsersConnected.getInstance().addGame();
                }
                // Il player vuole accedere alla partita con l'id da lui inserito
                // (È importante ricordare che il player inserisce un indice 1-based mentre in UsersConnected ed in games
                // gli indici sono 0-based)
                else if(choice < 0 || choice > games.size()){
                    socketHandler.sendMessage(new DirectMessage(new ErrorResponse("Hai inserito un id errato!")));
                }
                // Caso in cui un player voglia partecipare ad una partita già esistente con un certo id
                else {
                    gameIdConsidering = choice - 1;
                }

                List<String> usersConnected = UsersConnected.getInstance().getClients(gameIdConsidering);

                // TODO: modifica l'oggetto di sincronizzazione
                synchronized (UsersConnected.getInstance()) {
                    clients.put(nameConnection, socketHandler);
                    UsersConnected.getInstance().addClient(nameConnection, gameIdConsidering);
                    System.out.println("Client connected: " + nameConnection);


                    if(UsersConnected.getInstance().getClients(gameIdConsidering).size() != 1 && games.get(gameIdConsidering).getNumRequestedPlayers() == -1) {
                        socketHandler.sendMessage(new DirectMessage(new LobbyUnavailable()));
                        clients.remove(nameConnection);
                        UsersConnected.getInstance().removeClient(nameConnection, gameIdConsidering);
                        throw new LobbyUnavailableException("Lobby unavailable: riavvia il programma");
                    }

                    else if(UsersConnected.getInstance().getClients(gameIdConsidering).size() == 1){

                        // socketHandler.sendMessage(new DirectMessage(new SelectLevel()));

                        Message message = socketHandler.readMessage();

                        System.out.println(message.toString());

                        addGame(new Game(Integer.parseInt(message.toString()), gameIdConsidering));

                        System.out.println("arrivato a questo punto");

                    }
                }

                // Adesso mi occupo di ricevere lo username del player
                // Queste sono le istruzioni necessarie per ricevere lo username dal client, ELABORARLO e permettere
                // al game di aggiungere il player alla lista di player tramite l'handle delle action

                Action a = null;
                String username = null;

                boolean error;

                /*do {
                    try {
                        a = socketHandler.readMessage().call(new GetActionVisitor());
                        username = a.call(new SetUsernameActionVisitor());
                        a.call(new HandleActionVisitor(), username);
                        error = false;
                    }
                    catch(PlayerExistsException e){
                        socketHandler.sendMessage(new DirectMessage(new WrongUsername()));
                        error = true;
                    }
                } while(error);*/

                do {
                        a = socketHandler.readMessage().call(new GetActionVisitor());
                        username = a.call(new SetUsernameActionVisitor());
                        if(UsersConnected.getInstance().usernameAlreadyExists(username)){
                            socketHandler.sendMessage(new DirectMessage(new WrongUsername()));
                            error = true;
                        }
                        else{
                            UsersConnected.getInstance().removeClient(nameConnection, gameIdConsidering);
                            UsersConnected.getInstance().addClient(username, gameIdConsidering);
                            a.call(new HandleActionVisitor(), username);
                            error = false;
                        }
                } while(error);

                socketHandler.sendMessage(new DirectMessage(new AppropriateUsername(username, games.get(gameIdConsidering).getLevel())));
                /*int indiceUsernameDaCambiare = usersConnected.indexOf(username);
                usersConnected.remove(indiceUsernameDaCambiare);
                usersConnected.add(indiceUsernameDaCambiare, username);*/
                socketHandler.setUsername(username);

                /*synchronized (Server.getInstance().getClients()) {
                    if (Server.getInstance().getClients().size() == 1 && UsersConnected.getInstance().getClients().size() != 1) {
                        // Se è chiusa e siamo nel primo player riapriamo la serversocket

                        //Server.getInstance().setServerSocket("localhost", 8000);

                        // In questo punto permettiamo al Server di accettare nuovi client
                        ConnectionThread.getInstance().start();
                    }
                }*/


                if(UsersConnected.getInstance().getClients(gameIdConsidering).size() == games.get(gameIdConsidering).getNumRequestedPlayers()){
                    games.get(gameIdConsidering).getController().startBuildingPhase();
                }


            } catch (IOException e) {
                throw new RuntimeException("Could not accept the client in connectClients in Server" + e.getMessage());
            }

        }
    }


    // Questo metodo ci fornirà l'indirizzo ip del server
    public InetAddress getInetAddress() {

        // Sto ritornando un riferimento all'InetAddress, ma tanto gli InetAddress sono immutabili, quindi
        // non corriamo rischi
        return serverSocket.getInetAddress();

    }


    /**
     * Sends a message to a specified client connection. If the client connection is present in the
     * list of active sockets, the message is sent via the corresponding socket handler. If the
     * connection is not found in the active sockets, it falls back to using the RMI server handler
     * to deliver the message.
     *
     * @param message the {@code Message} object representing the information to be sent to the client
     * @param nameConnection the unique identifier for the client connection, used to look up the
     *                        appropriate socket handler or delegate message delivery through the RMI server
     * @throws RuntimeException if the specified client connection is found in the socket list but is null
     */
    // Metodo incaricato dell'invio dei messaggi ai client. Il parametro nameconnection ci dice quale elemento della lista di
    // socket bisogna considerare
    public void sendMessage(Message message, String nameConnection) {

        if(clients.containsKey(nameConnection)){
            SocketHandler socketHandler = null;

            synchronized (clients) {
                socketHandler = clients.get(nameConnection);
            }

            if (socketHandler != null) {
                System.out.println("Messaggio inviato con esito: " + socketHandler.sendMessage(message));
            } else {
                throw new RuntimeException("No client with name " + nameConnection);
            }
        }

        else{
            try{
                rmiServer.sendToUser(nameConnection, message);
            }
            catch(RemoteException e){
                System.out.println("Messaggio inviato con esito negativo");
            }
        }

    }

    /**
     * Sends a message to a client identified by their username. If the associated connection ID
     * is found in the active client list, the message is sent through that connection. If no
     * such connection ID is found, the message is sent through the RMI server handler.
     *
     * @param username the username identifying the client to whom the message should be sent
     * @param message the {@code Message} object representing the information to be sent to the client
     * @throws RuntimeException if the provided username or message is {@code null}
     */
    public void sendMessage(String username, Message message) {
        if (username != null && message != null) {
            String connectionID = null;
            synchronized (clients) {
                for (String connection : clients.keySet()) {
                    if (clients.get(connection).getUsername().equals(username)) {
                        connectionID = connection;
                        break;
                    }
                }
            }
            if(connectionID != null){
                sendMessage(message, connectionID);
            }
            else{
                try{
                    rmiServer.sendToNickname(username, message);
                } catch (RemoteException e) {
                    System.out.println("Messaggio inviato con esito negativo");
                }
            }

        } else {
            throw new RuntimeException("message or username in Server.sendMessage(username, message) are null (tried to send: " + message.toString() + ")");
        }
    }

    /**
     * Receives a message from a client identified by a given connection name.
     * This method retrieves the corresponding socket handler for the specified connection
     * and attempts to read a message from it.
     *
     * @param nameConnection the unique identifier of the client's connection from which the message should be retrieved
     * @return the {@code Message} object received from the specified connection, or {@code null} if the connection does not exist
     * @throws RuntimeException if an I/O error occurs while receiving the message
     */
    public Message receiveMessage(String nameConnection) {

        SocketHandler socketHandler = null;

        synchronized (clients) {
            socketHandler = clients.get(nameConnection);
        }

        if (socketHandler != null) {
            try {
                return socketHandler.readMessage();
            }
            catch (IOException e) {
                throw new RuntimeException("Error receiving the message: " + e.getMessage());
            }
        }
        return null;
    }

    public void notifyAllPlayers(Message message, int gameId) {
        synchronized (games.get(gameId)) {
            List<String> players = games.get(gameId).getPlayers().stream().map(Player::getNickname).collect(Collectors.toList());
            List<String> notOnFlight = games.get(gameId).getPlayersNotOnFlight().stream().map(Player::getNickname).collect(Collectors.toList());
            players.addAll(notOnFlight);
            try {
                for (String s : clients.keySet()) {
                    String username = getUsernameForConnection(s);
                    if (players.contains(username)) {
                        try {
                            sendMessage(username, message);
                        } catch (RuntimeException e) {
                            System.out.println("Eccezione lanciata nel notifyAllObservers di Server. Se si è disconnesso inaspettatamente un player questo messaggio è del tutto normale, altrimenti c'è un problema!!" + e.getMessage());
                        }
                        System.out.println("Notify observer: " + username + " with message: " + message.toString());
                        players.remove(getUsernameForConnection(s));
                    }
                }
                rmiServer.sendToAllClients(message, players);
            } catch (RemoteException e) {
                System.out.println("Messaggio inviato con esito negativo");
            }
        }
    }

    /**
     * Notifies all active observers (players) of a specific game with the given message.
     * This method attempts to send the message to each client associated with the game. If a player's
     * connection cannot be reached via the active socket handlers, the RMI server is used as a fallback
     * to deliver the message to any remaining clients.
     *
     * @param message the {@code Message} object containing the information to be sent to the observers
     * @param gameId the unique identifier of the game whose observers are to be notified
     */
    public void notifyAllObservers(Message message, int gameId) {
        synchronized (games.get(gameId)) {
            List<String> players = games.get(gameId).getPlayers().stream().map(Player::getNickname).collect(Collectors.toList());
            try {
                for (String s : clients.keySet()) {
                    String username = getUsernameForConnection(s);
                    if (players.contains(username)) {
                        try {
                            sendMessage(username, message);
                        } catch (RuntimeException e) {
                            System.out.println("Eccezione lanciata nel notifyAllObservers di Server. Se si è disconnesso inaspettatamente un player questo messaggio è del tutto normale, altrimenti c'è un problema!!" + e.getMessage());
                        }
                        System.out.println("Notify observer: " + username + " with message: " + message.toString());
                        players.remove(getUsernameForConnection(s));
                    }
                }
                rmiServer.sendToAllClients(message, players);
            } catch (RemoteException e) {
                System.out.println("Messaggio inviato con esito negativo");
            }
        }
    }

    // Metodo per chiudere la connessione con un solo client specifico
    public void closeConnection(String nameConnection) {
        synchronized (clients) {
            SocketHandler socketHandler = clients.get(nameConnection);

            if (socketHandler != null) {
                socketHandler.close();
                clients.remove(nameConnection);
            }
        }
    }


    public int getSize() {
        return clients.size();
    }


    public String getNameConnection(SocketHandler socketHandler) {
        for (String key : clients.keySet()) {
            if (clients.get(key) == socketHandler) {
                return key;
            }
        }
        return null;
    }

    public HashMap<String, SocketHandler> getClients() {
        return new HashMap<>(clients);
    }

    /**
     * Retrieves the username associated with the specified client connection ID.
     *
     * @param connectionID the unique identifier of the client connection whose username is to be retrieved
     * @return the username of the client associated with the given connection ID
     */
    public String getUsernameForConnection(String connectionID) {
        return clients.get(connectionID).getUsername();
    }

    /**
     * Retrieves the {@code Game} instance associated with the specified unique identifier.
     *
     * @param id the unique identifier of the game to be retrieved
     * @return the {@code Game} object corresponding to the given identifier, or {@code null} if no such game exists
     */
    public Game getGame(int id) {
        return games.get(id);
    }

    /**
     * Adds a game to the server's game collection and sets its controller.
     *
     * @param game the {@code Game} instance to be added to the server's collection
     */
    public void addGame(Game game) {
        games.put(game.getId(), game);
        game.setController();
    }

    public synchronized int getGamesSize(){
        return games.size();
    }

    /**
     * Retrieves a list of all currently available games managed by the server.
     *
     * @return a synchronized list of {@code Game} objects representing the active games on the server
     */
    public synchronized List<Game> getGames() {
        return games.values().stream().toList();
    }

}
