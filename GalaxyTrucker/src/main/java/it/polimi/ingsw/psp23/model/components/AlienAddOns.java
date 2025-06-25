package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Side;

/**
 * The AlienAddOns class represents a specialized type of component in the system,
 * identified by its unique color. It extends the Component base class and
 * provides additional functionality specific to alien add-on components.
 *
 * This class encapsulates color-specific behavior and provides appropriate
 * representations for its symbol and characteristics in textual format.
 */
public final class AlienAddOns extends Component {
    private final Color c;

    public AlienAddOns(Side up, Side down, Side left, Side right, Color c, int id) {
        super(up, down, left, right, id);
        this.c = c;
    }

    public Color getColor(){ return c; }

    /**
     * Returns a string representation of the alien add-on component's symbol based on its color.
     * If the color is purple, the symbol "Ap" is returned. For other colors, the symbol "Ab" is used.
     *
     * @return a string representation of the alien add-on's symbol, "Ap" for purple or "Ab" for other colors
     */
    @Override
    public String toSymbol() {
        return this.getColor() == Color.Purple ? "Ap" : "Ab";
    }

    /**
     * Returns information about the alien add-on component, including its unique color description.
     *
     * @return a string that describes the alien add-on component, including its color information.
     */
    @Override
    public String getInfo() {
        return "Supporto vitale di colore " + this.getColor() + "\n";
    }

}
