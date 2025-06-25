package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.model.enumeration.Side;


/**
 * Represents an engine component for a system, extending the generic Component.
 *
 * The engine has the capability to be activated or deactivated and may be either
 * single or double.
 */
public final class Engine extends Component{

    private final boolean isDouble;
    // necessario per gestire attivazione motori doppi
    private boolean isActive;

    public Engine (Side up, Side down, Side left, Side right, boolean isDouble, int id) {
        super(up, down, left, right, id);
        this.isDouble = isDouble;
    }

    /**
     * Determines whether the engine is double or not.
     *
     * @return true if the engine is double, false otherwise.
     */
    public boolean isDouble() {
        return isDouble;
    }

    /**
     * Activates the engine by setting its active state to true.
     * This method updates the internal state of the engine to indicate
     * that it is currently active and operational.
     */
    public void activeEngine(){
        isActive = true;
    }

    /**
     * Deactivates the engine by setting its active state to false.
     * This method changes the internal state of the engine to indicate
     * that it is no longer active or operational.
     */
    public void disactiveEngine(){
        isActive = false;
    }

    /**
     * Checks whether the component is currently active.
     *
     * @return true if the component is active, false otherwise.
     */
    public boolean isActive(){
        return isActive;
    }

    @Override
    public String toSymbol() {
        return isDouble() ? "E*" : "E";
    }

    @Override
    public String getInfo() {
        return "Motore " + (this.isDouble() ? "doppio":"singolo") + "\n";
    }






}
