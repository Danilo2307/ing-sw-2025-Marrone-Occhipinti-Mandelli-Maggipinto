package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.exceptions.GameException;
import it.polimi.ingsw.psp23.exceptions.InvalidCoordinatesException;
import it.polimi.ingsw.psp23.exceptions.PlayerExistsException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.ConnectionThread;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.request.HandleActionVisitor;
import it.polimi.ingsw.psp23.protocol.response.ErrorResponse;
import it.polimi.ingsw.psp23.protocol.response.IncorrectWelding;
import it.polimi.ingsw.psp23.protocol.response.MatchFinished;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientRMIHandler extends UnicastRemoteObject implements ClientRMIHandlerInterface {
    private final ClientRegistryInterface registry;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public ClientRMIHandler(ClientRegistryInterface registry) throws RemoteException{
        super();
        this.registry = registry;
    }

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

    @Override
    public void heartbeat(String username) throws RemoteException{}


    /**
     * Invia un messaggio a TUTTI i client RMI registrati.
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
     * Invia un messaggio RMI a un singolo client identificato da username.
     * Se il client non è registrato o non risponde, viene loggato e rimosso.
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
            System.err.println("sendToUser: impossibile inviare a \"" + nameConnection + "\": " + e.getMessage());
        }
    }

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
            System.err.println("sendToUser: impossibile inviare a \"" + nameConnection + "\": " + e.getMessage());
        }
    }

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

    @Override
    public void setGameLevel(int level) throws RemoteException {
        UsersConnected.getInstance().addGame();
        int gameIdConsidering = Server.getInstance().getGamesSize();
        Server.getInstance().addGame(new Game(level, gameIdConsidering));
    }

    @Override
    public int getGameLevel(int gameId) throws RemoteException {
        return Server.getInstance().getGame(gameId).getLevel();
    }

    @Override
    public GameStatus getGameStatus(int gameId) throws RemoteException {
        return Server.getInstance().getGame(gameId).getGameStatus();
    }

    @Override
    public int getNumPlayersConnected(int gameId) throws RemoteException {
        return UsersConnected.getInstance().getClients(gameId).size();
    }

    @Override
    public void setPlayerUsername(String username, int gameId) throws RemoteException{
        // Controller.getInstance().addPlayerToGame(username);
        if(UsersConnected.getInstance().usernameAlreadyExists(username)){
            throw new PlayerExistsException("");
        }
        UsersConnected.getInstance().addClient(username, gameId);
        UsersConnected.getInstance().getGameFromUsername(username).getController().addPlayerToGame(username);
    }

    @Override
    public void setNumRequestedPlayers(int num, String username) throws RemoteException{
        UsersConnected.getInstance().getGameFromUsername(username).setNumRequestedPlayers(num);
        if(!ConnectionThread.getInstance().isListening()) {
            ConnectionThread.getInstance().start();
        }
    }

    @Override
    public int getNumRequestedPlayers(int gameId) throws RemoteException{
        return Server.getInstance().getGame(gameId).getNumRequestedPlayers();
    }

    @Override
    public void startBuildingPhase(int gameId) throws RemoteException{
        Server.getInstance().getGame(gameId).getController().startBuildingPhase();
    }

    @Override
    public int getGamesSize() throws RemoteException{
        return Server.getInstance().getGamesSize();
    }

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
                        // Server.getInstance().notifyAllObservers(new BroadcastMessage(new MatchFinished("La partita è terminata perchè un player è uscito")), gameId);
                        Server.getInstance().disconnectAll(gameId, nickname);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void disconnectAll(List<String> players) throws RemoteException {
        for(String player : players) {
            String nameConnection = registry.getPlayerConnectionFromNickname(player);
            registry.unregisterClient(nameConnection);
        }
    }
}
