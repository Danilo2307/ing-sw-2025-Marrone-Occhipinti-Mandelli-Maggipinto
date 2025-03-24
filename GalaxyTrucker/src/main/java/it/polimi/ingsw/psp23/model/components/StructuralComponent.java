package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.model.enumeration.ComponentType;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public class StructuralComponent extends Component {
    // Alberto

    // la classe StructuralComponent include tutti quei component con praticamente "nessun metodo utile"
    // come ad esempio gun, double gun, engine, double engine, structural modules e shield

    private final ComponentType type;

    StructuralComponent(ComponentType type, Side up, Side down, Side left, Side right, ComponentType subType){
        super("StructuralComponent",up, down, left, right);
        this.type = subType;
    }

    public ComponentType getType(){
        return type;
    }

}
