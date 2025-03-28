package it.polimi.ingsw.psp23.model.Game;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Side;

import java.util.ArrayList;

// Utility Class che istanzia tutti i componenti della nave
public class ComponentFactory {

    // costruttore private e vuoto, così impedisco istanziazione: i components vengono creati una sola volta chiamando dal Game ComponentFactory.generateAllComponents()
    private ComponentFactory() {};

    // non necessita di uno stato interno dell'oggetto; può essere usato senza l'istanza della classe
    public static ArrayList<Component> generateAllComponents() {
        ArrayList<Component> components = new ArrayList<>();
        components.addAll(generateContainers());
        components.addAll(generateHousingUnits());
        components.addAll(generateBatteryHubs());
        components.addAll(generateAlienAddOns());
        components.addAll(generateCannons());
        components.addAll(generateEngines());
        components.addAll(generateShields());
        components.addAll(generateStructuralComponents());

        return components;
    }

    // sottometodi statici perchè anche questi non devono essere chiamati su oggetti. Privati perchè sono metodi di supporto che servono solo alla classe.
    private static ArrayList<Component> generateContainers() {
        ArrayList<Component> subComponents = new ArrayList<>();
        // ordine Side: up, down, left, right
        // tutti container blu
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, 2, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, 2, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 2, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, 2, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, 2, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, 2, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, 2, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, 2, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container(Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, 2, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.EMPTY, 3, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container(Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, 3, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.EMPTY, 3, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, 3, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container( Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, 3, Color.Blue, new ArrayList<>() ));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, 3, Color.Blue, new ArrayList<>() ));
        // tutti container rossi
        subComponents.add(new Container(Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, 1, Color.Red, new ArrayList<>() ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 1, Color.Red, new ArrayList<>() ));
        subComponents.add(new Container(Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, 1, Color.Red, new ArrayList<>() ));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, 1, Color.Red, new ArrayList<>() ));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, 1, Color.Red, new ArrayList<>() ));
        subComponents.add(new Container(Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, 1, Color.Red, new ArrayList<>() ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.EMPTY, 2, Color.Red, new ArrayList<>() ));
        subComponents.add(new Container(Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, 1, Color.Red, new ArrayList<>() ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, 2, Color.Red, new ArrayList<>() ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.EMPTY, 2, Color.Red, new ArrayList<>() ));

        return subComponents;
    }

    private static ArrayList<Component> generateHousingUnits() {
        ArrayList<Component> subComponents = new ArrayList<>();
        // ordine Side: up, down, left, right
        // cabine speciali : ???VALUTO INTRODUZIONE ATTRIBUTO COLOR???
        subComponents.add(new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true));
        subComponents.add(new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true));
        subComponents.add(new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true));
        subComponents.add(new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true));

        // cabine normali
        subComponents.add(new HousingUnit(Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, false));
        subComponents.add(new HousingUnit(Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, false));
        subComponents.add(new HousingUnit(Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, false));
        subComponents.add(new HousingUnit(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.EMPTY, false));
        subComponents.add(new HousingUnit(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, false));
        subComponents.add(new HousingUnit(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, false));
        subComponents.add(new HousingUnit(Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, false));
        subComponents.add(new HousingUnit(Side.SINGLE_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, false));
        subComponents.add(new HousingUnit(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, false));
        subComponents.add(new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.EMPTY, false));
        subComponents.add(new HousingUnit(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, false));
        subComponents.add(new HousingUnit(Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, false));
        subComponents.add(new HousingUnit(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, false));
        subComponents.add(new HousingUnit(Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, false));
        subComponents.add(new HousingUnit(Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, false));
        subComponents.add(new HousingUnit(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, false));
        subComponents.add(new HousingUnit(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, false));

        return subComponents;
    }

    private static ArrayList<Component> generateCannons() {
    }

    private static ArrayList<Component> generateEngines() {}

    private static ArrayList<Component> generateShields() {}

    private static ArrayList<Component> generateStructuralComponents() {
        ArrayList<Component> subComponents = new ArrayList<>();

        // ordine Side: up, down, left, right
        subComponents.add(new StructuralComponent(Side.UNIVERSAL_CONNECTOR,  Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR));
        subComponents.add(new StructuralComponent(Side.SINGLE_CONNECTOR,  Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR));
        subComponents.add(new StructuralComponent(Side.SINGLE_CONNECTOR,  Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR));
        subComponents.add(new StructuralComponent(Side.SINGLE_CONNECTOR,  Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR));
        subComponents.add(new StructuralComponent(Side.DOUBLE_CONNECTOR,  Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR));
        subComponents.add(new StructuralComponent(Side.UNIVERSAL_CONNECTOR,  Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR));
        subComponents.add(new StructuralComponent(Side.UNIVERSAL_CONNECTOR,  Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR));
        subComponents.add(new StructuralComponent(Side.UNIVERSAL_CONNECTOR,  Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR));

        return subComponents;
    }

    private static ArrayList<Component> generateBatteryHubs() {}

    private static ArrayList<Component> generateAlienAddOns() {}





}
