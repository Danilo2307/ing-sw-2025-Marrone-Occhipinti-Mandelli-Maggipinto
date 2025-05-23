package it.polimi.ingsw.psp23.view;

import it.polimi.ingsw.psp23.model.cards.CannonShot;
import it.polimi.ingsw.psp23.model.cards.Meteor;
import it.polimi.ingsw.psp23.model.components.Component;
import it.polimi.ingsw.psp23.model.enumeration.GameStatus;
import it.polimi.ingsw.psp23.network.Client;
import it.polimi.ingsw.psp23.network.rmi.ClientRMI;
import it.polimi.ingsw.psp23.network.socket.ClientSocket;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ViewAPI {
    void setClient(Client client);
    void setup() throws RemoteException;
    void setupRMI() throws RemoteException;
    void showRequestLevel();
    void showRequestNumPlayers();
    void showAppropriateUsername(String username);
    void showWrongUsername();
    void showTile(Component requested);
    void showShip(Component[][] ship, int[][] validCoordinates);
    void showUncovered(ArrayList<Component> uncovered, int lastVersion);
    void showError(String error);
    void showMessage(String message);
    void stateChanged(GameStatus newState);
    void showTurn(String turn);
    void showStart();
    void showIllegalTruck();
    void showPlayerLanded(String username, int planet);
    void showTimeExpired();
    void showEndTurn(String username);
    void showEnd();
    void endMatch(String message);
    void showCardUpdate(String message);
    void showMeteor(Meteor meteor);
    void showCannonShot(int coord, CannonShot cannonShot);



}
