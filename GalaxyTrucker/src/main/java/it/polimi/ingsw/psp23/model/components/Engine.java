package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.model.enumeration.ComponentType;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public class Engine extends Component{

    private final boolean isDouble;

    public Engine (Side up, Side down, Side left, Side right, boolean isDouble) {
        super(ComponentType.ENGINE, up, down, left, right);
        this.isDouble = isDouble;
    }

    public boolean isDouble() {
        return isDouble;
    }







}
