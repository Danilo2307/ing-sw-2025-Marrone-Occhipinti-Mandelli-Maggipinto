package it.polimi.ingsw.psp23.model.cards;

import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.Utility;

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
    // eventualmente i giocatori davanti a me e non contando la casella dove c'è l'altro giocatore, per questo mi basta usare updatePosition
    @Override
    public void play(List<Player> players){

        int i = 0; //indice che userò per scorrere la lista di player

        while (i < players.size()){
            Player giocatore = players.get(i);
            int potenzaMotrice = giocatore.getTruck().calculateEngineStrength();

            Utility.updatePosition(players, i , potenzaMotrice);

            i++;
        }
    }
}