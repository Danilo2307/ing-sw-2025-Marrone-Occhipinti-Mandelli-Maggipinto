package it.polimi.ingsw.psp23.model.Game;

import it.polimi.ingsw.psp23.exceptions.*;
import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.model.components.*;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.ComponentLocation;
import it.polimi.ingsw.psp23.model.enumeration.Direction;
import it.polimi.ingsw.psp23.model.enumeration.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void testAddComponent() {
        Board truck = new Board(2);
        HousingUnit cabin = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        cabin.moveToHand();
        // la cabina centrale non richiede adiacenza
        truck.addComponent(cabin, 2, 3);

        Cannon cannon = new Cannon(Side.GUN, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, true);

        // Simula il prelievo del componente dalla mano (obbligatorio prima della saldatura)
        cannon.moveToHand();

        // Saldo il componente sulla nave in posizione (1,3)
        truck.addComponent(cannon, 1, 3);

        // Verifico che il componente sia stato aggiunto nella lista dei cannoni
        assertTrue(truck.getCannons().contains(cannon));

        // Recupero il riferimento per i test successivi
        Cannon cTest = truck.getCannons().get(0);

        // Verifico che il riferimento in lista sia lo stesso oggetto passato
        assertEquals(cannon, cTest);

        // Verifico che le coordinate siano state correttamente assegnate
        assertEquals(1, cTest.getX());
        assertEquals(3, cTest.getY());

        // Verifico che lo stato del componente sia ON_TRUCK
        assertEquals(ComponentLocation.ON_TRUCK, cTest.getState());

        // Verifico che nella matrice nave alla posizione (1,3) ci sia effettivamente il componente saldato
        assertEquals(cannon, truck.getTile(1, 3));
    }

    @Test
    public void testInvalidCoordinates() {
        Board truck = new Board(2);

        HousingUnit cabin = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        cabin.moveToHand();
        // la cabina centrale non richiede adiacenza
        truck.addComponent(cabin, 2, 3);

        Container c = new Container(Side.SINGLE_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.DOUBLE_CONNECTOR, 2, Color.Blue, new ArrayList<>() );
        c.moveToHand();
        // check coordinate non valide: devo lanciare eccezione --> isValid funziona
        assertThrows(InvalidCoordinatesException.class, () -> truck.addComponent(c, 0, 1));

        truck.addComponent(c, 2, 2);
        Engine e = new Engine(Side.EMPTY, Side.ENGINE, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, false);
        // check coordinate già occupate: devo lanciare eccezione --> isFree funziona
        assertThrows(InvalidCoordinatesException.class, () -> { truck.addComponent(e, 2, 2); } );

        // check coordinate non adiacenti a pezzi già saldati: lancio eccezione -> hasAdjacentTile funziona
        assertThrows(InvalidCoordinatesException.class, () -> { truck.addComponent(e, 3, 1); } );
    }

    @Test
    public void testLoadGoods() {
        // creo board con cabina centrale
        Board truck = new Board(2);
        HousingUnit cabin = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        cabin.moveToHand();
        truck.addComponent(cabin, 2, 3);

        // aggiungo cannone e container
        Cannon c = new Cannon(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, false);
        c.moveToHand();
        truck.addComponent(c, 1, 3);
        Container container = new Container(Side.SINGLE_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.EMPTY, 3, Color.Blue, new ArrayList<>() );
        container.moveToHand();
        truck.addComponent(container, 2, 4);
        ArrayList<Item> items = new ArrayList<>(List.of( new Item(Color.Red), new Item(Color.Blue), new Item(Color.Green)));

        // provo a caricare le merci sul cannon in 1,3 -> lancio eccezione
//        assertThrows(TypeMismatchException.class, () -> truck.loadGoods(items, 1, 3));

        // provo a caricare items, ma contiene merce rossa e container è blu -> eccezione. Non carica nulla perchè Red è la prima
//        assertThrows(ContainerException.class, () -> truck.loadGoods(items, 2, 4));

        // rimuovo merce rossa
        items.removeFirst();
        // check caricamento effettivo
//        truck.loadGoods(items, 2, 4);
        assertEquals(container.getItems().get(0).getColor(), Color.Blue);
        assertEquals(container.getItems().get(1).getColor(), Color.Green);
        assertEquals(container.getItems().size(), 2);
    }



    @Test
    // non ritesto l'invalidità delle coordinate o TypeMismatch perchè già coperti da altri test e la logica è identica
    public void testReduceBatteries() {
        // setup
        Board truck = new Board(2);
        HousingUnit cabin = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        cabin.moveToHand();
        truck.addComponent(cabin, 2, 3);

        // battery hub con 3 batterie
        BatteryHub b = new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.EMPTY, Side.EMPTY, 3);
        b.moveToHand();
        truck.addComponent(b, 3, 3);

        // rimuovo 2 batterie -> deve rimanerne 1
        truck.reduceBatteries(3,3,2);
        assertEquals(b.getNumBatteries(), 1);

        // provo a rimuovere 2 batterie avendone 1 disponibile -> eccezione
        assertThrows(BatteryOperationException.class, () -> truck.reduceBatteries(3,3,2));
    }

    @Test
    public void testCalculateEngineStrength() {
        // setup
        Board truck = new Board(2);
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // aggiungo vari motori: 2 singoli e 1 doppio (attivandolo)
        Engine e1 = new Engine(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, false);
        e1.moveToHand();
        truck.addComponent(e1, 3, 3);
        Engine e2 = new Engine(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, true);
        e2.moveToHand();
        truck.addComponent(e2, 2, 2);
        e2.activeEngine();
        Engine e3 = new Engine(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, false);
        e3.moveToHand();
        truck.addComponent(e3, 2, 4);

        // controllo calcolo
        assertEquals(4, truck.calculateEngineStrength());

        // ora cannone doppio disattivato: check + ricalcolo
        assertFalse(e2.isActive());
        assertEquals(2, truck.calculateEngineStrength());

        // inserisco alieno marrone
        HousingUnit alienCabin = new HousingUnit(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, false);
        alienCabin.moveToHand();
        truck.addComponent(alienCabin, 2, 5);
        AlienAddOns alienAddOns = new AlienAddOns(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, Color.Brown);
        alienAddOns.moveToHand();
        truck.addComponent(alienAddOns, 2, 6);
        truck.updateAllowedAliens();
        alienCabin.setAlien(Color.Brown);

        // ricalcolo con alieno (motore doppio ancora disattivo)
        assertEquals(4, truck.calculateEngineStrength());

        // provo ad attivare cannone singolo -> eccezione
        assertThrows(InvalidComponentActionException.class, () -> truck.activeEngine(2,4));
    }

    @Test
    public void testCalculateCannonStrength() {
        // setup
        Board truck = new Board(2);
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // creo e saldo vari cannoni
        // singolo puntato in avanti
        Cannon c1 = new Cannon(Side.GUN, Side.EMPTY, Side.EMPTY, Side.EMPTY, false);
        c1.moveToHand();
        truck.addComponent(c1, 1, 3);
        // doppio puntato di lato verso destra
        Cannon c2 = new Cannon(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.GUN, true);
        c2.moveToHand();
        truck.addComponent(c2, 2, 4);
        truck.activeCannon(2,4);
        // singolo verso sinistra
        Cannon c3 = new Cannon(Side.EMPTY, Side.EMPTY, Side.GUN, Side.EMPTY, false);
        c3.moveToHand();
        truck.addComponent(c3, 2, 2);
        // doppio verso il basso
        Cannon c4 = new Cannon(Side.EMPTY, Side.GUN, Side.EMPTY, Side.EMPTY, true);
        c4.moveToHand();
        truck.addComponent(c4, 3, 3);
        truck.activeCannon(3,3);

        // calcolo
        assertEquals(3.5, truck.calculateCannonStrength());

        // check doppi disattivati
        assertFalse(c2.isActive());
        assertFalse(c4.isActive());

        // aggiungo singolo verso il basso
        Cannon e5 = new Cannon(Side.EMPTY, Side.GUN, Side.EMPTY, Side.EMPTY, false);
        e5.moveToHand();
        truck.addComponent(e5, 3, 2);

        // aggiungo doppio che punta verso l'alto e lo attivo
        Cannon e6 = new Cannon(Side.GUN, Side.EMPTY, Side.EMPTY, Side.EMPTY,  true);
        e6.moveToHand();
        truck.addComponent(e6, 1, 2);
        truck.activeCannon(1,2);

        // ricalcolo e check disattivazione
        assertEquals(4, truck.calculateCannonStrength());
        assertFalse(e6.isActive());

        // RICALCOLO CON ALIENO viola
        HousingUnit alienCabin = new HousingUnit(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, false);
        alienCabin.moveToHand();
        truck.addComponent(alienCabin, 3, 4);
        AlienAddOns alienAddOns = new AlienAddOns(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, Color.Purple);
        alienAddOns.moveToHand();
        truck.addComponent(alienAddOns, 4, 4);
        truck.updateAllowedAliens();
        alienCabin.setAlien(Color.Purple);

        assertEquals(4, truck.calculateCannonStrength());

        // attivazione cannone singolo -> eccezione
        assertThrows(InvalidComponentActionException.class, () -> truck.activeCannon(1,3));
    }

    @Test
    public void testCalculateGoodsSales() {
        // setup
        Board truck = new Board(2);
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // creo e saldo due container
        Container c1 = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 3, Color.Red, new ArrayList<>(List.of(new Item(Color.Red), new Item(Color.Green))));
        c1.moveToHand();
        truck.addComponent(c1, 2, 4);
        Container c2 = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.EMPTY, 2, Color.Blue, new ArrayList<>(List.of(new Item(Color.Blue), new Item(Color.Yellow))));
        c2.moveToHand();
        truck.addComponent(c2, 3, 4);

        assertEquals(10, truck.calculateGoodsSales());
    }

    @Test
    public void testCalculateExposedConnectors() {
        // setup
        Board truck = new Board(2);
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // creo e saldo vari componenti
        Cannon c1 = new Cannon(Side.GUN, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, false);
        c1.moveToHand();
        truck.addComponent(c1, 1, 3);

        Container ctn1 = new Container(Side.EMPTY, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR,2,Color.Red, new ArrayList<>());
        ctn1.moveToHand();
        truck.addComponent(ctn1, 1, 2);

        HousingUnit cabin = new HousingUnit(Side.SINGLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, false);
        cabin.moveToHand();
        truck.addComponent(cabin, 2, 2);

        BatteryHub hb = new BatteryHub(Side.SINGLE_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, 2);
        hb.moveToHand();
        truck.addComponent(hb, 2, 4);

        Cannon c2 = new Cannon(Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, false);
        c2.moveToHand();
        truck.addComponent(c2, 3, 3);

        assertEquals(4, truck.calculateExposedConnectors());

        // saldo due componenti per coprire connettori esposti laterali
        Engine e1 = new Engine(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, false);
        e1.moveToHand();
        truck.addComponent(e1, 2, 1);
        Engine e2 = new Engine(Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.EMPTY, false);
        e2.moveToHand();
        truck.addComponent(e2, 2, 5);

        // ricalcolo
        assertEquals(2, truck.calculateExposedConnectors());
    }

    // DI SEGUITO VARI METODI PER TESTARE CHECK() CHE CONTROLLA LEGALITA' NAVE
    @Test
    public void testCheckTileOverGun() {
        Board truck = new Board(2);

        // cabina centrale obbligatoria per una nave valida
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // struttura valida a destra della cabina
        Container ctn = new Container(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, 2, Color.Blue, new ArrayList<>());
        ctn.moveToHand();
        truck.addComponent(ctn, 2, 4);

        // battery hub e cannone sotto container: il cannone ha GUN rivolto verso il container
        BatteryHub bh = new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, 2);
        bh.moveToHand();
        truck.addComponent(bh, 3, 3);
        Cannon cannon = new Cannon(Side.GUN, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.EMPTY, false); // GUN rivolto verso l’alto
        cannon.moveToHand();
        truck.addComponent(cannon, 3, 4);

        // il GUN è rivolto contro un modulo sopra → nave non valida
        assertFalse(truck.check());
    }

    @Test
    public void testCheckTileUnderEngine() {
        Board truck = new Board(2);

        // cabina centrale
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // battery hub per collegare container a destra
        BatteryHub bh = new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, 2);
        bh.moveToHand();
        truck.addComponent(bh, 3, 3);

        // motore rivolto verso il basso (ENGINE nel lato down)
        Engine e = new Engine(Side.EMPTY, Side.ENGINE, Side.UNIVERSAL_CONNECTOR, Side.EMPTY, false);
        e.moveToHand();
        truck.addComponent(e, 2, 4);

        // componente sotto il motore → violazione del regolamento
        Container ctn = new Container(Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, Side.EMPTY, 2, Color.Blue, new ArrayList<>());
        ctn.moveToHand();
        truck.addComponent(ctn, 3, 4);

        // il motore punta verso un modulo → nave non valida
        assertFalse(truck.check());
    }

    @Test
    public void testCheckSingleConnectorVSDouble() {
        Board truck = new Board(2);

        // cabina centrale
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // container con connettore singolo a destra
        Container ctn = new Container(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, 2, Color.Blue, new ArrayList<>());
        ctn.moveToHand();
        truck.addComponent(ctn, 2, 4);

        // cabina con connettore doppio a sinistra -> nave illegale
        HousingUnit cabin = new HousingUnit(Side.EMPTY, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.EMPTY, false);
        cabin.moveToHand();
        truck.addComponent(cabin, 2, 5);

        assertFalse(truck.check());
    }

    @Test
    public void testCheckConnectorVSEmpty() {
        Board truck = new Board(2);

        // cabina centrale
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // container senza connettore a sinistra -> nave illegale
        Container ctn = new Container(Side.EMPTY, Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR, 2, Color.Blue, new ArrayList<>());
        ctn.moveToHand();
        truck.addComponent(ctn, 2, 4);

        assertFalse(truck.check());
    }

    @Test
    public void testCheckEngineIsNotDown() {
        Board truck = new Board(2);

        // cabina centrale
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // motore può essere rivolto solo verso il basso. Qui è verso destra -> nave illegale
        Engine e = new Engine(Side.EMPTY, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, Side.ENGINE, false);
        e.moveToHand();
        truck.addComponent(e, 2, 4);

        assertFalse(truck.check());
    }

    @Test
    public void testCheckGeneral() {
        Board truck = new Board(2);

        // cabina centrale
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // creo nave elaborata ma corretta
        BatteryHub bh = new BatteryHub(Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.SINGLE_CONNECTOR, 2);
        bh.moveToHand();
        truck.addComponent(bh, 2, 4);

        Engine e = new Engine(Side.SINGLE_CONNECTOR, Side.ENGINE, Side.EMPTY, Side.EMPTY, false);
        e.moveToHand();
        truck.addComponent(e, 3, 3);

        HousingUnit cabin = new HousingUnit(Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, Side.EMPTY, false);
        cabin.moveToHand();
        truck.addComponent(cabin, 3, 4);

        Cannon c = new Cannon(Side.GUN, Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, false);
        c.moveToHand();
        truck.addComponent(c, 1, 4);

        Shield shield = new Shield(Side.SHIELD, Side.EMPTY, Side.EMPTY, Side.SHIELD_DOUBLE_CONNECTOR);
        shield.moveToHand();
        truck.addComponent(shield, 2, 2);

        assertTrue(truck.check());

        // aggiungo tile irraggiungibile
        Cannon c2 = new Cannon(Side.EMPTY, Side.EMPTY, Side.GUN, Side.EMPTY, false);
        c2.moveToHand();
        truck.addComponent(c2, 1, 2);
        assertFalse(truck.check());
    }

    // Vari test per delete
    @Test
    public void testDeleteNormal() {
        // setup
        Board truck = new Board(2);
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // saldo cannone
        Cannon c = new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, false);
        c.moveToHand();
        truck.addComponent(c, 1, 3);

        // rimuovo cannone
        truck.delete(1,3);
        assertTrue(truck.getCannons().isEmpty());
        assertNull(truck.getTile(1,3));
    }

    @Test
    public void testDeleteGuns() {
        Board truck = new Board(2);
        HousingUnit h = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        h.moveToHand();
        truck.addComponent(h, 2, 3);

        Cannon c1 = new Cannon(Side.GUN, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, false);
        c1.moveToHand();
        truck.addComponent(c1, 1, 3);
        Cannon c2 = new Cannon(Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, Side.GUN, false);
        c2.moveToHand();
        truck.addComponent(c2, 2, 4);
        Cannon c3 = new Cannon(Side.SINGLE_CONNECTOR, Side.GUN, Side.EMPTY, Side.EMPTY, false);
        c3.moveToHand();
        truck.addComponent(c3, 3, 3);

        truck.delete(2,4);
        assertNull(truck.getTile(2,4));
        assertNotNull(truck.getTile(1,3));
        assertNotNull(truck.getTile(3,3));
        assertEquals(2, truck.getCannons().size());
    }

    @Test
    public void testDeleteUnreachableTiles() {
        // setup
        Board truck = new Board(2);
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // creo e saldo container che eliminerò e a cui saranno legati tutte le altre tiles
        Container c = new Container(Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, 2, Color.Blue, new ArrayList<>());
        c.moveToHand();
        truck.addComponent(c, 2, 2);

        // creo tiles unicamente collegate ad esso
        Cannon cannon = new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, false);
        cannon.moveToHand();
        truck.addComponent(cannon, 1, 2);
        AlienAddOns a = new AlienAddOns(Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, Color.Purple);
        a.moveToHand();
        truck.addComponent(a, 3, 2);
        Engine e = new Engine(Side.EMPTY, Side.ENGINE, Side.EMPTY, Side.SINGLE_CONNECTOR, false);
        e.moveToHand();
        truck.addComponent(e, 3, 1);

        // elimino container -> delete elimina anche cannon, a, e. L'eliminazione è sia dalla nave che dalle liste ovviamente
        truck.delete(2,2);
        assertNull(truck.getTile(2,2));
        assertTrue(truck.getContainers().isEmpty());
        assertNull(truck.getTile(1,2));
        assertTrue(truck.getCannons().isEmpty());
        assertNull(truck.getTile(3,2));
        assertTrue(truck.getAlienAddOns().isEmpty());
        assertNull(truck.getTile(3,1));
        assertTrue(truck.getEngines().isEmpty());

        // verifico però che la cabina centrale sia rimasta
        assertNotNull(truck.getTile(2,3));
        assertFalse(truck.getHousingUnits().isEmpty());
    }

    @Test
    public void testDeleteUnreachableTile() {
        // setup
        Board truck = new Board(2);
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // creo e saldo container che eliminerò e a cui sarà collegato cannon
        Container c = new Container(Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.UNIVERSAL_CONNECTOR, 2, Color.Blue, new ArrayList<>());
        c.moveToHand();
        truck.addComponent(c, 2, 2);

        // creo e saldo altre tiles: ora solo cannon è legata a container
        Cannon cannon = new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, false);
        cannon.moveToHand();
        truck.addComponent(cannon, 1, 2);
        AlienAddOns a = new AlienAddOns(Side.DOUBLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.SINGLE_CONNECTOR, Color.Purple);
        a.moveToHand();
        truck.addComponent(a, 3, 3);
        Engine e = new Engine(Side.EMPTY, Side.ENGINE, Side.SINGLE_CONNECTOR, Side.EMPTY, false);
        e.moveToHand();
        truck.addComponent(e, 3, 4);

        // cancello
        truck.delete(2,2);

        // verifico eliminazione container e cannon
        assertNull(truck.getTile(2,2));
        assertTrue(truck.getContainers().isEmpty());
        assertNull(truck.getTile(1,2));
        assertTrue(truck.getCannons().isEmpty());

        // verifico che 'a' ed 'e' siano ancora presenti
        assertNotNull(truck.getTile(3,3));
        assertFalse(truck.getAlienAddOns().isEmpty());
        assertNotNull(truck.getTile(3,4));
        assertFalse(truck.getEngines().isEmpty());
    }

    @Test
    public void testCheckDeleteTileMultiplePaths() {
        // setup
        Board truck = new Board(2);
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // tile che eliminerò
        Container c = new Container(Side.EMPTY, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, 2, Color.Blue, new ArrayList<>());
        c.moveToHand();
        truck.addComponent(c, 2, 4);

        Shield s = new Shield(Side.SHIELD_DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR);
        s.moveToHand();
        truck.addComponent(s, 3, 3);

        // 'e' è raggiungibile sia da 's' che da 'c'
        Engine e = new Engine(Side.SINGLE_CONNECTOR, Side.ENGINE, Side.SINGLE_CONNECTOR, Side.EMPTY, false);
        e.moveToHand();
        truck.addComponent(e, 3, 4);

        // elimino c
        truck.delete(2,4);

        // verifico che c venga eliminato ma 'e' rimanga in quanto raggiungibile tramite s
        assertNull(truck.getTile(2,4));
        assertTrue(truck.getContainers().isEmpty());
        assertNotNull(truck.getTile(3,4));
        assertFalse(truck.getEngines().isEmpty());
        assertNotNull(truck.getTile(3,3));
    }

    @Test
    public void testDeleteGeneral() {
        // setup
        Board truck = new Board(2);
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // configuro nave
        Cannon c1 = new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, false);
        c1.moveToHand();
        truck.addComponent(c1, 1, 3);
        Cannon c2 = new Cannon(Side.SINGLE_CONNECTOR, Side.GUN, Side.DOUBLE_CONNECTOR, Side.EMPTY, false);
        c2.moveToHand();
        truck.addComponent(c2, 3, 3);
        Shield s = new Shield(Side.SHIELD, Side.EMPTY, Side.SHIELD_SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR);
        s.moveToHand();
        truck.addComponent(s, 2, 2);
        BatteryHub bh = new BatteryHub(Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, 3);
        bh.moveToHand();
        truck.addComponent(bh, 1, 4);


        Container ctn = new Container(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR,2,Color.Red, new ArrayList<>());
        ctn.moveToHand();
        truck.addComponent(ctn, 2, 4);
        Cannon c3 = new Cannon(Side.EMPTY, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.GUN, false);
        c3.moveToHand();
        truck.addComponent(c3, 2, 5);

        truck.delete(1,4);
        assertNull(truck.getTile(1,4));
        assertNotNull(truck.getTile(2,4));
        assertNotNull(truck.getTile(2,5));

        /* truck.delete(2,4);
        assertNull(truck.getTile(2,4));
        assertNull(truck.getTile(1,4));
        assertNull(truck.getTile(2,5));
        assertNotNull(truck.getTile(3,3)); */
    }

    @Test
    public void testCannonShotBig() {
        // setup
        Board truck = new Board(2);
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // configurazione nave test precedente
        Container c = new Container(Side.EMPTY, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, 2, Color.Blue, new ArrayList<>());
        c.moveToHand();
        truck.addComponent(c, 2, 4);
        Shield s = new Shield(Side.SHIELD_DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, Side.SINGLE_CONNECTOR);
        s.moveToHand();
        truck.addComponent(s, 3, 3);
        // 'e' è raggiungibile sia da 's' che da 'c'
        Engine e = new Engine(Side.SINGLE_CONNECTOR, Side.ENGINE, Side.SINGLE_CONNECTOR, Side.EMPTY, false);
        e.moveToHand();
        truck.addComponent(e, 3, 4);

        // attivo scudo per vedere che non succede nulla
        truck.activeShield(3,3);
        // Cannonata pesante da sopra alla colonna 8
        truck.handleCannonShot(new CannonShot(true, Direction.UP), 8);
        // controllo eliminazione di 'c' e che tutto il resto sia rimasto intatto, infatti solo il 1^ modulo colpito viene eliminato (non controllo anche liste perchè già fatto in tutti i precedenti)
        assertNull(truck.getTile(2,4));
        assertNotNull(truck.getTile(3,4));
        assertNotNull(truck.getTile(3,3));
        assertNotNull(truck.getTile(2,3));

        // Cannonata pesante da destra a riga 6: riga vuota -> tutto intatto
        truck.handleCannonShot(new CannonShot(true, Direction.RIGHT), 6);
        assertNotNull(truck.getTile(3,4));
        assertNotNull(truck.getTile(3,3));
        assertNotNull(truck.getTile(2,3));

        // Cannonata pesante da sotto sulla colonna 7 -> elimina 's' e anche 'e' in quanto raggiungibile solo tramite 's'
        truck.handleCannonShot(new CannonShot(true, Direction.DOWN), 7);
        assertNull(truck.getTile(3,3));
        assertNull(truck.getTile(3,4));
        assertNotNull(truck.getTile(2,3));
    }

    @Test
    public void testCannonShotSmall() {
        // setup
        Board truck = new Board(2);
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // configurazione nave test precedente
        Container c = new Container(Side.EMPTY, Side.SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR, Side.EMPTY, 2, Color.Blue, new ArrayList<>());
        c.moveToHand();
        truck.addComponent(c, 2, 4);
        Shield s = new Shield(Side.SHIELD_DOUBLE_CONNECTOR, Side.EMPTY, Side.SHIELD, Side.SINGLE_CONNECTOR);
        s.moveToHand();
        truck.addComponent(s, 3, 3);
        // 'e' è raggiungibile sia da 's' che da 'c'
        Engine e = new Engine(Side.SINGLE_CONNECTOR, Side.ENGINE, Side.SINGLE_CONNECTOR, Side.EMPTY, false);
        e.moveToHand();
        truck.addComponent(e, 3, 4);

        // cannonata piccola da sopra su colonna 8 e da sinistra su riga 7 (verso container c e cabina centrale): tutto intatto grazie a scudo
        truck.activeShield(3,3);
        truck.handleCannonShot(new CannonShot(false, Direction.UP), 8);
        truck.activeShield(3,3);
        truck.handleCannonShot(new CannonShot(false, Direction.LEFT), 7);
        assertNotNull(truck.getTile(2,3));
        assertNotNull(truck.getTile(3,3));
        assertNotNull(truck.getTile(3,4));
        assertNotNull(truck.getTile(2,4));

        // cannonata da sotto su colonna 7 -> rimuove solo scudo (non protegge quella direzione)
        truck.activeShield(3,3);
        truck.handleCannonShot(new CannonShot(false, Direction.DOWN), 7);
        assertNull(truck.getTile(3,3));
        assertNotNull(truck.getTile(3,4));
        assertNotNull(truck.getTile(2,4));

        // cannonata da sopra su colonna 8 -> ora senza scudo, 'c' viene colpito -> 'e' viene rimosso perché isolato
        truck.handleCannonShot(new CannonShot(false, Direction.UP), 8);
        assertNull(truck.getTile(2,4));
        assertNull(truck.getTile(3,4));
        assertNotNull(truck.getTile(2,3));
    }

    @Test
    public void testBigMeteor() {
        // setup
        Board truck = new Board(2);
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // configuro nave
        Cannon c1 = new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, false);
        c1.moveToHand();
        truck.addComponent(c1, 1, 3);
        Cannon c2 = new Cannon(Side.SINGLE_CONNECTOR, Side.GUN, Side.DOUBLE_CONNECTOR, Side.EMPTY, false);
        c2.moveToHand();
        truck.addComponent(c2, 3, 3);
        Shield s = new Shield(Side.SHIELD, Side.EMPTY, Side.SHIELD_SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR);
        s.moveToHand();
        truck.addComponent(s, 2, 2);
        Container ctn = new Container(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR,2,Color.Red, new ArrayList<>());
        ctn.moveToHand();
        truck.addComponent(ctn, 2, 4);
        Cannon c3 = new Cannon(Side.EMPTY, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.GUN, false);
        c3.moveToHand();
        truck.addComponent(c3, 2, 5);
        BatteryHub bh = new BatteryHub(Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, 3);
        bh.moveToHand();
        truck.addComponent(bh, 1, 4);

        assertTrue(truck.check());

        // suppongo di voler usare sempre scudo s per difendermi, così verifico la sua inutilità per big meteors
        // big meteor da sopra su colonna 7 -> c1 la distrugge -> tutto intatto
        truck.activeShield(2,2);
        truck.handleMeteor(new Meteor(true, Direction.UP), 7);
        assertNotNull(truck.getTile(1,3));

        // big meteor da destra su riga 6 -> c3 la distrugge "sparando in diagonale" -> tutto intatto
        truck.activeShield(2,2);
        truck.handleMeteor(new Meteor(true, Direction.RIGHT), 6);
        assertNotNull(truck.getTile(1,4));
        assertNotNull(truck.getTile(2,5));
        assertNotNull(truck.getTile(3,3));

        // big meteor da sopra su colonna 8 -> c1 non protegge -> bh eliminato
        truck.activeShield(2,2);
        truck.handleMeteor(new Meteor(true, Direction.UP), 8);
        assertNull(truck.getTile(1,4));
        assertNotNull(truck.getTile(2,4));

        // big meteor da sotto su colonna 6 -> c2 la distrugge "sparando in diagonale" -> tutto intatto
        truck.activeShield(2,2);
        truck.handleMeteor(new Meteor(true, Direction.DOWN), 6);
        assertNotNull(truck.getTile(2,2));

        // big meteor da sinistra su riga 6 -> nessun cannone -> c1 distrutto
        truck.activeShield(2,2);
        truck.handleMeteor(new Meteor(true, Direction.LEFT), 6);
        assertNull(truck.getTile(1,3));

        // al posto di c1 metto cannone doppio
        Cannon c4 = new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, true);
        c4.moveToHand();
        truck.addComponent(c4, 1, 3);

        truck.activeCannon(1,3);
        truck.activeShield(2,2);
        truck.handleMeteor(new Meteor(true, Direction.UP), 7);
        assertNotNull(truck.getTile(1,3));

        // ora è disattivato -> meteora uguale a prima lo elimina
        truck.activeShield(2,2);
        truck.handleMeteor(new Meteor(true, Direction.UP), 7);
        assertNull(truck.getTile(1,3));
    }

    @Test
    public void testSmallMeteor() {
        // setup
        Board truck = new Board(2);
        HousingUnit central = new HousingUnit(Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, Side.UNIVERSAL_CONNECTOR, true);
        central.moveToHand();
        truck.addComponent(central, 2, 3);

        // configuro nave
        Cannon c1 = new Cannon(Side.GUN, Side.SINGLE_CONNECTOR, Side.EMPTY, Side.EMPTY, false);
        c1.moveToHand();
        truck.addComponent(c1, 1, 3);
        Cannon c2 = new Cannon(Side.SINGLE_CONNECTOR, Side.GUN, Side.DOUBLE_CONNECTOR, Side.EMPTY, false);
        c2.moveToHand();
        truck.addComponent(c2, 3, 3);
        Shield s = new Shield(Side.SHIELD, Side.SHIELD_DOUBLE_CONNECTOR, Side.SHIELD_SINGLE_CONNECTOR, Side.SINGLE_CONNECTOR);
        s.moveToHand();
        truck.addComponent(s, 2, 2);
        Container ctn = new Container(Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.UNIVERSAL_CONNECTOR,2,Color.Red, new ArrayList<>());
        ctn.moveToHand();
        truck.addComponent(ctn, 2, 4);
        Cannon c3 = new Cannon(Side.EMPTY, Side.EMPTY, Side.DOUBLE_CONNECTOR, Side.GUN, false);
        c3.moveToHand();
        truck.addComponent(c3, 2, 5);
        BatteryHub bh = new BatteryHub(Side.SINGLE_CONNECTOR, Side.DOUBLE_CONNECTOR, Side.EMPTY, Side.EMPTY, 3);
        bh.moveToHand();
        truck.addComponent(bh, 1, 4);

        assertTrue(truck.check());

        truck.handleMeteor(new Meteor(false, Direction.RIGHT), 6);
        // bh non ha connettore esposto a dx -> non è necessario scudo -> meteor rimbalza -> intatto
        assertNotNull(truck.getTile(1,4));

        truck.activeShield(2,2);
        truck.handleMeteor(new Meteor(false, Direction.UP), 8);
        // bh sopra ha connettore esposto, ma scudo protege
        assertNotNull(truck.getTile(1,4));

        truck.handleMeteor(new Meteor(false, Direction.UP), 8);
        // ora scudo non protegge -> bh eliminato
        assertNull(truck.getTile(1,4));
        assertNotNull(truck.getTile(2,4));

        truck.activeShield(2,2);
        truck.handleMeteor(new Meteor(false, Direction.LEFT), 8);
        // c2 ha connettore esposto a sx ma scudo protegge
        assertNotNull(truck.getTile(3,3));

        truck.handleMeteor(new Meteor(false, Direction.LEFT), 8);
        // scelgo di non attivare scudo -> c2 eliminato
        assertNull(truck.getTile(3,3));

        truck.handleMeteor(new Meteor(false, Direction.UP), 7);
        // lato GUN non è connettore esposto -> meteor rimbalza
        assertNotNull(truck.getTile(1,3));

        truck.activeShield(2,2);
        truck.handleMeteor(new Meteor(false, Direction.DOWN), 6);
        // uso scudo: mi protegge nonostante connettore esposto
        assertNotNull(truck.getTile(2,2));

        truck.handleMeteor(new Meteor(false, Direction.DOWN), 6);
        // non uso scudo e ha connettore esposto -> eliminato
        assertNull(truck.getTile(2,2));
    }


}
