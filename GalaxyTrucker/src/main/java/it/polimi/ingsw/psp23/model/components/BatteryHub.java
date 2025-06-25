package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.model.enumeration.Side;

/**
 * The BatteryHub class represents a component that serves as a hub for storing
 * and managing a given number of batteries. It extends the Component class.
 * A BatteryHub has a fixed capacity and a dynamic number of available batteries.
 *
 * This class provides methods for retrieving the hub's capacity, checking the
 * number of batteries currently stored, and removing a specified number of batteries.
 */
public final class BatteryHub extends Component {
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

    /**
     * Removes a specified number of batteries from the BatteryHub.
     *
     * The method checks if the number of batteries to be removed is within valid
     * bounds (greater than or equal to 0 and less than or equal to the current number
     * of batteries). If the input is invalid, an IllegalArgumentException is thrown.
     *
     * @param numRemoved the number of batteries to remove from the BatteryHub.
     *                    Must be a non-negative value and less than or equal to
     *                    the current number of batteries in the hub.
     * @throws IllegalArgumentException if the number of batteries to remove is
     *                                  out of valid bounds.
     */
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
