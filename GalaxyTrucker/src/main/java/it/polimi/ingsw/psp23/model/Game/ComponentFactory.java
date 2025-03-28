package it.polimi.ingsw.psp23.model.Game;
import it.polimi.ingsw.psp23.Player;
import it.polimi.ingsw.psp23.model.components.*;
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

    }

    // sottometodi statici perchè anche questi non devono essere chiamati su oggetti. Privati perchè sono metodi di supporto che servono solo alla classe.
    private static ArrayList<Component> generateContainers() {
        ArrayList<Component> subComponents = new ArrayList<>();
        subComponents.add(new Container())

    }
}
