package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import java.util.ArrayList;
import java.util.List;

public class HousingUnit extends Component {
    //GIGI
    private int numAstronaut = 0;
    private Color alien = null;
    private List<Color> connectedAddons = new ArrayList<>();
    private final boolean isStartingCabin;

    public HousingUnit(Side up, Side down, Side left, Side right, boolean isStartingCabin) {
        super(up, down, left, right);
        this.isStartingCabin = isStartingCabin;
        if (isStartingCabin) numAstronaut = 2;
    }

    // Serve a assigncrew() per vedere se è possibile mettere un tipo di alieno
    public boolean canContainAlien(Color color) { return connectedAddons.contains(color); }

    // Entrambi i set sono fatti solo prima del primo turno, non vengono più utilizzati
    public void setAlien(Color color) { if (numAstronaut == 0) this.alien = color; }

    public void setAstronaut() { if (alien == null) this.numAstronaut = 2; }

    // Servirà a reduceCrew() per la eliminazione di membri dal Component
    public void reduceOccupants(int num) {
        if (alien != null) {
            alien = null;
        } else if (num <= numAstronaut && num >= 0) {
            numAstronaut -= num;
        }
    }

    //Servirà a checkHousingUnit per vedere quali tipi di alieni può ospitare
    public void addConnectedAddon(Color color) { if (!connectedAddons.contains(color)) connectedAddons.add(color); }

    public Color getAlien() { return alien; }

    public int getNumAstronaut() { return numAstronaut; }

    public boolean isStartingCabin() { return isStartingCabin; }

//    public List<Color> getConnectedAddons() { return new ArrayList<>(connectedAddons); }
//}
