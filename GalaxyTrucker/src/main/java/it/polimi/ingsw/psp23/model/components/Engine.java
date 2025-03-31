package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.model.enumeration.ComponentType;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public class Engine extends Component{

    private final boolean isDouble;
    private boolean isActive;

    public Engine (Side up, Side down, Side left, Side right, boolean isDouble) {
        super(ComponentType.ENGINE, up, down, left, right);
        this.isDouble = isDouble;
    }

    public boolean isDouble() {
        return isDouble;
    }

    public void activeEngine(){
        isActive = true;
    }

    public void disactiveEngine(){
        isActive = false;
    }

    public boolean checkIfIsActive(){
        return isActive;
    }






}
