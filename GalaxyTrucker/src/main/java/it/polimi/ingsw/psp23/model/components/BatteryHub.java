package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.model.enumeration.Side;

public final class BatteryHub extends Component {
    // Alberto
    private int numBatteries;

    public BatteryHub(Side up, Side down, Side left, Side right, int numBatteries) {
        super(up, down, left, right);
        this.numBatteries = numBatteries;
    }

    public int getNumBatteries() {
        return numBatteries;
    }

    public void removeBatteries(int numRemoved){

        // numRemoved è il numero di batterie che l'utente vuole rimuovere
        if(numRemoved <= numBatteries && numRemoved >= 0) {
            numBatteries -= numRemoved;
        }
        else{
            throw new IllegalArgumentException("Error: num of batteries removed from this Battery Hub is out of bounds");
        }
    }
}
