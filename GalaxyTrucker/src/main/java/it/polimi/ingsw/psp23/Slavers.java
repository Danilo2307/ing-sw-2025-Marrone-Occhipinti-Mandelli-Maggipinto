package it.polimi.ingsw.psp23;
import java.util.List;

public class Slavers extends Card{
    // final perchè non vengono più modificati dopo l'inizializzazione
    private final int cannonStrength;
    private final int membersStolen;
    private final int prize;
    private final int days;

    Slavers(int level, int cannonStrength, int membersStolen, int prize, int days){
        super(level);
        this.cannonStrength = cannonStrength;
        this.membersStolen = membersStolen;
        this.prize = prize;
        this.days = days;
    }

    public void play(List<Player> players){
        int i = 0;
        boolean defeated = false;

        while(!defeated && i < players.size()){
            //in Java è standard dichiarare le variabili il più vicino possibile al loro utilizzo
            int playerCannonStrength = players.get(i).getTruck().getCannonStrength();
            // do you want to use any batteries?
            // quante batterie vuoi usare? quali cannoni vuoi attivare con queste batterie?
            // ricalcolo CannonStrength in base a questi input lo faccio con metodo dedicato in player (?)

            if(playerCannonStrength < cannonStrength){
                // player perde
                players.get(i).getTruck().reduceCrew(membersStolen);
                //scegli quali membri rimuovere da quali cabine
                //search_component in board? il nostro però ha String e non Type..

            /*    if (players.get(i).getTruck().crew) <= 0 {
                    players.get(i).getTruck().crew = 0;
                    players.get(i).leaveGame();

                }*/ //LO GESTISCO QUI O IN REDUCE_CREW? meglio in reduce_crew
            }
            else if(playerCannonStrength > cannonStrength){
                defeated = true;
                //tramite la View, player sceglie se guadagnare crediti cosmici oppure non perdere giorni di volo

                if(option == 0){    //sceglie money. se sceglie l'altra opzione non succede nulla
                    players.get(i).updateMoney(prize) ;
                    players.get(i).updatePosition(-days);
                }
            }
            //in caso di pareggio non succedde nulla e passo al giocatore successivo
            i++;
        }
    }
}