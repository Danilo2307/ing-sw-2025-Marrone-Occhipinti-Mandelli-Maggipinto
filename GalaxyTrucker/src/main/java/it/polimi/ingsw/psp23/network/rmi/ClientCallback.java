package it.polimi.ingsw.psp23.network.rmi;

import it.polimi.ingsw.psp23.view.ViewAPI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientCallback extends UnicastRemoteObject implements ClientCallbackInterface {
    private final ViewAPI view;

    protected ClientCallback(ViewAPI view) throws RemoteException {
        super();
        this.view = view;
    }

    @Override
    public void onReceivedMessage(String msg) throws RemoteException{
        view.showMessage(msg);
    }

    @Override
    public void onError(String error) throws RemoteException{
        view.showError(error);
    }
}
