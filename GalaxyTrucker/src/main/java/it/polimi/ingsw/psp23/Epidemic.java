package it.polimi.ingsw.psp23;

//import java.awt.*; -> questa libreria l'ha aggiunta automaticamente intellij perchè prende la parola "component" e la interpreta come elemento grafico
//                      visto che non l'abbiamo definito, quindi stiamo attenti che non importi librerie strane
import java.util.List;

public class Epidemic extends Card {
    // Alberto

    Epidemic(int level) {
        super(level);
    }

    // la logica del play è: per ogni player scorro tutta la sua nave, mano a mano che incontro un hub occupato e con il flag visited a false controllo che
    // ci siano hub adiacenti occupati(non mi importa il valore del loro flag perchè potrei averli controllati all'iterazione precedente, a me interessa solo che siano
    // occupati), in questo caso cambio il flag dell'hub corrente a true ed eventualmente cambio anche quello dell'hub adiacente a true(potrei anche non fare questa
    // operazione ed il metodo resterebbe comunque valido in quanto andrei a controllare dopo). Non faccio la sottrazione di
    // un membro dell'equipaggio in questa fase perchè rischierei che un hub interconnesso vada a zero prima del previsto e risulti non occupato. Per questo poi alla
    // fine vado a fare un ulteriore controllo su tutta la nave e diminuisco di uno il numero di membri di ogni hub con il flag a true e mano a mano imposto
    // nuovamente quel flag a false(posso fare così perchè facendo una scansione sequenziale della matrice non rischio di ripassare per lo stesso hub due volte).
    // In questo scrorrimento finale decrementerò anche il numero di componenti totale(crew) di ogni nave di uno per ogni hub visited che incontro.
    public void play(List<Player> players){
        for(Player p : players){

            Component[][] nave = p.getTruck().getShip(); // ho creato questa variabile in modo da migliorare la leggibilità del codice

            for(int i = 0; i < nave.length; i++){
                for(int j = 0; j < nave[i].length; j++){
                    if(nave[i][j].getClass() == Hub.class && nave[i][j].getNumMembers() > 0 && nave[i][j].getIsVisited() == false){

                        // al posto di questi 4 if si potrebbe iterare su un array contenente:"{1,0}, {-1,0}, {0,1}, {0,-1}" però
                        // comincia a diventare più complesso ed un po' meno esplicito, da valutare il cambiamento

                        if((i+1) < nave.length && nave[i+1][j].getClass() == Hub.class && nave[i+1][j].getNumMembers() > 0){
                            if(!nave[i][j].getIsVisited()) nave[i][j].setIsVisited(true); // "!p.Truck.Ship[i][j].visited" vuol dire che il flag visited è false
                            if(nave[i+1][j].getIsVisited() == false){
                                nave[i+1][j].setIsVisited(true);
                            }
                        }
                        if((i-1) >= 0 && nave[i-1][j].getClass() == Hub.class && nave[i-1][j].getNumMembers() > 0){
                            if(!nave[i][j].getIsVisited()) nave[i][j].setIsVisited(true);
                            if(nave[i-1][j].getIsVisited() == false){
                                nave[i-1][j].setIsVisited(true);
                            }
                        }
                        if((j + 1) < nave[i].length && nave[i][j+1].getClass() == Hub.class && nave[i][j+1].getNumMembers() > 0){
                            if(!nave[i][j].getIsVisited()) nave[i][j].setIsVisited(true);
                            if(nave[i][j+1].getIsVisited() == false){
                                nave[i][j+1].setIsVisited(true);
                            }
                        }
                        if((j-1) >= 0 && nave[i][j-1].getClass() == Hub.class && nave[i][j-1].getNumMembers() > 0){
                            if(!nave[i][j].getIsVisited()) nave[i][j].setIsVisited(true);
                            if(nave[i][j-1].getIsVisited() == false){
                                nave[i][j-1].setIsVisited(true);
                            }
                        }

                    }
                }
            }

            // qui procedo a decrementare il numero di componenti
            for(Component[] row : nave) {
                for(Component c : row){

                    if (c.getClass() == Hub.class && c.getIsVisited() == true) {
                        c.setNumMembers(c.getNumMembers()-1);
                        p.Truck.reduceCrew(1);
                        c.setIsVisited(false);
                    }

                }
            }

        }
    }
}
