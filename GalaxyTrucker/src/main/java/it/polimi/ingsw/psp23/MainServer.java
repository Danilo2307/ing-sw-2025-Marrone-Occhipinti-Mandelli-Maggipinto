package it.polimi.ingsw.psp23;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.messages.DirectMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.ConnectionThread;
import it.polimi.ingsw.psp23.network.socket.Server;
import it.polimi.ingsw.psp23.network.socket.Users;
import it.polimi.ingsw.psp23.protocol.response.RequestNumPlayers;

import java.util.UUID;


public class MainServer {
    public static void main(String[] args) {
        Server.getInstance("localhost", 8000);

        // attendo primo client: salvo il suo username e decido numero di avversari
        String connectionId = UUID.randomUUID().toString();
        Server.getInstance().connectClients(connectionId);

        Users.getInstance().createClientHandler(connectionId);
        Server.getInstance().sendMessage(new DirectMessage(new RequestNumPlayers()), connectionId);

        try {
            ConnectionThread.getInstance().join(40000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Game.getInstance().getPlayers().forEach(player -> {System.out.println(player.getNickname());});

    }
}
