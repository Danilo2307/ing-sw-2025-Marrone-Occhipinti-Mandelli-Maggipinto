package it.polimi.ingsw.psp23;

import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.Game.*;
import it.polimi.ingsw.psp23.model.components.*;
import java.util.Random;
import java.util.ArrayList;


public class Player {
    private final String nickname;
    private final int position ;
    private final Board truck;
    private int money;
    private boolean inGame;
    private boolean isConnected;
    private Game game;

    public Player(String nickname) {
        this.nickname = nickname;
        this.position = 0;
        this.truck = new Board();
        this.money = 0;
        this.inGame = true;
        this.isConnected = true;
    }

    /**
     * Setta il game a cui il player sta partecipando
     * @game è il riferimento alla sessione di gioco a cui il player partecipa
     */
    public void setGame(Game game){
        this.game = game;
    }

    public boolean isInGame() {
        return inGame;
    }

    public Board getTruck() {
        return truck;
    }

    public void updateMoney(int moneyVariation) {
        this.money += moneyVariation;
    }

    // TO DO: updateposition

    public int getPosition() {
        return position;
    }

    public void leaveGame() {
        this.inGame = false;
        // il metodo sort() presente in Game provvederà a spostare il Player da players a playersNotInGame
    }

    public String getNickname() {
        return nickname;
    }

    public int getMoney() {
        return money;
    }

    public boolean getConnected(){
        return isConnected;
    }

    public void setDisconnected(){
        this.isConnected = false;
    }

    public void setConnected(){
        this.isConnected = true;
    }

    public Component chooseTileFromHeap() throws NotCardInHandException{
        try{
            Component component = game.getTileFromHeap();
            return component;
        }
        catch(HeapIsEmptyException e){
            throw new NotCardInHandException();
        }
    }

    public Component chooseCardUncovered(int position) throws NotCardInHandException{
        try{
            Component component = game.getTileUncovered(position);
            return component;
        }
        catch(UncoveredIsEmptyException e){
            throw new NotCardInHandException();
        }
    }

    public void discardComponent(Component component){
        game.addTileUncovered(component);
    }

}
