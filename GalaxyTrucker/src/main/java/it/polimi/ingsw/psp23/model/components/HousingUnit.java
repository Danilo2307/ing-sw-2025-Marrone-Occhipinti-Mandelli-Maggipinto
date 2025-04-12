package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.ComponentType;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import java.util.ArrayList;
import java.util.List;

public class HousingUnit extends Component {
    //GIGI
    private int numAstronaut = 0;
    private Color alien = null;
    private ArrayList<Color> connectedAddons = new ArrayList<>();
    private final boolean isStartingCabin;

    public HousingUnit(Side up, Side down, Side left, Side right, boolean isStartingCabin) {
        super(ComponentType.HOUSINGUNIT, up, down, left, right);
        this.isStartingCabin = isStartingCabin;
        if (isStartingCabin) numAstronaut = 2;
    }

    // Serve a assigncrew() per vedere se è possibile mettere un tipo di alieno
    public boolean canContainAlien(Color color) {
        return connectedAddons.contains(color);
    }

    // Entrambi i set verranno chiamati dal controller solo prima del primo turno, non vengono più utilizzati
    // TODO: verifica prima che la lista di housing unit presente in board non contenga già alieni di questo colore (per ogni board solo un alieno viola e solo uno marrone al massimo)
    public void setAlien(Color color) {
        if (numAstronaut == 0) this.alien = color;
    }

    public void setAstronaut() {
        if (alien == null) this.numAstronaut = 2;
    }

    // Servirà a reduceCrew() per la eliminazione di membri dal Component
    public void reduceOccupants(int num) {
        // una cabina può contenere solo un alieno oppure 1/2 umani
        if (alien != null) {
            if (num == 1)
                alien = null;
            else
                throw new IllegalArgumentException("You can't remove more than one alien!");
        }
        else {
            if (num <= numAstronaut && num >= 0)
                numAstronaut -= num;
            else
                throw new IllegalArgumentException("Number of humans to remove is invalid");
        }
    }

    //Servirà a checkHousingUnit per vedere quali tipi di alieni può ospitare
    public void addConnectedAddon(Color color) {
        if (!connectedAddons.contains(color)) connectedAddons.add(color);
    }

    public Color getAlien() {
        return alien;
    }

    public int getNumAstronaut() {
        return numAstronaut;
    }

    public boolean isStartingCabin() {
        return isStartingCabin;
    }

//    public List<Color> getConnectedAddons() { return new ArrayList<>(connectedAddons); }
//}
}