package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.model.enumeration.Side;

public final class Engine extends Component{

    private final boolean isDouble;
    // necessario per gestire attivazione motori doppi
    private boolean isActive;

    public Engine (Side up, Side down, Side left, Side right, boolean isDouble, int id) {
        super(up, down, left, right, id);
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
