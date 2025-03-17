package it.polimi.ingsw.psp23;

import java.util.List;
import java.util.ArrayList;

public class OpenSpace extends Card{
    // Alberto

    OpenSpace(int level) {
        super(level);
    }

    private boolean thereIsCollision(Player giocatore, List<Player> players, int i) {
        for(int k=0; k<players.size(); k++){
            if(k!=i && players.get(k).getPosition() == giocatore.getPosition()){
                return true;
            }
        }
        return false;
    }

    // il ragionamento di play è: open space ci permette di avanzare di un numero di posizioni pari al valore della potenza motrice del player quindi,
    // sapendo che quando usiamo il getter della potenza motrice del player ci arriva già il valore aggiornato della potenza motrice, in cui è considerato
    // l'eventuale uso di batterie per i motori doppi, basta che open space faccia avanzare le posizioni delle pedine.
    // Per l'avanzamento devo assicurarmi di avanzare effettivamente di un numero di POSIZIONI VUOTE pari al numero della potenza motrice, sorpassando
    // eventualmente i giocatori davanti a me e non contando la casella dove c'è l'altro giocatore, per questo mi basta fare check sulla posizione del giocatore e,
    // mano a mano che avanzo, controllo che la mia posizione non sia uguale a quella di qualsiasi altro player, altrimenti avanzo di un altro player
    @Override
    public void play(List<Player> players){

        int i = 0; //indice che userò per scorrere la lista di player

        while (i < players.size()){
            Player giocatore = players.get(i);
            int potenzaMotrice = giocatore.getTruck().getEngineStrength();

            // questo for mi serve per incrementare di uno spazio alla volta la pedina del mio giocatore. Il for annidato serve
            // a controllare che in quel posto non ci sia nessun altro player, altrimenti fai scorrere di un'altra posizione in avanti la posizione
            // del giocatore perchè è come se non si fosse mosso di uno spazio vuoto e riporta k a zero perchè adesso devi ricontrollare di nuovo
            // le posizioni di tutti i giocatori, non essendo la lista di player ordinata
            for(int j = 0 ; j < potenzaMotrice ; j++){
                giocatore.updatePosition(1);

                //for(int k = 0 ; k < players.size() ; k++){
                //    if(k != i && players.get(k).getPosition() == giocatore.getPosition()){
                //        giocatore.updatePosition(1);
                //        k=0;
                //    }
                //} -> inizalmente avevo usato questo for, ma ha più senso fare:
                while(thereIsCollision(giocatore, players, i)) {
                    giocatore.updatePosition(1);
                }
            }
            i++;
        }
    }

}
