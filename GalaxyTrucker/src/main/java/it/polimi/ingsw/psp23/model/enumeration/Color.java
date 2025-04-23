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
        return switch (this) {
            case Brown -> "marrone";
            case Purple -> "viola";
            case Green -> "verde";
            case Yellow -> "gialla";
            case Red -> "rossa";
            case Blue -> "blu";
        };
    }
}