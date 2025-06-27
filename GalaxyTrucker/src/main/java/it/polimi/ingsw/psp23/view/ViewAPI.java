package it.polimi.ingsw.psp23.view;

import it.polimi.ingsw.psp23.model.helpers.CannonShot;
import it.polimi.ingsw.psp23.model.helpers.Meteor;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.Color;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.Client;

import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The ViewAPI interface defines the contract for the client-side view implementation, enabling interaction
 * with the server and communication of game state updates to the client. This interface is implemented
 * by both GUI and TUI classes, ensuring flexibility in the selection of the user interface.
 * It provides methods for initializing the view, handling server responses, updating game state,
 * and interacting with game components.
 */
public interface ViewAPI {
    void setClient(Client client);
    void setup() throws RemoteException;
    void setupRMI(String nameConnection) throws RemoteException;
    void showRequestLevel();
    void showRequestNumPlayers();
    void showAppropriateUsername(String username, int level);
    void showWrongUsername();
    void showTile(Component requested);
    void showShip(Component[][] ship, String owner);
    void showUncovered(ArrayList<Component> uncovered, int lastVersion);
    void showError(String error);
    void showMessage(String message);
    void stateChanged(GameStatus newState);
    void showIllegalTruck();
    void showTimeExpired();
    void endMatch(String message);
    void showCannonShot(int coord, CannonShot cannonShot);
    void showCardUpdate(String message);
    void showDeck(ArrayList<Integer> ids, String description);
    void incorrectTile();
    void showNewCard(int id, String description);
    void showFlightBoard(Map<Color, Integer> flightMap);
    void showAvailableLobbies(List<List<Integer>> availableLobbies);
    void showRanking(List<AbstractMap.SimpleEntry<String,Integer>> ranking);
    void savePlayersNames(List<String> players);
}
