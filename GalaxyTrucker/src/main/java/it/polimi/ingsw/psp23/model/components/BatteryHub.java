package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public final class BatteryHub extends Component {
    // Alberto
    private final int capacity;
    private int numBatteries;

    public BatteryHub(Side up, Side down, Side left, Side right, int numBatteries, int id) {
        super(up, down, left, right, id);
        capacity = numBatteries;
        this.numBatteries = capacity;
    }

    public int getCapacity() {
        return capacity;
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

    @Override
    public String toSymbol() {
        return this.getCapacity() == 2 ? "B2" : "B3";
    }

    @Override
    public String getInfo() {
        return "Il pacco batterie ha capacità " + this.getCapacity() +
                " e attualmente contiene " + this.getNumBatteries() + " batterie\n";
    }
}
