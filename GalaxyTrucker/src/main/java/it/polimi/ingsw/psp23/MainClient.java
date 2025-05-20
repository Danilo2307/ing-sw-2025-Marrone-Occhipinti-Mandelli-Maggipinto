package it.polimi.ingsw.psp23;

import it.polimi.ingsw.psp23.network.rmi.ClientRMI;
import it.polimi.ingsw.psp23.network.socket.ClientSocket;
import it.polimi.ingsw.psp23.view.ClientEventHandler;
import it.polimi.ingsw.psp23.view.TUI.TuiApplication;
import it.polimi.ingsw.psp23.view.ViewAPI;
import it.polimi.ingsw.psp23.view.gui.GuiApplication;

import java.util.Scanner;


public class MainClient {
    public static void main(String[] args) {
        try {
            System.out.println("Vuoi usare un'interfaccia testuale o grafica?");
            System.out.println("Digita 1 per TUI, 2 per GUI:");
            Scanner scanner = new Scanner(System.in);
            int interfaceChosen = scanner.nextInt();
            scanner.nextLine();  // Consuma il newline

            System.out.println("Vuoi usare il protocollo di connessione RMI o Socket?");
            System.out.println("Digita 1 per RMI, 2 per Socket:");
            scanner = new Scanner(System.in);
            int protocol = scanner.nextInt();
            scanner.nextLine();  // Consuma il newline


            ViewAPI view;  // Uso l'interfaccia per garantire flessibilità
            ClientEventHandler clientEventHandler;
            ClientSocket client;

            // Scelta dell'interfaccia
            if (interfaceChosen == 1) {
                view = new TuiApplication();  // Assegno direttamente alla variabile generica
            } else if (interfaceChosen == 2) {
                view = new GuiApplication();  // Assegno direttamente alla variabile generica
            } else {
                System.out.println("Scelta non valida. Riprova.");
                return;
            }

            // Usa la variabile generica view per l'handler e il client
            clientEventHandler = new ClientEventHandler(view);

            // Scelta del protocollo
            if (protocol == 1) {
                ClientRMI clientRmi = new ClientRMI("localhost", 1099, null, clientEventHandler);
            } else if (protocol == 2) {
                client = new ClientSocket("localhost", 8000, null, clientEventHandler);
                view.setClient(client);
                view.init();  // Avvio della view scelta
            } else {
                System.out.println("Scelta non valida. Riprova.");
                return;
            }


        } catch (Exception e) {
            System.out.println("ERRORE in mainClient: " + e.getMessage());
        }
    }
}