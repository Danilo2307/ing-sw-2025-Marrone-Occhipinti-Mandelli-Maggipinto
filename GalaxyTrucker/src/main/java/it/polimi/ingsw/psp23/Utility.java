package it.polimi.ingsw.psp23;
import java.util.Random;
import java.util.List;

public final class Utility {
    // creo istanza unica (efficienza e vera casualità) della classe di Java Random che serve per generare numeri casuali
    private static final Random rand = new Random();

    // evito istanziazione
    private Utility() {}

    public static int roll2to12() {
        return rand.nextInt(6) + 1 + rand.nextInt(6) + 1;
    }

    public static void updatePosition(List<Player> players,int pos, int positions){
        //il numero di posizioni totali è 18
        //l'aggiornamento dell'ordine della lista viene supposto a posteriori del play della carta
        //considero la posizione 0 come quella più sulla sinistra della tavola da gioco
        int size = players.size();
        int playerPosition = players.get(pos).position;
        int finalPosition = playerPosition + positions;
        if(positions > 0){
            for(int i = 0; i < size; i++)
                if(players.get(i).position > playerPosition && players.get(i).position <= finalPosition)
                    finalPosition++; //per ogni player nel percorso, aumento di uno le caselle effettive da percorrere

            players.get(pos).updatePosition(finalPosition);

        }else if(positions < 0){
            for(int i = size-1; i >= 0; i--) //comincio dall'ultimo elemento della lista che sarebbe il player in ultima posizione
                if(players.get(i).position < playerPosition && players.get(i).position >= finalPosition)
                    finalPosition--; 
            
            players.get(pos).updatePosition(finalPosition);
            
        }
    }

    //la check per verificare se un giocatore è stato doppiato e quindi dovrebbe abbandonare il gioco, la lascio ad un'altra funzione
    // ma in caso, dovrebbe essere inserita qui, alla fine dell'aggiornamento delle posizioni
}