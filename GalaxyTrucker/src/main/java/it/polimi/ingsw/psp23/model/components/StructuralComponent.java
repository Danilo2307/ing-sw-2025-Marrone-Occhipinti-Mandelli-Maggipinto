package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public class StructuralComponent extends Component {
    // Alberto

    // la classe StructuralComponent include tutti quei component con praticamente "nessun metodo utile"
    // come ad esempio gun, double gun, engine, double engine, structural modules e shield

    private final String type;

    StructuralComponent(String type, Side up, Side down, Side left, Side right, String subType){
        super("Structural Component"up, down, left, right);
        this.type = subType;
    }

    public String getType(){
        return new String(type);
    }

}
