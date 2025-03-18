package it.polimi.ingsw.psp23.model.components;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public class Shield extends Component {
    // Alberto

    private boolean upCovered;
    private boolean downCovered;
    private boolean rightCovered;
    private boolean leftCovered;

    Shield(Side up, Side down, Side left, Side right, boolean upCovered, boolean downCovered, boolean rightCovered, boolean leftCovered){
        super(up, down, left, right);
        this.upCovered = upCovered;
        this.downCovered = downCovered;
        this.rightCovered = rightCovered;
        this.leftCovered = leftCovered;
    }

    public Boolean getRightCovered(){
        return rightCovered;
    }

    public Boolean getLeftCovered(){
        return leftCovered;
    }

    public Boolean getUpCovered(){
        return upCovered;
    }

    public Boolean getDownCovered(){
        return downCovered;
    }

}
