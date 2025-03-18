package it.polimi.ingsw.psp23;

public class StructuralComponent extends Component{
    // Alberto

    // la classe StructuralComponent include tutti quei component con praticamente "nessun metodo utile"
    // come ad esempio gun, double gun, engine, double engine e structural modules

    private final String type;

    StructuralComponent(Side up, Side down, Side left, Side right, String type){
        super(up, down, left, right);
        this.type = type;
    }

    public String getType(){
        return type;
    }

}
