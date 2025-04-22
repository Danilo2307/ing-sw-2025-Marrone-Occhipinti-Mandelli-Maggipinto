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


    public boolean isShield() {
        return (this == SHIELD || this == SHIELD_SINGLE_CONNECTOR || this == SHIELD_DOUBLE_CONNECTOR);
    }

    public ConnectorType connectorType() {
        return switch (this) {
            case SINGLE_CONNECTOR, SHIELD_SINGLE_CONNECTOR -> ConnectorType.SINGLE;
            case DOUBLE_CONNECTOR, SHIELD_DOUBLE_CONNECTOR -> ConnectorType.DOUBLE;
            case UNIVERSAL_CONNECTOR -> ConnectorType.UNIVERSAL;
            default -> ConnectorType.NONE;
        };
    }
}

// alcune Side come GUN, SHIELD ecc. sono necessarie perchè quando chiamo component.rotate() devo far ruotare anche il cannone/lo scudo.
// Bastano GUN e ENGINE perchè poi per capire se è doppio chiamo il rispettivo metodo isDouble()