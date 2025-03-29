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

    public void drawFromHeap() throws NotCardInHandException{
        try{
            int index = rand.nextInt(heap.size());
            // sincronizzo su heap così che solo un Player alla volta possa rimuovere un component dal mucchio.
            synchronized (heap) {
                randomComponent = heap.remove(index);  // rimuove dalla lista l'elemento heap(index) e lo restituisce
            }
            randomComponent.moveToHand();
        }
        catch(HeapIsEmptyException e){
            throw new NotCardInHandException();
        }
    }

    public void chooseCardCovered() throws NotCardInHandException{
        try{

        }
        catch(UncoveredIsEmptyException e){
            throw new NotCardInHandException();
        }
    }

}
