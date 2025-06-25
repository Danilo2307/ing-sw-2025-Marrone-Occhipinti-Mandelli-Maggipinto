package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.model.enumeration.Side;

/**
 * Represents a Shield, a specific type of component for a system.
 * A Shield may be activated or deactivated to indicate its state.
 * Shields are denoted with a specific symbol and provide information
 * about their type when queried.
 *
 * This class extends {@code Component} and inherits its functionality,
 * while adding behavior specific to shields.
 */
public final class Shield extends Component{


    // necessario per gestire attivazione cannoni doppi
    private boolean isActive = false;

    public Shield(Side up, Side down, Side left, Side right, int id) {
        super(up, down, left, right, id);
    }

    /**
     * Activates the shield by setting its active state to true.
     * This method changes the internal state of the shield to indicate
     * that it is currently active and operational. An active shield
     * may represent a functional and defensive component within the system.
     */
    public void activeShield(){
        isActive = true;
    }

    /**
     * Deactivates the shield by setting its active state to false.
     * This method modifies the internal state of the shield to indicate
     * that it is no longer active or in use.
     */
    public void disactiveShield(){
        isActive = false;
    }

    public boolean isActive(){
        return isActive;
    }

    @Override
    public String toSymbol() {
        return "S";
    }

    @Override
    public String getInfo() {
        return "Scudo\n";
    }

}
