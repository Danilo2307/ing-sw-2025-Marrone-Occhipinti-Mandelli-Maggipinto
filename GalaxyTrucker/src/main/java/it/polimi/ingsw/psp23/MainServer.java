package it.polimi.ingsw.psp23;

import it.polimi.ingsw.psp23.exceptions.LobbyUnavailableException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.UsersConnected;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.rmi.ClientRegistry;
import it.polimi.ingsw.psp23.network.rmi.ClientRegistryInterface;
import it.polimi.ingsw.psp23.network.rmi.ClientRMIHandlerInterface;
import it.polimi.ingsw.psp23.network.rmi.ClientRMIHandler;
import it.polimi.ingsw.psp23.network.socket.ConnectionThread;
import it.polimi.ingsw.psp23.network.Server;
import it.polimi.ingsw.psp23.network.socket.Users;
import it.polimi.ingsw.psp23.protocol.response.RequestNumPlayers;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.UUID;


public class MainServer {
    public static void main(String[] args) throws RemoteException {

        System.setProperty("java.rmi.server.hostname", "172.26.190.81");

        // 2) --- Avvio del registry RMI sulla 1099 ---
        Registry rmiRegistry = LocateRegistry.createRegistry(4321);

        // 3) --- Bind del ClientRegistry (mantiene gli stub callback) ---
        ClientRegistryInterface clientRegistry = new ClientRegistry();
        rmiRegistry.rebind("ClientRegistry", clientRegistry);

        // 4) --- Bind del GameServer RMI (deleghe a clientRegistry) ---
        ClientRMIHandlerInterface rmiServer = new ClientRMIHandler(clientRegistry);
        rmiRegistry.rebind("GameServer", rmiServer);

        System.out.println("RMI registry avviato su port 4321");

        Server.getInstance("172.26.190.81", 8000, rmiServer);

        // attendo primo client: salvo il suo username e decido numero di avversari
        String connectionId = UUID.randomUUID().toString();

        try{
            Server.getInstance().connectClients(connectionId);
            Users.getInstance().createClientHandler(connectionId);
            /*if(UsersConnected.getInstance().getClients().size() == 1) {
                Server.getInstance().sendMessage(new DirectMessage(new RequestNumPlayers()), connectionId);
            }*/

            try {
                ConnectionThread.getInstance().join(40000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Game.getInstance().getPlayers().forEach(player -> {System.out.println(player.getNickname());});
        }
        catch (LobbyUnavailableException e){
            System.out.println(e.getMessage());
        }



    }
}
