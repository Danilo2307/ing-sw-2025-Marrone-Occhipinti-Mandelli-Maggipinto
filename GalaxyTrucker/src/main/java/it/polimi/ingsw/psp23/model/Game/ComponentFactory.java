package it.polimi.ingsw.psp23.model.Game;

import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Side;

import java.util.ArrayList;


/**
 * The ComponentFactory class is a factory for creating Component objects. 
 * This class is designed to generate and manage the creation of every 
 * type of components used in a game. It is intended to be used as a 
 * static utility class and cannot be instantiated.
 *
 * Methods in this class include a primary method for generating all components
 * at a specified game level, as well as private helper methods for creating
 * different categories of components. The generated components are returned 
 * as collections for further processing or integration into the game.
 */
public class ComponentFactory {

    // private empty constructor to prevent instantiation: components are created only once by calling ComponentFactory.generateAllComponents() from Game
    private ComponentFactory() {}

    /**
     * Generates a list of all available components based on the specified level.
     * This method aggregates various categories of components, such as containers, 
     * housing units, cannons, engines, shields, structural components, battery hubs, 
     * and optionally alien add-ons if the level is set to 2.
     *
     * @param level the level of components to be generated. If level is 2, alien add-ons 
     *              are included in the result.
     * @return a list of all components available for the specified level.
     */
    public static ArrayList<Component> generateAllComponents(int level) {
        ArrayList<Component> components = new ArrayList<>();
        components.addAll(generateContainers());
        components.addAll(generateHousingUnits());
        components.addAll(generateBatteryHubs());
        components.addAll(generateCannons());
        components.addAll(generateEngines());
        components.addAll(generateShields());
        components.addAll(generateStructuralComponents());
        if (level == 2) {
            components.addAll(generateAlienAddOns());
        }

        return components;
    }

    /**
     * Generates a list of container components with predefined properties.
     * The containers are categorized by specific attributes such as connectors,
     * color, weight, and unique identifiers. This method is a helper function 
     * specific to the ComponentFactory class and provides the list of container 
     * components for further use in the application.
     *
     * @return an ArrayList of Component objects representing the generated containers.
     */
    private static ArrayList<Component> generateContainers() {
        ArrayList<Component> subComponents = new ArrayList<>();
        // Side order: up, down, left, right
        // every blue container
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, 2, Color.Blue, new ArrayList<>(), 11));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, 2, Color.Blue, new ArrayList<>(), 12));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 2, Color.Blue, new ArrayList<>(), 13 ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, 2, Color.Blue, new ArrayList<>(), 14 ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, 2, Color.Blue, new ArrayList<>(), 15 ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, 2, Color.Blue, new ArrayList<>(), 16 ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, 2, Color.Blue, new ArrayList<>(), 17 ));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, 2, Color.Blue, new ArrayList<>(), 18 ));
        subComponents.add(new Container(Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, 2, Color.Blue, new ArrayList<>(), 19 ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.EMPTY, 3, Color.Blue, new ArrayList<>(), 110 ));
        subComponents.add(new Container(Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, 3, Color.Blue, new ArrayList<>(), 111 ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.EMPTY, 3, Color.Blue, new ArrayList<>(), 112 ));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, 3, Color.Blue, new ArrayList<>(), 113 ));
        subComponents.add(new Container( Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, 3, Color.Blue, new ArrayList<>(), 114 ));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, 3, Color.Blue, new ArrayList<>(), 115 ));
        // every red container
        subComponents.add(new Container(Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, 1, Color.Red, new ArrayList<>(), 116 ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 1, Color.Red, new ArrayList<>(), 117 ));
        subComponents.add(new Container(Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, 1, Color.Red, new ArrayList<>(), 118 ));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, 1, Color.Red, new ArrayList<>(), 119 ));
        subComponents.add(new Container(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, 1, Color.Red, new ArrayList<>(), 120 ));
        subComponents.add(new Container(Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, 1, Color.Red, new ArrayList<>(), 121 ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.EMPTY, 2, Color.Red, new ArrayList<>(), 122 ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, 2, Color.Red, new ArrayList<>(), 123 ));
        subComponents.add(new Container(Side.EMPTY, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.EMPTY, 2, Color.Red, new ArrayList<>(), 124 ));

        return subComponents;
    }

    /**
     * Generates a list of housing unit components with predefined properties.
     * Each housing unit is configured with specific connector types, orientations,
     * and unique identifiers. The generated components are added to an ArrayList
     * for further use within the application.
     *
     * @return an ArrayList of Component objects representing the generated housing units.
     */
    private static ArrayList<Component> generateHousingUnits() {
        ArrayList<Component> subComponents = new ArrayList<>();
        // Side order: up, down, left, right
        // Regular housing units (special ones are instantiated elsewhere because they won't go in the heap)
        subComponents.add(new HousingUnit(Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, false, 21));
        subComponents.add(new HousingUnit(Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, false, 22));
        subComponents.add(new HousingUnit(Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, false, 23));
        subComponents.add(new HousingUnit(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.EMPTY, false, 24));
        subComponents.add(new HousingUnit(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, false, 25));
        subComponents.add(new HousingUnit(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, false, 26));
        subComponents.add(new HousingUnit(Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, false, 27));
        subComponents.add(new HousingUnit(Side.SINGLE_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, false, 28));
        subComponents.add(new HousingUnit(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, false, 29));
        subComponents.add(new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.EMPTY, false, 210));
        subComponents.add(new HousingUnit(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, false, 211));
        subComponents.add(new HousingUnit(Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, false, 212));
        subComponents.add(new HousingUnit(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, false, 213));
        subComponents.add(new HousingUnit(Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, false, 214));
        subComponents.add(new HousingUnit(Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, false, 215));
        subComponents.add(new HousingUnit(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, false, 216));
        subComponents.add(new HousingUnit(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, false, 217));

        return subComponents;
    }

    /**
     * Generates a list of cannon components with predefined properties.
     * The cannons are categorized based on their configuration, which includes
     * aspects such as connector types, orientations, and whether they are single
     * or double cannons. These components are created with unique identifiers
     * and specific attributes tailored for use in the application.
     *
     * @return an ArrayList of Component objects representing the generated cannons.
     */
    private static ArrayList<Component> generateCannons() {
        ArrayList<Component> subComponents = new ArrayList<>();

        // generate all single cannons
        subComponents.add(new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, false, 31));
        subComponents.add(new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, false, 32));
        subComponents.add(new Cannon(Side.GUN, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, false, 33));
        subComponents.add(new Cannon(Side.GUN, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, false, 34));
        subComponents.add(new Cannon(Side.GUN, Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, false, 35));
        subComponents.add(new Cannon(Side.GUN, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, false, 36));
        subComponents.add(new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, false, 37));
        subComponents.add(new Cannon(Side.GUN, Side.EMPTY, Side.EMPTY, Side.DOUBLE_CONNECTOR, false, 38));
        subComponents.add(new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, false, 39));
        subComponents.add(new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, false, 310));
        subComponents.add(new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, false, 311));
        subComponents.add(new Cannon(Side.GUN, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.EMPTY, false, 312));
        subComponents.add(new Cannon(Side.GUN, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, false, 313));
        subComponents.add(new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, false, 314));
        subComponents.add(new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, false, 315));
        subComponents.add(new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, false, 316));
        subComponents.add(new Cannon(Side.GUN, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false, 317));
        subComponents.add(new Cannon(Side.GUN, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.EMPTY, false, 318));
        subComponents.add(new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, false, 319));
        subComponents.add(new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, false, 320));
        subComponents.add(new Cannon(Side.GUN, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, false, 321));
        subComponents.add(new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, false, 322));
        subComponents.add(new Cannon(Side.GUN, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, false, 323));
        subComponents.add(new Cannon(Side.GUN, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, false, 324));
        subComponents.add(new Cannon(Side.GUN, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, false, 325));

        // generate all double cannons
        subComponents.add(new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, true, 326));
        subComponents.add(new Cannon(Side.GUN, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, true, 327));
        subComponents.add(new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, true, 328));
        subComponents.add(new Cannon(Side.GUN, Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, true, 329));
        subComponents.add(new Cannon(Side.GUN, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, true, 330));
        subComponents.add(new Cannon(Side.GUN, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, true, 331));
        subComponents.add(new Cannon(Side.GUN, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, true, 332));
        subComponents.add(new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, true, 333));
        subComponents.add(new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, true, 334));
        subComponents.add(new Cannon(Side.GUN, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, true, 335));
        subComponents.add(new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, true, 336));

        return subComponents;
    }

    /**
     * Generates a list of engine components with predefined properties.
     * The engines are differentiated based on their connector types,
     * orientations, and whether they are single or double engines.
     * Each engine is assigned a unique identifier and specific attributes.
     * This method is a helper function specific to the ComponentFactory class
     * and is used to provide a list of engine components for further use
     * within the application.
     *
     * @return an ArrayList of Component objects representing the generated engines.
     */
    private static ArrayList<Component> generateEngines() {
        ArrayList<Component> subComponents = new ArrayList<>();

        //generate all single engines
        subComponents.add(new Engine(Side.EMPTY, Side.ENGINE, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, false, 41));
        subComponents.add(new Engine(Side.EMPTY, Side.ENGINE, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, false, 42));
        subComponents.add(new Engine(Side.SINGLE_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.EMPTY, false, 43));
        subComponents.add(new Engine(Side.SINGLE_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.EMPTY, false, 44));
        subComponents.add(new Engine(Side.SINGLE_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.SINGLE_CONNECTOR, false, 45));
        subComponents.add(new Engine(Side.DOUBLE_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.EMPTY, false, 46));
        subComponents.add(new Engine(Side.DOUBLE_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.EMPTY, false, 47));
        subComponents.add(new Engine(Side.DOUBLE_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, false, 48));
        subComponents.add(new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.DOUBLE_CONNECTOR, false, 49));
        subComponents.add(new Engine(Side.EMPTY, Side.ENGINE, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false, 410));
        subComponents.add(new Engine(Side.DOUBLE_CONNECTOR, Side.ENGINE, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, false, 411));
        subComponents.add(new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.SINGLE_CONNECTOR, Side.EMPTY, false, 412));
        subComponents.add(new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, false, 413));
        subComponents.add(new Engine(Side.EMPTY, Side.ENGINE, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, false, 414));
        subComponents.add(new Engine(Side.SINGLE_CONNECTOR, Side.ENGINE, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, false, 415));
        subComponents.add(new Engine(Side.DOUBLE_CONNECTOR, Side.ENGINE, Side.DOUBLE_CONNECTOR, Side.EMPTY, false, 416));
        subComponents.add(new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, false, 417));
        subComponents.add(new Engine(Side.EMPTY, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, false, 418));
        subComponents.add(new Engine(Side.EMPTY, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, false, 419));
        subComponents.add(new Engine(Side.EMPTY, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, false, 420));
        subComponents.add(new Engine(Side.SINGLE_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, false, 421));

        // generate all double engines
        subComponents.add(new Engine(Side.SINGLE_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.EMPTY, true, 422));
        subComponents.add(new Engine(Side.SINGLE_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, true, 423));
        subComponents.add(new Engine(Side.DOUBLE_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.EMPTY, true, 424));
        subComponents.add(new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.SINGLE_CONNECTOR, true, 425));
        subComponents.add(new Engine(Side.SINGLE_CONNECTOR, Side.ENGINE, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, true, 426));
        subComponents.add(new Engine(Side.DOUBLE_CONNECTOR, Side.ENGINE, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, true, 427));
        subComponents.add(new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.DOUBLE_CONNECTOR, Side.EMPTY, true, 428));
        subComponents.add(new Engine(Side.EMPTY, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true, 429));
        subComponents.add(new Engine(Side.DOUBLE_CONNECTOR, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, true, 430));

        return subComponents;
    }

    /**
     * Generates a list of shield components with predefined properties.
     * Each shield is configured with specific sides and a unique identifier.
     * The method creates and adds several Shield objects to an ArrayList
     * and returns this list for further use within the application.
     *
     * @return an ArrayList of Component objects representing the generated shield components.
     */
    private static ArrayList<Component> generateShields() {
        ArrayList<Component> subComponents = new ArrayList<>();

        subComponents.add(new Shield(Side.SHIELD, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SHIELD_SINGLE_CONNECTOR, 51));
        subComponents.add(new Shield(Side.SHIELD_SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SHIELD, 52));
        subComponents.add(new Shield(Side.SHIELD_DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SHIELD_SINGLE_CONNECTOR, 53));
        subComponents.add(new Shield(Side.SHIELD, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SHIELD, 54));
        subComponents.add(new Shield(Side.SHIELD, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SHIELD_DOUBLE_CONNECTOR, 55));
        subComponents.add(new Shield(Side.SHIELD_SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SHIELD_DOUBLE_CONNECTOR, 56));
        subComponents.add(new Shield(Side.SHIELD, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.SHIELD, 57));
        subComponents.add(new Shield(Side.SHIELD, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.SHIELD_DOUBLE_CONNECTOR, 58));

        return subComponents;
    }

    /**
     * Generates a list of structural components with predefined properties.
     * Each structural component is configured with specific connector types
     * on its sides and assigned a unique identifier. These components are
     * used as tubular elements in the application and contribute to the
     * overall structure.
     *
     * @return an ArrayList of Component objects representing the generated structural components.
     */
    private static ArrayList<Component> generateStructuralComponents() {
        ArrayList<Component> subComponents = new ArrayList<>();

        // Side order: up, down, left, right
        subComponents.add(new StructuralComponent(Side.UNIVERSAL_CONNECTOR,  Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 61));
        subComponents.add(new StructuralComponent(Side.SINGLE_CONNECTOR,  Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 62));
        subComponents.add(new StructuralComponent(Side.SINGLE_CONNECTOR,  Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 63));
        subComponents.add(new StructuralComponent(Side.SINGLE_CONNECTOR,  Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 64));
        subComponents.add(new StructuralComponent(Side.DOUBLE_CONNECTOR,  Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 65));
        subComponents.add(new StructuralComponent(Side.UNIVERSAL_CONNECTOR,  Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, 66));
        subComponents.add(new StructuralComponent(Side.UNIVERSAL_CONNECTOR,  Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, 67));
        subComponents.add(new StructuralComponent(Side.UNIVERSAL_CONNECTOR,  Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, 68));

        return subComponents;
    }

    /**
     * Generates a list of battery hub components with predefined properties.
     * This method creates a collection of BatteryHub objects, configured with
     * specific connector types, battery counts, and unique identifiers.
     * The generated components are added to an ArrayList for further use
     * within the application.
     *
     * @return an ArrayList of Component objects representing the generated battery hubs.
     */
    private static ArrayList<Component> generateBatteryHubs() {
        ArrayList<Component> subComponents = new ArrayList<>();

        // generate all battery hubs with two batteries
        subComponents.add(new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, 2, 71));
        subComponents.add(new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.EMPTY, Side.DOUBLE_CONNECTOR, 2, 72));
        subComponents.add(new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, 2, 73));
        subComponents.add(new BatteryHub(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, 2, 74));
        subComponents.add(new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.EMPTY, 2, 75));
        subComponents.add(new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, 2, 76));
        subComponents.add(new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, 2, 77));
        subComponents.add(new BatteryHub(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, 2, 78));
        subComponents.add(new BatteryHub(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, 2, 79));
        subComponents.add(new BatteryHub(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, 2, 710));
        subComponents.add(new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, 2, 711));

        // generate all battery hubs with three batteries
        subComponents.add(new BatteryHub(Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, 3, 712));
        subComponents.add(new BatteryHub(Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, 3, 713));
        subComponents.add(new BatteryHub(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, Side.EMPTY, 3, 714));
        subComponents.add(new BatteryHub(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, 3, 715));
        subComponents.add(new BatteryHub(Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, 3, 716));
        subComponents.add(new BatteryHub(Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.EMPTY, 3, 717));

        return subComponents;
    }

    /**
     * Generates a list of alien add-on components with predefined properties.
     * The alien add-ons are categorized based on their connector types, colors,
     * orientations, and unique identifiers. This method creates and adds
     * several AlienAddOns objects to an ArrayList and returns this list for
     * further use within the application.
     *
     * @return an ArrayList of Component objects representing the generated alien add-ons.
     */
    private static ArrayList<Component> generateAlienAddOns() {
        ArrayList<Component> subComponents = new ArrayList<>();

        // generate all brown alien add ons
        subComponents.add(new AlienAddOns(Side.SINGLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Color.Brown, 81));
        subComponents.add(new AlienAddOns(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Color.Brown, 82));
        subComponents.add(new AlienAddOns(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Color.Brown, 83));
        subComponents.add(new AlienAddOns(Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Color.Brown, 84));
        subComponents.add(new AlienAddOns(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, Color.Brown, 85));
        subComponents.add(new AlienAddOns(Side.SINGLE_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Color.Brown, 86));

        // generate all purple alien add ons
        subComponents.add(new AlienAddOns(Side.SINGLE_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Color.Purple, 87));
        subComponents.add(new AlienAddOns(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Color.Purple, 88));
        subComponents.add(new AlienAddOns(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Color.Purple, 89));
        subComponents.add(new AlienAddOns(Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Color.Purple, 810));
        subComponents.add(new AlienAddOns(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, Color.Purple, 811));
        subComponents.add(new AlienAddOns(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Color.Purple, 812));

        return subComponents;
    }
}
