package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.exceptions.InvalidComponentActionException;
import it.polimi.ingsw.psp23.exceptions.LevelException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import java.util.ArrayList;

public final class HousingUnit extends Component {
    //GIGI
    private int numAstronaut = 0;
    private Color alien = null;
    private final ArrayList<Color> connectedAddons = new ArrayList<>();
    private final boolean isStartingCabin;

    public HousingUnit(Side up, Side down, Side left, Side right, boolean isStartingCabin, int id) {
        super(up, down, left, right, id);
        this.isStartingCabin = isStartingCabin;
        if (isStartingCabin) this.setAstronaut();
    }

    // check se esiste un supporto vitale adiacente alla cabina dello stesso colore dell'alieno che voglio inserire
    public boolean canContainAlien(Color color) {
        return connectedAddons.contains(color);
    }

    // Entrambi i set verranno chiamati dal controller solo prima del primo turno, non vengono più utilizzati
    // verifica prima che la lista di housing unit presente in board non contenga già alieni di questo colore (per ogni board solo un alieno viola e solo uno marrone al massimo)
    public void setAlien(Color color) {
        // non strettamente necessaria perchè connectedAddonds sarebbe vuoto, ma così dò messaggio più preciso

        if (numAstronaut == 0 && canContainAlien(color) && !isStartingCabin)
            this.alien = color;
        else if(!canContainAlien(color)){
            throw new InvalidComponentActionException("Non hai il supporto per alieno " + color );
        }
        else
            throw new InvalidComponentActionException("Cannot place alien: cabin is not eligible (check crew, adjacency, or central module).");
    }

    public void setAstronaut() {
        if (alien == null) this.numAstronaut = 2;
        else throw new InvalidComponentActionException("Error: cabina già occupata da un alieno! ");
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

    //Servirà a updateAllowedAliens per vedere quali tipi di alieni può ospitare
    public void addConnectedAddon(Color color) {
        if (!connectedAddons.contains(color)) connectedAddons.add(color);
    }

    public Color getAlien() {
        return alien;
    }

    public int getNumAstronaut() {
        return numAstronaut;
    }

    @Override
    public boolean isStartingCabin() {
        return this.isStartingCabin;
    }

    @Override
    public String toSymbol() {
        return "H";
    }

    @Override
    public String getInfo() {
        String content;
        if (this.getAlien() != null) {
            content = "un alieno di colore\n" + this.getAlien() + "\n";
        } else {
            content = this.numAstronaut + " astronauti\n";
        }
        return "La cabina contiene " + content + "\n";
    }

}