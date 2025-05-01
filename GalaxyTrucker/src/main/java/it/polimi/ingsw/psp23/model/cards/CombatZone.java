package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.Challenge;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

import java.util.List;
import java.util.ArrayList;

public class CombatZone extends Card{
    // Danilo

    Challenge penalty1;
    Challenge penalty2;
    Challenge penalty3;
    private final int daysLost;
    private final int goodsLost;
    private final int membersLost;
    private final List<CannonShot> cannonShot;

    public CombatZone(int level,int daysLost, int goodsLost, int membersLost,Challenge penalty1,Challenge penalty2, Challenge penalty3, List<CannonShot> cannonshot) {
        super(level);
        this.daysLost = daysLost;
        this.goodsLost = goodsLost;
        this.membersLost = membersLost;
        this.penalty1 = penalty1;
        this.penalty2 = penalty2;
        this.penalty3 = penalty3;
        this.cannonShot = cannonshot;
    }

    public Challenge getFirstPenalty() {
        return penalty1;
    }

    public Challenge getSecondPenalty() {
        return penalty2;
    }

    public Challenge getThirdPenalty() {
        return penalty3;
    }

    public int getDaysLost() {
        return daysLost;
    }

    public int getGoodsLost() {
        return goodsLost;
    }

    public int getMembersLost() {
        return membersLost;
    }

    public List<CannonShot> getCannonShot() {
        return new ArrayList<CannonShot>(cannonShot);
    }

    /*public void setIndex(int numeroPenalità){
        if(numeroPenalità >= 1 && numeroPenalità <= 3) {
            index = numeroPenalità;
        }
        else{
            throw new IndexOutOfBoundsException("Eccezione lanciata nel metodo setIndex presente in combatZone");
        }
    }*/

    @Override
    public Object call(Visitor visitor){
        return visitor.visitForCombatZone(this);
    }

    @Override
    public Object call(VisitorParametrico visitorParametrico, int index){
        if(index < 1 || index > 3) {
            throw new IndexOutOfBoundsException("Eccezione lanciata nel metodo setIndex presente in combatZone");
        }
        return visitorParametrico.visitForCombatZone(this, index);
    }

    public void initPlay(){
        Game.getInstance().setGameStatus(GameStatus.INIT_COMBATZONE);
        Game.getInstance().fireEvent(new Event(Game.getInstance().getGameStatus(), daysLost, goodsLost, membersLost, penalty1, penalty2, penalty3, cannonShot));
    }

   public void play() {
        int impactLine;
        int pos;
        int size = Game.getInstance().getPlayers().size();
        List<Player> players = Game.getInstance().getPlayers();
        int tmp = -1;
        Player playerTmp;


        //inizio prima sfida

        if (penalty1 == Challenge.Members) {
            playerTmp = findMinMembers();

            System.out.println(playerTmp.getNickname() + " has less crew members!");
            Utility.updatePosition(players, players.indexOf(playerTmp), -daysLost);
        } else if (penalty1 == Challenge.CannonStrength) {
            playerTmp = findMinCannonStrength();
            System.out.println(playerTmp.getNickname() + " has less cannon strength!");
            Utility.updatePosition(players, players.indexOf(playerTmp), -daysLost);
        } else {
            throw new IllegalArgumentException("Eccezione in combatzone ");
        }

        //inizio seconda sfida
        playerTmp = findMinEngineStrength();

        System.out.println(playerTmp.getNickname() + " has less engine strength!");

        if (penalty2 == Challenge.Members) {
            // dobbiamo sapere da che cabina togliere l'equipaggio
            if(inputObject.getNumHousingUnits() == 1){
                playerTmp.getTruck().reduceCrew(inputObject.getHousingUnits().get(0)[0],inputObject.getHousingUnits().get(0)[1] , membersLost);
            }else{
                playerTmp.getTruck().reduceCrew(inputObject.getHousingUnits().get(0)[0],inputObject.getHousingUnits().get(0)[1] , 1);
                playerTmp.getTruck().reduceCrew(inputObject.getHousingUnits().get(1)[0],inputObject.getHousingUnits().get(1)[1] , 1);
            }
        } else if (penalty2 == Challenge.Goods) {

            for(int i = 0; i < inputObject.getContainers().size(); i++){
                playerTmp.getTruck().removePreciousItemFromContainer(inputObject.getContainers().get(i)[0],inputObject.getContainers().get(i)[1],inputObject.getToRemove().get(i));
            }


        } else {
            throw new IllegalArgumentException("Eccezione in combatzone ");
        }

        //inizio terza sfida
        if (penalty3 == Challenge.Members) {
            playerTmp = findMinMembers();

            System.out.println(playerTmp.getNickname() + " has less crew members!");
            int i = 0;
            for (CannonShot c : cannonShot) {
                impactLine = Utility.roll2to12();
                playerTmp.getTruck().handleCannonShot(c, impactLine);
                i++;
            }


        } else if (penalty3 == Challenge.CannonStrength) {
            playerTmp = findMinCannonStrength();
            System.out.println(playerTmp.getNickname() + " has less cannon strength!");
            int i = 0;
            for (CannonShot c : cannonShot) {
                impactLine = Utility.roll2to12();
                playerTmp.getTruck().handleCannonShot(c, impactLine);
                i++;
            }
        } else {
            throw new IllegalArgumentException("Eccezione in combatzone");
        }

    }



        private Player findMinMembers(){
            List<Player> players = Game.getInstance().getPlayers();
            int tmp = players.get(0).getTruck().calculateCrew();
            Player playerTmp = players.get(0);
            for (Player p : players) {
                if (p.getTruck().calculateCrew() < tmp) {
                    playerTmp = p;
                    tmp = p.getTruck().calculateCrew();
                }
            }
            return playerTmp;
        }

        //prima di questo metodo il player deve chiamare activate cannon in modo da aumentare la sua potenza di fuoco
        private Player findMinCannonStrength(){
            List<Player> players = Game.getInstance().getPlayers();
            double tmp = players.get(0).getTruck().calculateCannonStrength();
            Player playerTmp = players.get(0);
            for (Player p : players) {
                if (p.getTruck().calculateCannonStrength() < tmp) {
                    playerTmp = p;
                    tmp = p.getTruck().calculateCannonStrength();
                }
            }
            return playerTmp;
        }

    private Player findMinEngineStrength(){
        List<Player> players = Game.getInstance().getPlayers();
        int tmp = players.get(0).getTruck().calculateEngineStrength();
        Player playerTmp = players.get(0);
        for (Player p : players) {
            if (p.getTruck().calculateEngineStrength() < tmp) {
                playerTmp = p;
                tmp = p.getTruck().calculateEngineStrength();
            }
        }
        return playerTmp;
    }

    //da finire la validità

    /*private void housingUnitsValidity(InputObject inputObject) {
        int count = 0;
        if (inputObject.getHousingUnits().size() == 2) {
            count += inputObject.getHousingUnits().get(i)[0];

        }


    }*/
}
