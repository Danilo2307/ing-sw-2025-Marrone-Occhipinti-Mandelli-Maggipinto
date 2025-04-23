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

    public void printInfoTile(Component c) {
        if (c == null) {
            System.out.println("Tile vuota");
            return;
        }

        switch (c) {
            case Cannon cannon -> System.out.println("Cannone " + (cannon.isDouble() ? "doppio":"singolo"));
            case Engine engine -> System.out.println("Motore " + (engine.isDouble() ? "doppio":"singolo"));
            case HousingUnit cabin -> {
                if (cabin.getAlien() == null)
                    System.out.println("Cabina contiene " + cabin.getNumAstronaut() + " astronauti");
                else
                    System.out.println("Cabina contiene un alieno di colore " + cabin.getAlien());
            }
            case AlienAddOns a -> System.out.println("Supporto vitale di colore "+ a.getColor());
            case StructuralComponent t -> System.out.println("Tubi");
            case Container container -> System.out.println("Container di colore "+ container.getColor() + "e capacità " + container.getSize()
                    + ". Contiene le seguenti merci: " + container.getItems());
            case BatteryHub b -> System.out.println("Il pacco batterie ha capacità " + b.getCapacity() +
                    "e attualmente contiene " + b.getNumBatteries() + "batterie");
            case Shield s -> System.out.println("Scudo");
            default -> System.out.println();
        }

        System.out.println("Connettori:");
        System.out.println("  ↑ Up     : " + c.getUp());
        System.out.println("  ↓ Down   : " + c.getDown());
        System.out.println("  ← Left   : " + c.getLeft());
        System.out.println("  → Right  : " + c.getRight());
    }




}
