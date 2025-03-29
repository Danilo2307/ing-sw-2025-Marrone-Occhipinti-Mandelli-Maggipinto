package it.polimi.ingsw.psp23.model.components;

import it.polimi.ingsw.psp23.model.enumeration.ComponentType;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public class Shield extends Component{

    // VALUTO ATTRIBUTI COVERED per indicare Direction coperta; verranno settati quando lo Shield è stato saldato.
    // Costruttore rimane invariato in ogni caso perchè all'istanziazione non posso sapere lato coperto (a causa di possibili rotazioni del player).

    public Shield(Side up, Side down, Side left, Side right) {
        super((ComponentType.SHIELD), up, down, left, right);
    }

}
