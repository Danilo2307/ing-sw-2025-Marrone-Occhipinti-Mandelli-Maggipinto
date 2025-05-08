package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public final class AlienAddOns extends Component {
    //danilo
    private final Color c;

    public AlienAddOns(Side up, Side down, Side left, Side right, Color c) {
        super(up, down, left, right);
        this.c = c;
    }

    public Color getColor(){ return c; }

    @Override
    public String toSymbol() {
        return this.getColor() == Color.Purple ? "Ap" : "Ab";
    }

    @Override
    public String getInfo() {
        return "Supporto vitale di colore " + this.getColor() + "\n";
    }

}
