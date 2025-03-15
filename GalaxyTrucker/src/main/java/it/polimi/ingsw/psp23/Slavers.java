package it.polimi.ingsw.psp23;

import java.util.List;

public class Slavers extends Card{

    // Federico

    private int cannonStrength;
    private int membersStolen;
    private int prize;
    private int days;

    Slavers(int level, int cannonStrength, int membersStolen, int prize, int days){
        super(level);
        this.cannonStrength = cannonStrength;
        this.membersStolen = membersStolen;
        this.prize = prize;
        this.days = days;
    }

    public void play(List<Player> players){
        int i = 0;
        Boolean defeated = false;
        int option;

        while(!defeated && i < players.size()){
            // do you want to use any batteries?

            if(players.get(i).getTruck().getFirePower() < cannonStrength){
                // qui dobbiamo gestire il fatto che il player perde
            }
            else if(players.get(i).getTruck().getFirePower() > cannonStrength){
                defeated = true;
                // opzione di scelta tra il prendere i crediti cosmici ma perdere
                // i giorni oppure non perdere giorni ma non prendere i crediti cosmici
                option = players.get(i).decide();
                if(option == 0){
                    players.get(i).perdigiorni
                }
                else if(option == 1){

                }
            }

            i++;
        }
    }

}