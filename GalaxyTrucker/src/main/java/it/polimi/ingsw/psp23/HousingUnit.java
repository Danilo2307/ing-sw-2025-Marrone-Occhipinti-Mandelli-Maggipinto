package it.polimi.ingsw.psp23;

import java.util.ArrayList;
import java.util.List;

public class HousingUnit extends Component {
    private int numAstronaut = 0;
    private Color alien = null;
    private List<Color> connectedAddons = new ArrayList<>();
    private final boolean isStartingCabin;

    public HousingUnit(Side up, Side down, Side left, Side right, boolean isStartingCabin) {
        super(up, down, left, right);
        this.isStartingCabin = isStartingCabin;
        if (isStartingCabin) numAstronaut = 2;
    }

    public boolean canContainAlien(Color color) { return connectedAddons.contains(color); }

    public void setAlien(Color color) { if (numAstronaut == 0) this.alien = color; }

    public void setAstronaut() { if (alien == null) this.numAstronaut = 2; }

    public void reduceOccupants(int num) {
        if (alien != null) {
            alien = null;
        } else if (num <= numAstronaut && num >= 0) {
            numAstronaut -= num;
        }
    }

    public void addConnectedAddon(Color color) { if (!connectedAddons.contains(color)) connectedAddons.add(color); }

    public void clearOccupants() {
        this.numAstronaut = 0;
        this.alien = null;
    }

    public Color getAlien() { return alien; }

    public int getNumAstronaut() { return numAstronaut; }

    public boolean isStartingCabin() { return isStartingCabin; }

    public List<Color> getConnectedAddons() { return new ArrayList<>(connectedAddons); }
}
