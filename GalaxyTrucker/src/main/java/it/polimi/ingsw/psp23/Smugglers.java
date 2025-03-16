package it.polimi.ingsw.psp23;

import java.util.List;

public class Smugglers extends Card {
    // Alberto

    private final int firePower;
    private final int numItemsStolen;
    private final int days;
    private final Item[] prize;
    private boolean defeated = false; // questa variabile mi serve per capire se sono stati sconfitti ed in caso non continuare con i giocatori successivi


    Smugglers(int level, int firePower, int numItemsStolen, int days, Item[] prize ) {
        super(level);
        this.firePower = firePower;
        this.numItemsStolen = numItemsStolen;
        this.days = days;
        this.prize = prize;
    }

    // Il ragionamento dietro play è: uso un while che continua a scorrere fintanto che il flag defeated degli Smugglers è settato a false
    @Override
    public void play(List<Player> players){

        int i = 0; //userò questo indice per scorrere la lista di players

        while(!defeated && i < players.size()){

            Player giocatore = players.get(i);

            if(giocatore.getTruck().getCannonStrength() > firePower){ // ovviamente la cannonStrength sarà prima aggiornata dal giocatore che,
                                                                      // una volta arrivatagli la carta, decide se usare le batterie o meno(bisogna
                                                                      // gestire correttamente tutti i casi limite come ad esempio valori non validi)
                defeated = true;
                giocatore.decide();
            }
            else if(giocatore.getTruck().getCannonStrength() < firePower){
                giocatore.getTruck.pickMostImportantGoods(2); // questo metodo presente in board toglierà due delle merci più importanti dalla board del player
            }
            i++;
        }
    }

}
