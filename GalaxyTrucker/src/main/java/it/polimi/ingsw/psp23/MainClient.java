package it.polimi.ingsw.psp23;

import it.polimi.ingsw.psp23.model.Game.Game;
import it.polimi.ingsw.psp23.network.socket.Client;
import it.polimi.ingsw.psp23.view.TUI.ClientEventHandler;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

import javax.smartcardio.CardException;
import java.util.Scanner;


public class MainClient {
    public static void main(String[] args) {
        try {
            TuiApplication tui = new TuiApplication();
            ClientEventHandler clientEventHandler = new ClientEventHandler(tui);
            Client client = new Client("localhost", 8000, null, clientEventHandler);

            if (Game.getInstance() == null) {
                System.out.println("Welcome to GALAXY TRUCKER! Inserisci il tuo username e il livello (0 o 2): ");
                Scanner scanner = new Scanner(System.in);
                String[] words = scanner.nextLine().split(" ");
                String username = words[0];
                int level = Integer.parseInt(words[1]);
                client.setInitGame(username, level);
            }
            else {
                System.out.println("Welcome to GALAXY TRUCKER! Inserisci il tuo username: ");
                Scanner scanner = new Scanner(System.in);
                String username = scanner.nextLine();
                client.setUsername(username);
            }
            tui.setClient(client);
            tui.runGame();
        } catch (Exception e) {
            System.out.println("ERRORE in mainClient: " + e.getMessage());
        }
    }
}
