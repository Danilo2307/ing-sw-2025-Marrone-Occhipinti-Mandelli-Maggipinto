package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.network.messages.GetEventVisitor;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.protocol.response.Event;
import it.polimi.ingsw.psp23.protocol.response.HandleEventVisitor;
import it.polimi.ingsw.psp23.view.ClientEventHandler;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



/**
 * The ClientCallback class serves as a mechanism for handling communication between the server
 * and a client in a distributed system. It is an implementation of the
 * {@link ClientCallbackInterface} and extends {@link UnicastRemoteObject} to facilitate
 * remote method invocation (RMI). This class is responsible for processing server commands,
 * handling errors, disconnecting clients, and implementing heartbeat functionality.
 *
 * Responsibilities:
 * - Receiving messages from the server and delegating their handling via the {@link ClientEventHandler}.
 * - Displaying error messages on the client-side view.
 * - Managing disconnection of the client and providing appropriate notifications to the user.
 * - Responding to heartbeat checks to indicate the client's connected state.
 */
public class ClientCallback extends UnicastRemoteObject implements ClientCallbackInterface {
    private final ClientEventHandler handler;
    private final ViewAPI view;

    protected ClientCallback(ClientEventHandler handler) throws RemoteException {
        super();
        this.handler = handler;
        this.view = handler.getView();
    }

    @Override
    public void onReceivedMessage(Message message) throws RemoteException{
        Event e = message.call(new GetEventVisitor());
        e.call(new HandleEventVisitor(), view);
    }

    @Override
    public void onError(String error) throws RemoteException{
        view.showError(error);
    }

    @Override
    public void disconnectClient() throws RemoteException {
        view.stopMatch("La partita è terminata perchè un player è uscito\n");
        UnicastRemoteObject.unexportObject(this, true);
    }

    @Override
    public void sendHeartbeat() throws RemoteException {}
}
