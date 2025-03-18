package it.polimi.ingsw.psp23;
import java.util.ArrayList;
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

    public Board() {
        ship = new Component[rows][cols];
        garbage = new ArrayList<>();
        crew = 0;
        batteries = 0;
        cannonStrength = 0;
        engineStrength = 0;
        exposedConnectors = 0;
        goods = new int[4];

    }

    public Board(Board other) {
        // costruttore di copia
    }


    public boolean check() {
    }

    public Component[][] getShip() {
    }

    public boolean isValid(int i, int j) {
    }

    public void delete(int i, int j) {
    }

    public boolean isFree(int i, int j) {
    }

    public void reduceBatteries() {
    }

    public void reduceCrew(int numMembers) {
    }

    public List<Component> searchComponent(Component c) {
    }

    public void addComponent(Component c, int i, int j) {
    }

    public void releaseComponent(int i, int j) {
    }

    public void calculateExposedConnectors() {
    }

    public int getExposedConnectors() {
        return exposedConnectors;
    }

    public double getCannonStrength() {
        return cannonStrength;
    }

    public int getEngineStrength() {
        return engineStrength;
    }

    public Component[][] getShip() {
    }

    public int getCrew() {
        return crew;
    }

    public void pickMostImportantGoods(int numGoodsStolen) {
    }

    public void handleCannonShot(CannonShot cannonShot, int impactLine) {
    }

    public void handleMeteor(Meteor meteor, int impactLine) {
    }

    public void loadGoods(List<Item> items) {
    }

    public void checkHousingUnits() {
    }
}
