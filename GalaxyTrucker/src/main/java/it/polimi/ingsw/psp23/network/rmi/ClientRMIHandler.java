package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.exceptions.GameException;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.protocol.request.Action;
import it.polimi.ingsw.psp23.protocol.request.HandleActionVisitor;
import it.polimi.ingsw.psp23.protocol.response.ErrorResponse;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRMIHandler extends UnicastRemoteObject implements ClientRMIHandlerInterface {
    private final Map<String, ClientCallbackInterface> clients = new ConcurrentHashMap<>();

    protected ClientRMIHandler() throws RemoteException{
        super();
    }

    @Override
    public void registerClient(String username, ClientCallbackInterface callback) throws RemoteException{
        clients.put(username, callback);
    }

    @Override
    public void heartbeat(String username) throws RemoteException{}

    @Override
    public void sendAction(Action action, String username) throws RemoteException{
        try {
            action.call(new HandleActionVisitor(), username);
        }
        catch(GameException e) {
            ClientCallbackInterface caller = clients.get(username);
            if (caller != null) {
                caller.onError(e.getMessage());
            }
        }
    }

}
