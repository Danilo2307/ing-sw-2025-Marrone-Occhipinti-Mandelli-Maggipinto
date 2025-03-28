package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.model.enumeration.ComponentType;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public class StructuralComponent extends Component {
    // Alberto
    // Tubi
    StructuralComponent(Side up, Side down, Side left, Side right, String subType){
        super(ComponentType.STRUCTURAL_COMPONENT,up, down, left, right);
    }
}
