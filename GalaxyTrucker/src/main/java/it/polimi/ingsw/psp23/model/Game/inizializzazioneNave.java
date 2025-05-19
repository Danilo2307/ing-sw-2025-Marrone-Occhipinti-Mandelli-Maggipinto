package it.polimi.ingsw.psp23.model.Game;


import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.Side;

public class inizializzazioneNave {

    private static inizializzazioneNave instance;

    public inizializzazioneNave() {
    }

    public static inizializzazioneNave getInstance(){
        if(instance == null){
            instance = new inizializzazioneNave();
        }
        return instance;
    }

    public void popolaNave(Player p) {
        Board nave = p.getTruck();

        Component gun1 = new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, false, -1);
        gun1.moveToHand();
        nave.addComponent(gun1, 1, 3);

        Component hu1 = new HousingUnit(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, false,  -1);
        hu1.moveToHand();
        nave.addComponent(hu1 , 2, 4);


        Component gun2 = new Cannon(Side.GUN, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, true,  -1);
        gun2.moveToHand();
        nave.addComponent(gun2, 1, 4);

        Component sc1 = new StructuralComponent(Side.UNIVERSAL_CONNECTOR,  Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR,  -1);
        sc1.moveToHand();
        nave.addComponent(sc1, 3, 4);

        Component bh1 = new BatteryHub(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, 3,  -1);
        bh1.moveToHand();
        nave.addComponent(bh1, 4, 4);

        Component e1 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.SINGLE_CONNECTOR, Side.EMPTY, false,  -1);
        e1.moveToHand();
        nave.addComponent(e1,4, 5);

        Component e2 = new Engine(Side.SINGLE_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.EMPTY, true,  -1);
        e2.moveToHand();
        nave.addComponent(e2,3, 3);

        Component gun3 = new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, true,  -1);
        gun3.moveToHand();
        nave.addComponent(gun3, 2, 2);

        Component gun4 = new Cannon(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.GUN, Side.DOUBLE_CONNECTOR, false,  -1);
        gun4.moveToHand();
        nave.addComponent(gun4, 2, 1);

        Component aa1 = new AlienAddOns(Side.SINGLE_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Color.Brown,  -1);
        aa1.moveToHand();
        nave.addComponent(aa1, 3, 2);

        Component hu2 = new HousingUnit(Side.DOUBLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, false,  -1);
        hu2.moveToHand();
        nave.addComponent(hu2, 3, 1);

        Component e3 = new Engine(Side.UNIVERSAL_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.DOUBLE_CONNECTOR, false,  -1);
        e3.moveToHand();
        nave.addComponent(e3, 4, 1);

        Component bh = new BatteryHub(Side.EMPTY, Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, 3,  -1);
        bh.moveToHand();
        nave.addComponent(bh , 4, 2);
    }
}
