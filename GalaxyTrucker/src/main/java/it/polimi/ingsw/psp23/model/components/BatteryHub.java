package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.model.enumeration.Side;

public class BatteryHub extends Component {
    // Alberto

    private int numBatteries;

    BatteryHub(Side up, Side down, Side left, Side right, int numBatteries) {
        super(up, down, left, right);
        this.numBatteries = numBatteries;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    public void setNumBatteries(int n){
        if(n >= 0) numBatteries = n;
        else System.out.println("Error: numBatteries must be greater than 0");
    }

    public void removeBatteries(int numRemoved){ // numRemoved indica il numero di batterie da rimuovere
        // metto questo if-else per controllare che stia rimuovendo un numero valido di batterie e che non stia
        // andando oltre il numero di batterie disponibili. Valutiamo se inserire una eccezione.
        if(numRemoved <= numBatteries && numRemoved >= 0) {
            numBatteries = numBatteries - numRemoved;
        }
        else{
            throw new IllegalArgumentException("Error: num of batteries removed from Battery Hub is out of bounds");
        }
    }

}
