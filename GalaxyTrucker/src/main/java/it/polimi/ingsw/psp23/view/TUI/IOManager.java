package it.polimi.ingsw.psp23.view.TUI;

import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.Color;

import java.util.Scanner;

/**
 * The IOManager class provides a utility for handling input and output operations in the system.
 * It includes methods for reading user input, printing messages, handling error logs,
 * and displaying components of a ship structure.
 */
public class IOManager {
    private final Scanner scanner;

    public IOManager() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a line of user input from the console using the associated Scanner.
     *
     * @return the line of input entered by the user as a String
     */
    public String read() {
        return scanner.nextLine();
    }

    public void print(String s) {
        System.out.print(s);
    }

    /**
     * Prints an error message to the standard error stream (stderr).
     *
     * @param message the error message to be displayed
     */
    public void error(String message) {
        // printo su stderr (e non stdout) così compaiono in rosso
        System.err.println("Errore: " + message);
    }

    /**
     * Prints a graphical representation of the ship to the console.
     * Depending on the level specified, it adjusts the valid coordinates considered
     * when displaying the ship structure.
     *
     * @param ship a 2D array of {@code Component} objects representing the ship's structure
     * @param level an integer that specifies the configuration level for valid coordinates
     */
    public void printShip(Component[][] ship, int level) {
        int[][] validCoords;
        if (level == 2) {
            validCoords = new int[][]{
                    {0, 2}, {0, 4}, {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4},
                    {2, 5}, {2, 6}, {3, 0}, {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, {4, 0}, {4, 1}, {4, 2}, {4, 4}, {4, 5}, {4, 6}
            };
        }
        else {
            validCoords = new int[][]{
                    {0, 3}, {1, 2}, {1, 3}, {1, 4}, {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5},
                    {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {4, 1}, {4, 2}, {4, 4}, {4, 5},
            };
        }

        for (int i = 0; i < ship.length; i++) {
            for (int j = 0; j < ship[i].length; j++) {

                boolean valid = false;
                for (int[] coord : validCoords) {
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

    /**
     * Retrieves the symbolic representation of a given component.
     * If the provided component is null, a default symbol "_" is returned.
     *
     * @param c the component whose symbolic representation is to be retrieved;
     *          if null, a default "_" symbol is returned.
     * @return the symbol representing the component, or "_" if the component is null.
     */
    public String getSymbol(Component c) {
        if (c == null) return "_";

        return c.toSymbol();
    }

    /**
     * Prints details about a specified component, including its information and its connector positions.
     * If the component is null, it outputs a message indicating an empty tile.
     *
     * @param c the component whose information and connectors are to be printed;
     *          if null, a message indicating an empty tile is displayed
     */
    // c-getInfo usa binding dinamico in component
    public void printInfoTile(Component c) {
        if (c == null) {
            System.out.println("Tile vuota");
            return;
        }

        print(c.getInfo());
        print("Connettori:\n" +
                "  ↑ Sopra : " + c.getUp() + "\n" +
                "  → Destra : " + c.getRight() + "\n" +
                "  ↓ Sotto : " + c.getDown() + "\n" +
                "  ← Sinistra : " + c.getLeft() + "\n");
    }




}
