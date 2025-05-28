package it.polimi.ingsw.psp23.network;

import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.rmi.ClientRMIHandlerInterface;
import it.polimi.ingsw.psp23.network.socket.SocketHandler;
import it.polimi.ingsw.psp23.protocol.request.Action;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public abstract class Client {
    public void sendAction(Action action) throws RemoteException{

    }
    public boolean isRmi() throws RemoteException{
        return false;
    }
    public void open() throws RemoteException{}
    public void sendMessage(Message message){}
    public Socket getSocket(){return null;}
    public Message readMessage() throws SocketTimeoutException{return null;}
    public void close() throws RemoteException, NotBoundException{};
    public void avvia(){};
    public void setUsername(String username) throws RemoteException{};
    public ClientRMIHandlerInterface getGameServer() throws RemoteException{return null;};
    public SocketHandler getSocketHandler(){return null;};
    public void stopListeningForServerThread(){};
    public String getNameConnection() throws RemoteException{return null;}
}
