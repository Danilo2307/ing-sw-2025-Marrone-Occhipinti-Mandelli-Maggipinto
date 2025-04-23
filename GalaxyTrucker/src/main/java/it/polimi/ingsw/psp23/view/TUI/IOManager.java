package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.Color;

public class IOManager {

    /** classe che si occupa della gestione di output (formattazione nave, stampa messaggi)
     *  e gestione degli input "grezzi"
     */

    public void printShip(Component[][] ship) {
        for (int i = 0; i < ship.length; i++) {
            for (int j = 0; j < ship[i].length; j++) {
                Component c = ship[i][j];
                System.out.print(getSymbol(c) + " ");  // lascio spazio tra una tile e l'altra
            }
            System.out.println();
        }
    }

    private String getSymbol(Component c) {
        if (c == null) return "_";

        return switch (c) {
            case Cannon cannon -> cannon.isDouble() ? "G*" : "G";
            case Engine engine -> engine.isDouble() ? "E*" : "E";
            case HousingUnit cabin -> "H";
            case AlienAddOns a -> a.getColor() == Color.Purple ? "Ap" : "Ab";
            case StructuralComponent t -> "T";
            case Container container -> container.getColor() == Color.Red ? "Cr" : "Cb";
            case BatteryHub b -> b.getCapacity() == 2 ? "B2" : "B3";
            case Shield s -> "S";
            default -> "";   // non dovrebbe mai capitare
        };
    }


}
