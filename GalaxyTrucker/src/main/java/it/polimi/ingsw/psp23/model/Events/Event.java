package it.polimi.ingsw.psp23.model.Events;

import it.polimi.ingsw.psp23.Item;
import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.enumeration.Challenge;
import it.polimi.ingsw.psp23.model.enumeration.EventType;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.ArrayList;
import java.util.List;

//classe che definisce la struttura dell'evento
public class Event {

    private GameStatus newStatus;
    private int numPianeti;
    private int daysLost;
    String items1;
    String items2;
    String items3;
    String items4;

    private int cosmicCredits;
    private int numMembers;

    private int numGoodsLost;
    String challenge1;
    String challenge2;
    String challenge3;
    String cannonShot;

    //costruttore per carta Pianeti
    //passo delle stringhe con i colori delle merci poichè saranno soltanto da presentare al player
    public Event(GameStatus newStatus, int daysLost,List<List<Item>> planetGoods) {
        this.newStatus = newStatus;
        this.numPianeti = planetGoods.size();
        this.daysLost = daysLost;
        items1 ="";
        items2 ="";
            for(Item goods : planetGoods.get(0)) {
                items1 += (goods.toString() + " ");
            }

            for(Item goods : planetGoods.get(1)){
                items2 += (goods.toString() + " ");
            }
        if(numPianeti == 3){
            items3 = "";
            for(Item goods : planetGoods.get(2)){
                items3 += (goods.toString() + " ");
            }
        }else if(numPianeti == 4){
            items3 = "";
            items4 = "";
            for(Item goods : planetGoods.get(2)){
                items3 += (goods.toString() + " ");
            }

            for(Item goods : planetGoods.get(3)){
                items4 += (goods.toString() + " ");
            }
        }
    }

    //costruttore per carta AbandonedShip
    public Event(GameStatus newStatus,int days, int cosmicCredits, int numMembers){
        this.newStatus = newStatus;
        this.daysLost = days;
        this.cosmicCredits = cosmicCredits;
        this.numMembers = numMembers;
    }

    //costruttore per carta AbandonedStation
    public Event(GameStatus newStatus, int days, int numMembers, List<Item> prize){
        this.newStatus = newStatus;
        this.daysLost = days;
        this.numMembers = numMembers;
        items1 = "";
        for(Item goods : prize){
            items1 += (goods.toString() + " ");
        }
    }

    //costruttore per Combat Zone
    public Event(GameStatus newStatus, int daysLost, int goodsLost, int membersLost, Challenge challenge1, Challenge challenge2, Challenge challenge3, List<CannonShot> cannonShot){
        this.newStatus = newStatus;
        this.daysLost = daysLost;
        this.numMembers = membersLost;
        this.numGoodsLost = goodsLost;
        this.challenge1 = challenge1.toString();
        this.challenge2 = challenge2.toString();
        this.challenge3 = challenge3.toString();
        this.cannonShot = "";
        for(CannonShot cs : cannonShot){
            this.cannonShot += (cs.toString() + " ");
        }
    }

    //costruttore per MeteorSwarm
    public Event(GameStatus newStatus){

    }
    //mancano le altre carte da fare
    //
     //
     //
     //
     //
     //
     //
     //
     //


    public int getDaysLost() {
        return daysLost;
    }
    public GameStatus getNewStatus() {
        return newStatus;
    }
    public int getNumMembers() {
        return numMembers;
    }
    public int getNumGoodsLost() {
        return numGoodsLost;
    }
    public String getChallenge1() {
        return challenge1;
    }
    public String getChallenge2() {
        return challenge2;
    }
    public String getChallenge3() {
        return challenge3;
    }
    public String getCannonShots() {
        return cannonShot;
    }
    public int getNumPianeti() {
        return numPianeti;
    }
    public int getCosmicCredits() {
        return cosmicCredits;
    }
    public String getItems1() {
        return items1;
    }
    public String getItems2() {
        return items2;
    }
    public String getItems3() {
        return items3;
    }
    public String getItems4() {
        return items4;
    }


}
