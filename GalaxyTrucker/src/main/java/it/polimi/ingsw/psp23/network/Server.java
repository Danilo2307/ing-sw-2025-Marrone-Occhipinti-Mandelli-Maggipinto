package it.polimi.ingsw.psp23.network;

import it.polimi.ingsw.psp23.network.messages.Message;
import it.polimi.ingsw.psp23.network.messages.SelectCannonsMessage;
import it.polimi.ingsw.psp23.network.messages.UpdateStateMessage;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {

    private ServerSocket serverSocket;

    // Il booleano isWaitingForClients verrà messo a false ad esempio quando si sono connessi solo 3 player ma la
    // partita è cominciata; non si può quindi aggiungere un nuovo giocatore e quindi a partita avviata si setta
    // questo valore a false
    private boolean isWaitingForClients = true;

    // Definisco la lista che salverà l'insieme di socket dei client
    private List<Socket> clients;

    // Definisco la lista di stream di ObjectInput dai client
    private List<ObjectInputStream> objectInputs;

    // Definisco la lista di stream di ObjectOutput verso i client
    private List<ObjectOutputStream> objectOutputs;

    // Definisco la lista di buffer di input dai client
    private List<BufferedInputStream> bufferedInputStreams;

    // Definisco la lista di buffer di output verso i client
    private List<BufferedOutputStream> bufferedOutputStreams;

    // Oggetto che mi servirà per il synchronized presente in setIsWaitingForClients
    private final Object lock = new Object();


    // Qui creo il server
    Server(int port) {
        try {

            serverSocket = new ServerSocket(port);
            clients = new ArrayList<Socket>();
            objectInputs = new ArrayList<ObjectInputStream>();
            objectOutputs = new ArrayList<ObjectOutputStream>();
            bufferedInputStreams = new ArrayList<BufferedInputStream>();
            bufferedOutputStreams = new ArrayList<BufferedOutputStream>();
            System.out.println("Server started at port " + port + " and with address " + serverSocket.getInetAddress());

        }
        catch (IOException e) {
            throw new RuntimeException("Could not load the server: error in constructor of class Server" + e.getMessage()+")");
        }
    }


    // Questo metodo ci fornirà l'indirizzo ip del server
    public InetAddress getInetAddress() {

        // Sto ritornando un riferimento all'InetAddress, ma tanto gli InetAddress sono immutabili, quindi
        // non corriamo rischi
        return serverSocket.getInetAddress();
    }


    public boolean getIsWaitingForClients() {
        synchronized (lock) {
            return isWaitingForClients;
        }
    }


    public void setIsWaitingForClients(boolean isWaitingForClients) {
        synchronized (lock) {
            this.isWaitingForClients = isWaitingForClients;
        }
    }


    @Override
    public void run() {
        while(clients.size() < 4 && getIsWaitingForClients()) {
            try {
                synchronized (lock) {

                    // Variabile ausiliaria
                    BufferedOutputStream tempOutput;
                    BufferedInputStream tempInput;
                    // Questa socket mi serve per popolare correttamente le liste di client e di stream
                    Socket tempSocket;

                    tempSocket = serverSocket.accept();
                    clients.add(tempSocket);
                    tempOutput = new BufferedOutputStream(tempSocket.getOutputStream());
                    bufferedOutputStreams.add(tempOutput);
                    objectOutputs.add(new ObjectOutputStream(tempOutput));
                    tempInput = new BufferedInputStream(tempSocket.getInputStream());
                    bufferedInputStreams.add(tempInput);
                    objectInputs.add(new ObjectInputStream(tempInput));

                }
            }
            catch (IOException e) {
                throw new RuntimeException("Could not connect the client to the server: error in run() of class Server" + e.getMessage()+")");
            }
        }
    }

    // Metodo incaricato dell'invio dei messaggi ai client. Il parametro indice ci dice quale elemento della lista di
    // socket bisogna considerare
    public void sendMessage(Message message, int indice) {
        try {
            // Questa funzione, prima di chiamare writeObject, cancella la cache degli oggetti già inviati
            objectOutputs.get(indice).reset();

            // Inserisco il messaggio nell'output stream, tuttavia questa scrittura si accumula nel buffer interno,
            // per questo dopo si fa il flush
            objectOutputs.get(indice).writeObject(message);

            // Flush per assicurarsi che il buffer venga svuotato(e quindi inviato)
            objectOutputs.get(indice).flush();

            System.out.println("E' stato inviato il messaggio: " + message.toString());

        } catch (IOException e) {
            throw new RuntimeException("Could not send the message: error in sendMessage in Server" + e.getMessage()+")");
        }
    }

    public void handleMessage(int indice) {
        try {

            Object message = objectInputs.get(indice).readObject();

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

    // Metodo per chiudere la connessione con un solo client specifico
    public void close(int indice) {
        try {
            if (objectOutputs.get(indice) != null) {
                // Poichè sto chiudendo l'ObjectOutputStream, che è quello che avvolge gli altri tipi di stream di
                // output di modo che, per com'è fatto il metodo close, li chiuda automaticamente
                objectOutputs.get(indice).close();
            }
        }
        catch(IOException e){
            throw new RuntimeException("Could not close the output stream in Client" + e.getMessage());
        }

        try{
            if (objectInputs.get(indice) != null) {
                // Stesso discorso fatto sopra
                objectInputs.get(indice).close();
            }
        }
        catch(IOException e){
            throw new RuntimeException("Could not close the input stream in Client" + e.getMessage());
        }

        try{
            if (clients.get(indice) != null && !clients.get(indice).isClosed()) {
                clients.get(indice).close();
            }
        }
        catch(IOException e){
            throw new RuntimeException("Could not close the socket in Client" + e.getMessage());
        }
    }

    // Metodo per chiudere tutti gli stream e tutte le socket
    public void close() {
        try {
            for(ObjectOutputStream outputStream : objectOutputs) {
                if (outputStream != null) {
                    // Poichè sto chiudendo l'ObjectOutputStream, che è quello che avvolge gli altri tipi di stream di
                    // output di modo che, per com'è fatto il metodo close, li chiuda automaticamente
                    outputStream.close();
                }
            }
        }
        catch(IOException e){
            throw new RuntimeException("Could not close the output stream in Server" + e.getMessage());
        }

        try{
            for(ObjectInputStream inputStream : objectInputs) {
                if (inputStream != null) {
                    // Stesso discorso fatto sopra
                    inputStream.close();
                }
            }
        }
        catch(IOException e){
            throw new RuntimeException("Could not close the input stream in Server" + e.getMessage());
        }

        try{
            for(Socket socket : clients) {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }
        }
        catch(IOException e){
            throw new RuntimeException("Could not close the socket in Server" + e.getMessage());
        }
    }
}
