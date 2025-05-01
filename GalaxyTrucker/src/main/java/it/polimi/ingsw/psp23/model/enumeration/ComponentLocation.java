package it.polimi.ingsw.psp23.model.enumeration;

public enum ComponentLocation {
    PILE,       // nel mucchio
    IN_HAND,    //in mano al player
    FACE_UP,    // a testa in su (scartato dal player)
    ON_TRUCK,    // saldato sulla nave: x,y assumono valore positivo
    RESERVED    // prenotato
}
