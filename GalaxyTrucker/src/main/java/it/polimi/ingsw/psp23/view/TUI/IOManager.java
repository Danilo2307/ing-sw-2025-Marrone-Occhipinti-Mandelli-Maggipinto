package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.Color;

import java.util.Scanner;

/** classe che si occupa della gestione di output (formattazione nave, stampa messaggi)
 *  e gestione degli input "grezzi"
 */
public class IOManager {
    private final Scanner scanner;

    public IOManager() {
        this.scanner = new Scanner(System.in);
    }

    public String read() {
        return scanner.nextLine();
    }

    public void print(String s) {
        System.out.print(s);
    }

    public void error(String message) {
        // printo su stderr (e non stdout) così compaiono in rosso
        System.err.println("Errore: " + message);
    }

    public void printShip(Component[][] ship, int[][] validCoordinates) {

        for (int i = 0; i < ship.length; i++) {
            for (int j = 0; j < ship[i].length; j++) {

                boolean valid = false;
                for (int[] coord : validCoordinates) {
                    if (coord[0] == i && coord[1] == j) {
                        valid = true;
                    }
                }
                if (!valid) {
                    print(" \t");
                }
                else {
                    Component c = ship[i][j];
                    print(getSymbol(c) + "\t");  // lascio spazio tra una tile e l'altra
                }
            }
            System.out.println();
        }
    }

    public String getSymbol(Component c) {
        if (c == null) return "_";

        return c.toSymbol();
    }

    public void printInfoTile(Component c) {
        if (c == null) {
            System.out.println("Tile vuota");
            return;
        }

        print(c.getInfo());
        print("Connettori:\n" +
                "  ↑ Sopra : " + c.getUp() + "\n" +
                "  ↓ Sotto : " + c.getDown() + "\n" +
                "  ← Sinistra : " + c.getLeft() + "\n" +
                "  → Destra : " + c.getRight() + "\n");
    }




}
