package it.polimi.ingsw.psp23;

public class CombatZone {
    // Danilo

    //legenda dei char: E: engine strength
    //                  C: cannon strength
    //                  M: members number
    //                  D: days lost
    //                  G: Goods
    //                  R: right
    //                  L: left
    //                  U: Up
    //                  B: bottom
    // è sottinteso che le regole della carta impongano le sfide a chi ha il minor valore di questi attributi
    String penalty1;
    String penalty2;
    String penalty3;
    int daysLost;
    int goodsLost;
    int membersLost;

    public CombatZone(int daysLost, int goodsLost, int membersLost,String penalty1,String penalty2, String penalty3) {
        this.daysLost = daysLost;
        this.goodsLost = goodsLost;
        this.membersLost = membersLost;
        this.penalty1 = penalty1;
        this.penalty2 = penalty2;
        this.penalty3 = penalty3;
    }

    public String getPenalty1(int i) {
        if(i==1)
            return penalty1;
        else if(i==2)
            return penalty2;
        else if(i==3)
            return penalty3;
        else
            return "error";
    }

    public void play(List<Player> players){
        int i, pos;
        int size = players.size();
        int tmp;
        if(penalty1.equals("Members")){
            for(i=0;i<size;i++){
                if(i==0) {
                    tmp = players.get(i).getTruck().crew;
                    pos = i;
                }else if(tmp > players.get(i).getTruck().crew) {
                    tmp = players.get(i).getTruck().crew;
                    pos = i;
                }
            }
            System.out.println(players.get(i).nickname+" has less crew members!");
            players.get(i).updatePosition(-daysLost);
        }else if(penalty2.equals("Cannon_strength")){
            for(i=0;i<size;i++){
                if(i==0) {
                    tmp = players.get(i).getTruck().getCannonStrength();
                    pos = i;
                }else if(tmp > players.get(i).getTruck().getCannonStrength()){
                    tmp = players.get(i).getTruck().getCannonStrength();
                    pos = i;
                }
            }
        }
    }



    }
