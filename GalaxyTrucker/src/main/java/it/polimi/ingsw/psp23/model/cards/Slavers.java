package it.polimi.ingsw.psp23.model.cards;


import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.Events.Event;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;

public class Slavers extends Card {
    //Federico
    // final perchè non vengono più modificati dopo l'inizializzazione
    private final int cannonStrength;
    private final int membersStolen;
    private final int prize;
    private final int days;
    private boolean defeated;

    public Slavers(int level, int cannonStrength, int membersStolen, int prize, int days){
        super(level);
        this.cannonStrength = cannonStrength;
        this.membersStolen = membersStolen;
        this.prize = prize;
        this.days = days;
        this.defeated = false;
    }

    public int getCannonStrength() {
        return cannonStrength;
    }

    public int getMembersStolen() {
        return membersStolen;
    }

    public int getPrize() {
        return prize;
    }

    public int getDays() {
        return days;
    }

    @Override
    public Object call(Visitor visitor){
        return visitor.visitForSlavers(this);
    }

    public void initPlay(){
        Game.getInstance().setGameStatus(GameStatus.RunningSlavers);
        Game.getInstance().fireEvent(new Event(Game.getInstance().getGameStatus()));
    }

    public void play(InputObject inputObject){
        int i = 0;
        Player player = Game.getInstance().getCurrentPlayer();
        if(!defeated){
            //suppongo che il player giunto questo punto abbia già chiamato i metodi per attivare in cannoni che voleva attivare e rimosso le batterie da dover rimuovere
            double playerCannonStrength = player.getTruck().calculateCannonStrength();

            if(playerCannonStrength < cannonStrength){
                // player perde
                // 1 e 1 sono la posizione dell'HousingUnit da cui togliere i membri
                player.getTruck().reduceCrew(membersStolen,1,1); //dovrò gestire se perde tutti i membri lascia il gioco
                //scegli quali membri rimuovere da quali cabine
                //search_component in board? il nostro però ha String e non Type..
            }
            else if(playerCannonStrength > cannonStrength){
                defeated = true;
                //tramite la View, player sceglie se guadagnare crediti cosmici oppure non perdere giorni di volo

                if(option == 0){    //sceglie money. se sceglie l'altra opzione non succede nulla
                    players.get(i).updateMoney(prize) ;
                    Utility.updatePosition(players,i,-days);
                }
            }
            //in caso di pareggio non succedde nulla e passo al giocatore successivo
            i++;
        }
    }
    }
}