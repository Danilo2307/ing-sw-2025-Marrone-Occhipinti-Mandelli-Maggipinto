package it.polimi.ingsw.psp23.model.enumeration;

public enum Color {
    Brown,
    Purple,
    Green,
    Yellow,
    Red,
    Blue;

    @Override
    public String toString() {
        if(Color.Brown.equals(this)) {
            return "Brown";
        }
        else if(Color.Purple.equals(this)) {
            return "Purple";
        }
        else if(Color.Green.equals(this)) {
            return "Green";
        }
        else if(Color.Yellow.equals(this)) {
            return "Yellow";
        }
        else if(Color.Red.equals(this)) {
            return "Red";
        }
        else{
            return "Blue";
        }
    }
}