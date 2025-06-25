package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.model.enumeration.Side;

/**
 * The Cannon class represents a cannon component within the system.
 * It extends the Component class and provides additional functionality
 * specific to cannon components, such as whether a cannon is single or double
 * and its activation state.
 *
 * Instances of this class can represent either single or double cannons,
 * and they provide mechanisms to activate or deactivate the cannon.
 */
public final class Cannon extends Component {

    private final boolean isDouble;
    // necessario per gestire attivazione cannoni doppi
    private boolean isActive = false;

    public Cannon (Side up, Side down, Side left, Side right, boolean isDouble, int id) {
        super(up, down, left, right, id);
        this.isDouble = isDouble;
    }

    /**
     * @return true if the cannon is double, false otherwise.
     */
    public boolean isDouble() {
        return isDouble;
    }

    /**
     * Activates the cannon by setting its active state to true.
     * This method changes the internal state of the cannon to indicate
     * that it is currently active and operational.
     */
    public void activeCannon(){
        isActive = true;
    }

    /**
     * Deactivates the cannon by setting its active state to false.
     * This method changes the internal state of the cannon to indicate
     * that it is no longer active or operational.
     */
    public void disactiveCannon(){
        isActive = false;
    }

    /**
     * @return true if the component is active, false otherwise.
     */
    public boolean isActive(){
        return isActive;
    }

    @Override
    public String toSymbol() {
        return isDouble() ? "G*" : "G";
    }

    @Override
    public String getInfo() {
        return "Cannone " + (this.isDouble() ? "doppio":"singolo") + "\n";
    }


}
