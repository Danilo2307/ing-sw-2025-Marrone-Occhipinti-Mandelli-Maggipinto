package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.model.Game.Game;

import java.util.UUID;

/*
 * Thread used to connect players to my game. When the game starts, we have to call "ConnectionThread.getInstance().start();"
 * so that the server starts listening for new clients
 * ConnectionThread È SINGLETON!!!!
 */
public class ConnectionThread extends Thread{

    public static ConnectionThread instance = null;

    public boolean listening = false;

    public ConnectionThread(){}

    public static ConnectionThread getInstance(){
        if(instance == null){
            instance = new ConnectionThread();
        }
        return instance;
    }

    @Override
    public void run() {
        listening = true;
        while(true){

            String connectionId = UUID.randomUUID().toString();

            Server.getInstance().connectClients(connectionId);

            Users.getInstance().createClientHandler(connectionId);

        }
    }

    public boolean isListening(){
        return listening;
    }
}
