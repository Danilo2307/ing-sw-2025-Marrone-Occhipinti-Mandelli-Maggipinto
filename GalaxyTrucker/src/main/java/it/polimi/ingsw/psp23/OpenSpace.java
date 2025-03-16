package it.polimi.ingsw.psp23;

import java.util.List;
import java.util.ArrayList;

public class OpenSpace extends Card{
    // Alberto

    OpenSpace(int level) {
        super(level);
    }

    // il ragionamento di play è: open space ci permette di avanzare di un numero di posizioni pari al valore della potenza motrice del player quindi,
    // sapendo che quando usiamo il getter della potenza motrice del player ci arriva già il valore aggiornato della potenza motrice, in cui è considerato
    // l'eventuale uso di batterie per i motori doppi, basta che open space faccia avanzare le posizioni delle pedine.
    // Per l'avanzamento devo assicurarmi di avanzare effettivamente di un numero di POSIZIONI VUOTE pari al numero della potenza motrice, sorpassando
    // eventualmente i giocatori davanti a me e non contando la casella dove c'è l'altro giocatore, per questo mi basta fare check sulla posizione del giocatore e,
    // nel caso in cui la sua posizione diventi maggiore di quella di un altro player aggiungo un altro passo in avanti
    @Override
    public void play(List<Player> players){

        int i = 0; //indice che userò per scorrere la lista di player

        while (i < players.size()){
            Player giocatore = players.get(i);
            int newposition = giocatore.getPosition() + giocatore.getTruck().getEngineStrength();
            List<Player> newList = new ArrayList<Player>();

            // il ragionamento di questo for è, scorro la lista di player in maniera decrescente, in modo da controllare mano a mano il player più in avanti di
            // una posizione. Se la nuova posizione del player che stiamo analizzando è >= a quella del player davanti a lui dovrà avanzare di una posizione in
            // più perchè una delle pedine che ha attraversato era quella dell'altro player e non conta come spazio vuoto.
            for(int j = i-1 ; j >= 0 ; j--){
                if(newposition >= players.get(i).getPosition()){
                    newposition = newposition + 1;
                }
            }
            giocatore.updatePosition(newposition - giocatore.getPosition());
            // serve il metodo che aggiorni la lista, tuttavia se la lista si aggiorna ad ogni carta questo metodo play non funziona perchè
            // quando scorre la lista non vede correttamente quali sono i player prima di lui, se la lista si aggiorna al primo cambiamento
            // di posizione continua a non funzionare perchè, iterando sulla lista di player, se io aggiorno questa lista potrei saltare
            // l'avanzamento di qualche player o far avanzare un player due volte perchè ad esempio inizialmente un player era primo e lo
            // faccio avanzare, poi per via degli avanzamenti degli altri player va terzo e, arrivato ad analizzare il terzo lo farei riavanzare
            i++;
        }
    }

}
