package it.polimi.ingsw.psp23.model.enumeration;

public enum Side {
    SINGLE_CONNECTOR,
    DOUBLE_CONNECTOR,
    UNIVERSAL_CONNECTOR,
    EMPTY,
    GUN,
    ENGINE,
    SHIELD,
    SHIELD_SINGLE_CONNECTOR,
    SHIELD_DOUBLE_CONNECTOR;
}

// alcune Side come GUN, SHIELD ecc. sono necessarie perchè quando chiamo component.rotate() devo far ruotare anche il cannone/lo scudo
// Bastano GUN e ENGINE perchè poi per capire se è doppio chiamo il rispettivo metodo isDouble()