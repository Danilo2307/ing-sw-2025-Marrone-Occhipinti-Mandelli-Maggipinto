package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.network.messages.GetEventVisitor;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.protocol.response.Event;
import it.polimi.ingsw.psp23.protocol.response.HandleEventVisitor;
import it.polimi.ingsw.psp23.view.ClientEventHandler;
import it.polimi.ingsw.psp23.view.ViewAPI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
}
