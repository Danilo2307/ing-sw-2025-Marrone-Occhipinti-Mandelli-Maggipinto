package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.model.enumeration.Side;

/**
 * Represents a structural component in the system.
 *
 * A StructuralComponent is a specific type of `Component` that embodies
 * the structural elements of a system. It inherits the base functionality
 * of the `Component` class, including states and positioning, but uniquely
 * represents itself with a specific symbol and informational string.
 *
 *  It does not introduce additional state beyond the inherited properties
 *  of the base `Component` class.
 */
public final class StructuralComponent extends Component {
    // Tubi: non hanno nessuna caratteristica se non quelle di Component
    public StructuralComponent(Side up, Side down, Side left, Side right, int id){
        super(up, down, left, right, id);
    }

    @Override
    public String toSymbol() {
        return "T";
    }

    @Override
    public  String getInfo() {
        return "Tubi\n";
    }
}
