package it.polimi.ingsw.psp23.model.enumeration;

import java.util.Map;

public enum Color {
    Brown,
    Purple,
    Green,
    Yellow,
    Red,
    Blue;

    // non la metto in parse se no la creerei ad ogni chiamata (spreco)
    private static final Map<String, Color> aliasMap = Map.of(
            "marrone", Brown,
            "viola" , Purple,
            "verde", Green,
            "giallo", Yellow,
            "rosso", Red,
            "blu", Blue
    );

    public static Color parse(String input) {
        return aliasMap.get(input);
    }

    @Override
    public String toString() {
        return switch (this) {
            case Brown -> "marrone";
            case Purple -> "viola";
            case Green -> "verde";
            case Yellow -> "gialla";
            case Red -> "rossa";
            case Blue -> "blu";
        };
    }


}