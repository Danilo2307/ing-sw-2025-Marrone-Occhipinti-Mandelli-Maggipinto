package it.polimi.ingsw.psp23.model.cards;
import it.polimi.ingsw.psp23.model.enumeration.Challenge;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;
import java.util.List;
import java.util.ArrayList;

public class CombatZone extends Card {
    // Danilo

    Challenge penalty1;
    Challenge penalty2;
    Challenge penalty3;
    private final int daysLost;
    private final int goodsLost;
    private final int membersLost;
    private final ArrayList<CannonShot> cannonShot;

    //la maggior parte dei for in questo codice servono a trovare un membro che abbia uno dei parametri minimo
    //quindi si potrebbe anche pensare di definire dei metodi per farlo

    public CombatZone(int level,int daysLost, int goodsLost, int membersLost,Challenge penalty1,Challenge penalty2, Challenge penalty3, ArrayList<CannonShot> cannonshot) {
        super(level);
        this.daysLost = daysLost;
        this.goodsLost = goodsLost;
        this.membersLost = membersLost;
        this.penalty1 = penalty1;
        this.penalty2 = penalty2;
        this.penalty3 = penalty3;
        this.cannonShot = cannonshot;
    }

    public Challenge getPenalty(int i) {
        if(i==1)
            return penalty1;
        else if(i==2)
            return penalty2;
        else
            return penalty3;
    }

    private int findMinMembers(List<Player> players){
        int size = players.size();
        int pos = -1;
        int tmp = 0;
        for(int i=0;i<size;i++){
            if(i==0) {
                tmp = players.get(i).getTruck().getCrew();
                pos = i;
            }else if(tmp > players.get(i).getTruck().getCrew()) {
                tmp = players.get(i).getTruck().getCrew();
                pos = i;
            }
        }

        return pos;
    }

    private int findMinCannonStrength(List<Player> players){
        int size = players.size();
        int pos = -1;
        double tmp = -1;
        for(int i=0;i<size;i++){
            if(i==0) {
                tmp = players.get(i).getTruck().getCannonStrength();
                pos = i;
            }else if(tmp > players.get(i).getTruck().getCannonStrength()){
                tmp = players.get(i).getTruck().getCannonStrength(); //get cannon strength si occuperà dell'input sulle batterie
                pos = i;
            }
        }
        return pos;
    }

    private int findMinEngineStrength(List<Player> players){
        int size = players.size();
        int pos = -1;
        int tmp = -1;
        for(int i=0;i<size;i++){
            if(i==0) {
                tmp = players.get(i).getTruck().getEngineStrength();
                pos = i;
            }else if(tmp > players.get(i).getTruck().getEngineStrength()){
                tmp = players.get(i).getTruck().getEngineStrength(); //get engine strength si occuperà dell'input sulle batterie
                pos = i;
            }
        }
        return pos;
    }

@Override
    public void play(List<Player> players){
        int impactLine;
        int pos;
        int size = players.size();
        int tmp = -1;
//inizio prima sfida
        if(penalty1 == Challenge.Members){
            pos = findMinMembers(players);

            System.out.println(players.get(pos).getNickname() +" has less crew members!");
            Utility.updatePosition(players, pos, -daysLost);
        }else if(penalty1 == Challenge.Cannon_strength){
            pos = findMinCannonStrength(players);
            System.out.println(players.get(pos).getNickname()+" has less cannon strength!");
            Utility.updatePosition(players,pos,-daysLost);


        }else{
            throw new IllegalArgumentException("Eccezione in combatzone ");
        }

//inizio seconda sfida
            pos = findMinEngineStrength(players);

        System.out.println(players.get(pos).getNickname() + " has less engine strength!");

        if(penalty2 == Challenge.Members) {
            players.get(pos).getTruck().reduceCrew(membersLost);
        } else if(penalty2 == Challenge.Goods){
            players.get(pos).getTruck().pickMostImportantGoods(goodsLost);
        }else{
            throw new IllegalArgumentException("Eccezione in combatzone ");
        }

//inizio terza sfida
        if(penalty3 == Challenge.Members){
            pos = findMinMembers(players);

            System.out.println(players.get(pos).getNickname() + " has less crew members!");
            for(CannonShot c: cannonShot){
                impactLine = Utility.roll2to12();
                players.get(pos).getTruck().handleCannonShot(c,impactLine);
            }



        }else if(penalty3 == Challenge.Cannon_strength){
            pos = findMinCannonStrength(players);
            System.out.println(players.get(pos).getNickname() + " has less cannon strength!");
            for(CannonShot c: cannonShot){
                impactLine = Utility.roll2to12();
                players.get(pos).getTruck().handleCannonShot(c,impactLine);
            }
        }else{
            throw new IllegalArgumentException("Eccezione in combatzone");
        }


    }



}
