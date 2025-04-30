package it.polimi.ingsw.psp23;

import it.polimi.ingsw.psp23.network.socket.Client;
import it.polimi.ingsw.psp23.view.TUI.ClientEventHandler;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

import java.util.Scanner;


public class MainClient {
    public static void main(String[] args) {
        try {
            TuiApplication tui = new TuiApplication();
            ClientEventHandler clientEventHandler = new ClientEventHandler(tui);
            System.out.println("Welcome to GALAXY TRUCKER! Inserisci il tuo username: ");
            Scanner scanner = new Scanner(System.in);
            String username = scanner.nextLine();
            Client client = new Client("localhost", 8000, username, clientEventHandler);
            tui.setClient(client);
            tui.runGame();
        } catch (Exception e) {
            System.out.println("Errore di connessione: " + e.getMessage());
        }
    }
}
