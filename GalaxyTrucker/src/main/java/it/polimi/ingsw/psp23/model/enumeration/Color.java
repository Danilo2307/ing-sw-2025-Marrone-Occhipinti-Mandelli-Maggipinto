package it.polimi.ingsw.psp23.model.enumeration;

import java.util.Map;

public enum Color {
    Brown,
    Purple,
    Green,
    Yellow,
    Red,
    Blue;

    // I don't put it in parse otherwise it would be created at every call (waste)
    private static final Map<String, Color> aliasMap = Map.of(
            "marrone", Brown,
            "viola" , Purple,
            "verde", Green,
            "giallo", Yellow,
            "rosso", Red,
            "blu", Blue
    );

    /**
     * Parses the provided input string and returns the corresponding Color enum value
     * based on the predefined alias mappings.
     *
     * @param input the string representation of a color alias to parse
     * @return the matching Color enum value, or null if no match is found
     */
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