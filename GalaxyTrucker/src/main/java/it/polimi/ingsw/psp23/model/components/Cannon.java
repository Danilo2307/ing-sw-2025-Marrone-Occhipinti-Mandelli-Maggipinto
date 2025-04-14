package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.model.enumeration.Side;

public final class Cannon extends Component {

    private final boolean isDouble;
    // necessario per gestire attivazione cannoni doppi
    private boolean isActive = false;

    public Cannon (Side up, Side down, Side left, Side right, boolean isDouble) {
        super(up, down, left, right);
        this.isDouble = isDouble;
    }

    public boolean isDouble() {
        return isDouble;
    }

    public void activeCannon(){
        isActive = true;
    }

    public void disactiveCannon(){
        isActive = false;
    }

    public boolean isActive(){
        return isActive;
    }
}
