package it.polimi.ingsw.psp23;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.socket.ConnectionThread;
import it.polimi.ingsw.psp23.network.socket.Server;


public class MainServer {
    public static void main(String[] args) {
        Server.getInstance("localhost", 8000);
        ConnectionThread.getInstance().start();
        try {
            ConnectionThread.getInstance().join(10000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Game.getInstance().getPlayers().forEach(player -> {System.out.println(player.getNickname());});

    }
}
