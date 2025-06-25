package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.exceptions.InvalidComponentActionException;
import it.polimi.ingsw.psp23.exceptions.LevelException;
import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import java.util.ArrayList;


/**
 * The HousingUnit class represents a specific type of component designed
 * to house astronauts and aliens. It enforces constraints on whether a specific alien or number
 * of astronauts can occupy the unit, depending on its state and connected elements.
 * HousingUnit also supports operations related to crew management and the addition of connected addons.
 * This class is immutable regarding whether it is a starting cabin and extends from the Component class.
 */
public final class HousingUnit extends Component {
    private int numAstronaut = 0;
    private Color alien = null;
    private final ArrayList<Color> connectedAddons = new ArrayList<>();
    private final boolean isStartingCabin;

    public HousingUnit(Side up, Side down, Side left, Side right, boolean isStartingCabin, int id) {
        super(up, down, left, right, id);
        this.isStartingCabin = isStartingCabin;
        if (isStartingCabin) this.setAstronaut();
    }

    /**
     * Checks whether the housing unit can contain an alien of the specified color
     * based on the presence of connected addons of the same color.
     *
     * @param color the color of the alien to be checked
     * @return true if the housing unit contains a connected addon of the specified color, false otherwise
     */
    // check se esiste un supporto vitale adiacente alla cabina dello stesso colore dell'alieno che voglio inserire
    public boolean canContainAlien(Color color) {
        return connectedAddons.contains(color);
    }

    /**
     * Sets an alien of the specified color to the housing unit.
     * This method ensures that the alien can be placed in the housing unit based on
     * eligibility checks such as the lack of astronauts, the presence of connected addons
     * of the specified color, and whether the unit is not the starting cabin.
     * If any condition is not met, an exception is thrown.
     *
     * @param color the color of the alien to be placed in the housing unit
     * @throws InvalidComponentActionException if the housing unit cannot contain the alien
     *         due to invalid conditions or lack of necessary support
     */
    // verifica prima che la lista di housing unit presente in board non contenga già alieni di questo colore (per ogni board solo un alieno viola e solo uno marrone al massimo)
    public void setAlien(Color color) {

        if (numAstronaut == 0 && canContainAlien(color) && !isStartingCabin)
            this.alien = color;
        else if(!canContainAlien(color)){
            throw new InvalidComponentActionException("Non hai il supporto per alieno " + color );
        }
        else
            throw new InvalidComponentActionException("Cannot place alien: cabin is not eligible (check crew, adjacency, or central module).");
    }

    /**
     * Sets the number of astronauts within the housing unit.
     *
     * This method assigns a default number of astronauts (2) to the housing unit
     * if it is currently unoccupied by any alien. If the unit is already occupied
     * by an alien, an exception is thrown to prevent conflicting occupants.
     *
     * @throws InvalidComponentActionException if the housing unit is already occupied by an alien.
     */
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

    /**
     * Adds a connected addon of the specified color to the housing unit.
     * If the specified color is not already present in the list of connected addons,
     * it will be added.
     *
     * @param color the color of the addon to connect to the housing unit
     */
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

    /**
     * Determines if the housing unit is the starting cabin.
     *
     * @return true if the housing unit is the starting cabin, false otherwise.
     */
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