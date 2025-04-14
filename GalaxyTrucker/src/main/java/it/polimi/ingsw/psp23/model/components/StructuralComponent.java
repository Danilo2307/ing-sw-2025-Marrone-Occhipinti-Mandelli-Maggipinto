package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public final class StructuralComponent extends Component {
    // Alberto
    // Tubi: non hanno nessuna caratteristica se non quelle di Component
    public StructuralComponent(Side up, Side down, Side left, Side right){
        super(up, down, left, right);
    }
}
