package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.messages.SelectCannonsMessage;
import it.polimi.ingsw.psp23.network.messages.UpdateStateMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class Client implements Runnable {
    private final int ID;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    // Nel costruttore verrà eseguita la connessione tra client e server
    Client(int ID, String serverIP, int port) {
        this.ID = ID;
        try {
            socket = new Socket(serverIP, port);
            System.out.println("Connected to the server " + serverIP + " at port " + port);

            // Metto il buffer in un ObjectOutputStream per poter trattare meglio gli oggetti serializzati
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            outputStream.flush();

            inputStream = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException("Could not connect to the server: error in constructor of class Client " + e.getMessage()+")");
        }
    }

    public int getID() {
        return ID;
    }


    // ritorna l'indirizzo remoto a cui è connessa la socket del client
    public SocketAddress getSocketAddress() {
        return socket.getRemoteSocketAddress();
    }


    // Metodo incaricato dell'inoltro dei messaggi
    public void sendMessage(Message message) {
        try {
            // Questa funzione, prima di chiamare writeObject, cancella la cache degli oggetti già inviati
            outputStream.reset();

            // Inserisco il messaggio nell'output stream, tuttavia questa scrittura si accumula nel buffer interno,
            // per questo dopo si fa il flush
            outputStream.writeObject(message);

            // Flush per assicurarsi che il buffer venga svuotato(e quindi inviato)
            outputStream.flush();

            System.out.println("E' stato inviato il messaggio: " + message.toString());

        } catch (IOException e) {
            throw new RuntimeException("Could not send the message: error in sendMessage in Client" + e.getMessage()+")");
        }
    }


    // Metodo che gestisce il comportamento da adottare in funzione dei messaggi che gli arrivano
    public void handleMessage() {
        try {
            Object message = inputStream.readObject();

            if(message == null){
                System.out.println("Non è stato inviato niente perchè il messaggio è null");
            }

            else {
                // Questo costrutto deve essere terminato con tutti i tipi di messaggio che potrebbero capitare
                switch (message) {
                    case UpdateStateMessage updateStateMessage -> {

                    }
                    case SelectCannonsMessage selectCannonsMessage -> {

                    }
                    default -> {
                    }
                }
            }
        }
        catch(IOException e){
            throw new RuntimeException("Could not handle the message: error in handleMessage in Client" + e.getMessage()+")");
        }
        catch(ClassNotFoundException e){
            throw new RuntimeException("Could not cast to Object: error in handleMessage in Client" + e.getMessage()+")");
        }
    }


    // TODO: il seguente metodo potrebbe essere migliorato aggiungendo ad esempio un timeout nel caso in cui la
    //       connessione dovesse cadere in maniera inappropriata, causando un mancato aggiornamento del flag isClosed
    @Override
    public void run() {
        while(!socket.isClosed()) {

            // Non metto quest'istruzione in un blocco synchronized perchè il metodo close delle socket che dovrebbe
            // cambiare il flag di isClosed è thread-safe
            handleMessage();
        }
    }


    // Avremmo potuto anche usare i try-with-resources negli altri blocchi, ma fare chiusure esplicite rende più robusto
    // il codice
    public void close() {

        try {
            if (outputStream != null) {
                // Poichè sto chiudendo l'ObjectOutputStream, che è quello che avvolge gli altri tipi di stream di
                // output di modo che, per com'è fatto il metodo close, li chiuda automaticamente
                outputStream.close();
            }
        }
        catch(IOException e){
            throw new RuntimeException("Could not close the output stream in Client" + e.getMessage());
        }

        try{
            if (inputStream != null) {
                // Stesso discorso fatto sopra
                inputStream.close();
            }
        }
        catch(IOException e){
            throw new RuntimeException("Could not close the input stream in Client" + e.getMessage());
        }

        try{
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
        catch(IOException e){
            throw new RuntimeException("Could not close the socket in Client" + e.getMessage());
        }

    }

}
