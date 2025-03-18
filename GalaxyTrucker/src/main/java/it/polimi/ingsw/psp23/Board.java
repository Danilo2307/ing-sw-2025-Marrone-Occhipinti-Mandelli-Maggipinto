package it.polimi.ingsw.psp23;
import java.util.List;


public class Board {
    private Component[][] ship;
    private List<Component> garbage;
    private int crew;
    private int batteries;
    private double cannonStrength;
    private int engineStrength;
    private int exposedConnectors;
    private int[] goods;
    private final int rows = 5;
    private final int cols = 7;

    Board(List<Component> garbage, int crew, int batteries, int engineStrength, int exposedConnectors, int[] goods, double cannonStrength) {
        this.ship = new Component[rows][cols];
        this.garbage = garbage;

    }
}
