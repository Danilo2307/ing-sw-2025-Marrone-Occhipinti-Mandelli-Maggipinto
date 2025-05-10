package it.polimi.ingsw.psp23.model.Game;
import java.util.Random;
import java.util.List;

public final class Utility {
    // creo istanza unica (efficienza e vera casualità) della classe di Java Random che serve per generare numeri casuali
    private static final Random rand = new Random();

    // evito istanziazione
    private Utility() {
    }

    public static int roll2to12() {
        return rand.nextInt(6) + 1 + rand.nextInt(6) + 1;
    }

    public static int randomComponent(int size) {
        return rand.nextInt(0, size);
    }

    public static void updatePosition(List<Player> players, int playerIndex, int positionsToJump) {
        Player giocatore = players.get(playerIndex);
        int playerLocation = giocatore.getPosition();
        int saltiEffettivi = positionsToJump;
        if (positionsToJump > 0) {
            for (int offset = 0; offset < saltiEffettivi; offset++) {
                for (int k = 0; k < players.size(); k++) {
                    if (k != playerIndex && players.get(k).getPosition() == playerLocation + offset) {
                        saltiEffettivi++;
                        offset++;
                        k = 0; // azzerando k ottengo l'effetto di far ripartire il ciclo nel caso in cui ci sia una collisione, in modo da
                        // essere sicuro di confrontare il giocatore corrente con TUTTI GLI ALTRI GIOCATORI PRESENTI

                        /* TODO: bisogna attenzionare che non ci siano loop infiniti dovuto magari ad un accavallamento successivo delle pedine
                                 che potrebbe portare questo ciclo a non esaurirsi mai perchè trova sempre posizioni occupate dopo */
                    }
                }
            }
        } else {
            for (int offset = 0; offset > saltiEffettivi; offset--) {
                for (int k = 0; k < players.size(); k++) {
                    if (k != playerIndex && players.get(k).getPosition() == playerLocation + offset) {
                        saltiEffettivi--;
                        offset--;
                        k = 0; // azzerando k ottengo l'effetto di far ripartire il ciclo nel caso in cui ci sia una collisione, in modo da
                        // essere sicuro di confrontare il giocatore corrente con TUTTI GLI ALTRI GIOCATORI PRESENTI

                        /* TODO: bisogna attenzionare che non ci siano loop infiniti dovuto magari ad un accavallamento successivo delle pedine
                                 che potrebbe portare questo ciclo a non esaurirsi mai perchè trova sempre posizioni occupate dopo */
                    }
                }
            }
        }
        giocatore.setPosition(saltiEffettivi);
    }

}

    // È importante che il positionsToJump sia negativo quando si arretra!!!
    /*public static void updatePosition(List<Player> players,int playerIndex, int positionsToJump){
        Player giocatore = players.get(playerIndex);
        int playerLocation = giocatore.getPosition();
        int newlocation = positionsToJump + playerLocation;
            for (int k = 0; k < players.size(); k++) {
                if (k != playerIndex && players.get(k).getPosition() == newlocation) {
                    if(positionsToJump >= 0){
                        positionsToJump++;
                    }
                    else{
                        positionsToJump--;
                    }
                    newlocation = positionsToJump + playerLocation;
                    k = 0; // azzerando k ottengo l'effetto di far ripartire il ciclo nel caso in cui ci sia una collisione, in modo da
                           // essere sicuro di confrontare il giocatore corrente con TUTTI GLI ALTRI GIOCATORI PRESENTI

                    *//* TODO: bisogna attenzionare che non ci siano loop infiniti dovuto magari ad un accavallamento successivo delle pedine
                             che potrebbe portare questo ciclo a non esaurirsi mai perchè trova sempre posizioni occupate dopo *//*
                }
            }
            giocatore.setPosition(positionsToJump);
    }
    *//* TODO: la check per verificare se un giocatore è stato doppiato e quindi dovrebbe abbandonare il gioco, la lascio ad un'altra funzione
             ma in caso, dovrebbe essere inserita qui, alla fine dell'aggiornamento delle posizioni*//*
}*/


/* qui sono presenti i metodi thereIsCollision ed il metodo alternativo per scorrere le posizioni

private boolean thereIsCollision(Player giocatore, List<Player> players, int i) {
    for(int k=0; k<players.size(); k++){
        if(k!=i && players.get(k).getPosition() == giocatore.getPosition()){
            return true;
        }
    }
    return false;
}
 */

/* backup vecchio metodo updatePosition circa funzionante
public static void updatePosition(List<Player> players,int playerIndex, int positionsToJump){
        //il numero di posizioni totali è 24
        //l'aggiornamento dell'ordine della lista viene supposto a posteriori del play della carta
        //considero la posizione 0 come quella più sulla sinistra della tavola da gioco
        int size = players.size();
        int playerPosition = players.get(playerIndex).getPosition();
        int finalPosition = playerPosition + positionsToJump;
        if(positionsToJump > 0){
            for(int i = size-1; i >= 0; i--)
                if(players.get(i).getPosition() > playerPosition && players.get(i).getPosition() <= finalPosition)
                    finalPosition++; //per ogni player nel percorso, aumento di uno le caselle effettive da percorrere

            players.get(playerIndex).setPosition(finalPosition);

        }else{
            for(int i = 0; i <size; i++) //comincio dall'ultimo elemento della lista che sarebbe il player in ultima posizione
                if(players.get(i).getPosition() < playerPosition && players.get(i).getPosition() >= finalPosition)
                    finalPosition--;

            players.get(playerIndex).setPosition(finalPosition);

        }
    }
 */