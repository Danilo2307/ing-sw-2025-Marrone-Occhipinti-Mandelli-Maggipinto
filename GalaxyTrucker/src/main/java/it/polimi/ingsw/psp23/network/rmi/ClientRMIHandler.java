package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.controller.Controller;
import it.polimi.ingsw.psp23.exceptions.GameException;
import it.polimi.ingsw.psp23.exceptions.InvalidCoordinatesException;
import it.polimi.ingsw.psp23.exceptions.LobbyUnavailableException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.Game.Player;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.BroadcastMessage;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.request.HandleActionVisitor;
import it.polimi.ingsw.psp23.protocol.response.ErrorResponse;
import it.polimi.ingsw.psp23.protocol.response.IncorrectWelding;
import it.polimi.ingsw.psp23.protocol.response.SelectLevel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientRMIHandler extends UnicastRemoteObject implements ClientRMIHandlerInterface {
    private final ClientRegistryInterface registry;

    public ClientRMIHandler(ClientRegistryInterface registry) throws RemoteException{
        super();
        this.registry = registry;
    }

    @Override
    public void registerClient(String username, String nameConnection, ClientCallbackInterface callback) throws RemoteException{

        List<String> usersConnected = UsersConnected.getInstance().getClients();

        synchronized (usersConnected) {
            UsersConnected.getInstance().addClient(nameConnection);
            registry.registerClient(nameConnection, callback);
            System.out.println("Client connected: " + nameConnection);

            /*if(UsersConnected.getInstance().getClients().size() == 1){

                //socketHandler.sendMessage(new DirectMessage(new SelectLevel()));
                Message message = (new DirectMessage(new SelectLevel()));

                callback.onReceivedMessage(message);

                // System.out.println(message.toString());

            }*/
            if(UsersConnected.getInstance().getClients().size() != 1 && Game.getInstance().getNumRequestedPlayers() == -1){
                UsersConnected.getInstance().removeClient(nameConnection);
                throw new LobbyUnavailableException("lobby is unavailable");
            }
        }



    }

    @Override
    public void heartbeat(String username) throws RemoteException{}


    /**
     * Invia un messaggio a TUTTI i client RMI registrati.
     */
    @Override
    public void sendToAllClients(Message msg) throws RemoteException {
        for (ClientCallbackInterface cb : registry.getAllClients()) {
            try {
                cb.onReceivedMessage(msg);
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
            List<DirectMessage> dm = action.getDm();
            List<BroadcastMessage> bm = action.getBm();

            if(dm != null && !dm.isEmpty()){
                for(Message m : dm) {
                    sendToUser(nameConnection, m);
                }
                dm.clear();
            }
            if(bm != null && !bm.isEmpty()){
                for(Message m : bm) {
                    for(Player p : Game.getInstance().getPlayers()) {
                        if(registry.getAllPlayers().contains(p.getNickname()))
                            sendToUser(registry.getPlayerConnectionFromNickname(p.getNickname()), m);
                    }
                }
                bm.clear();
            }

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
        Game.getInstance(level);
    }

    @Override
    public int getNumPlayersConnected() throws RemoteException {
        return UsersConnected.getInstance().getClients().size();
    }

    @Override
    public void setPlayerUsername(String username) throws RemoteException{
        Controller.getInstance().addPlayerToGame(username);
    }

    @Override
    public void setNumRequestedPlayers(int num) throws RemoteException{
        Game.getInstance().setNumRequestedPlayers(num);
    }

    @Override
    public int getNumRequestedPlayers() throws RemoteException{
        return Game.getInstance().getNumRequestedPlayers();
    }

    @Override
    public void startBuildingPhase() throws RemoteException{
        Controller.getInstance().startBuildingPhase();
    }
}
