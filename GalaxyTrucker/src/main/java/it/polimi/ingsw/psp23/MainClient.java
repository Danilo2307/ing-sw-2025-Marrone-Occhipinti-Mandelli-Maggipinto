package it.polimi.ingsw.psp23;

import it.polimi.ingsw.psp23.network.messages.GetEventVisitor;
import it.polimi.ingsw.psp23.network.messages.LevelSelectionMessage;
import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.socket.Client;
import it.polimi.ingsw.psp23.protocol.response.HandleEventVisitor;
import it.polimi.ingsw.psp23.view.TUI.ClientEventHandler;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;

import javax.smartcardio.CardException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainClient {
    public static void main(String[] args) {
        try {
            TuiApplication tui = new TuiApplication();
            ClientEventHandler clientEventHandler = new ClientEventHandler(tui);
            Client client = new Client("localhost", 8000, null, clientEventHandler);

            Socket socket = client.getSocket();

            Scanner scanner = new Scanner(System.in);;

            try {

                socket.setSoTimeout(1000);
                Message messaggio = client.readMessage();

                messaggio.call(new GetEventVisitor()).call(new HandleEventVisitor(), tui);

                int livello = scanner.nextInt();
                scanner.nextLine();

                messaggio = new LevelSelectionMessage(livello);
                client.sendMessage(messaggio);

                client.avvia();
                socket.setSoTimeout(0);

            } catch (SocketTimeoutException ste) {
                socket.setSoTimeout(0);
                client.avvia();
            }

            System.out.println("Welcome to GALAXY TRUCKER! Inserisci il tuo username: ");
            String username = scanner.nextLine();
            client.setUsername(username);
            tui.setClient(client);
            tui.runGame();
        } catch (Exception e) {
            System.out.println("ERRORE in mainClient: " + e.getMessage());
        }
    }
}