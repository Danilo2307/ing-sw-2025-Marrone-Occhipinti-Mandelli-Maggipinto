package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.network.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * Questa classe serve per far sì che il server invochi i metodi di sendMessage e readMessage specifici per le istanze
 * di socketHandler che gestiscono le socket specifiche dei vari client(in pratica ci sarà un socketHandler specifico
 * per ogni client). Questa classe sarà usata anche nel client perchè, come dice il nome, permette di gestire le socket
 */

public class SocketHandler {

    Socket socket;

    ObjectOutputStream out;

    ObjectInputStream in;

    // Gli oggetti di lock mi permettono di inviare/ricevere un messaggio alla volta
    final Object lockLettura = new Object();

    final Object lockScrittura = new Object();

    String username = null;

    public SocketHandler(Socket socket){

        this.socket = socket;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Problema nell'istanziazione di 'out' in SocketHandler " + e.getMessage());
        }

        try {
            // Attenzione perchè quest'istruzione mette tutto in pausa fino a quando non gli arriva l'header
            // per la connessione da parte del client tra l'outputstream(facendo "flush") e questo inputstream.
            // A questo scopo serve l'istruzione "socket.setSoTimeout(5000);" presente nel metodo connectClients
            // del server, di modo che, nel caso in cui non dovesse arrivare
            // l'header in tempo non avremo un'attesa infinita, bensì questa terminerà
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Problema nell'istanziazione di 'in' in SocketHandler " + e.getMessage());
        }


        System.out.println("SocketHandler istanziato correttamente");

    }


    public Message readMessage(){
        synchronized (lockLettura) {
            Message received = null;
            try {

                // TODO: casting da togliere!!!
                received = (Message)in.readObject();

                return received;

            } catch (IOException e) {
                throw new RuntimeException("Problema(RuntimeException) in readMessage in SocketHandler " + e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Problema(ClassNotFoundException) in readMessage in SocketHandler " + e.getMessage());
            }
        }

    }


    public boolean sendMessage(Message message) {
        synchronized (lockScrittura){
            try {
                out.reset();
            } catch (IOException e) {
                throw new RuntimeException("Problema(IOException) in reset di 'out' in writeMessage in SocketHandler " + e.getMessage());
            }
            try {
                out.writeObject(message);
            } catch (IOException e) {
                throw new RuntimeException("Problema(IOException) in writeObject di 'out' in writeMessage in SocketHandler " + e.getMessage());
            }
            try {
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException("Problema(IOException) in flush di 'out' in writeMessage in SocketHandler " + e.getMessage());
            }
        }
        return true;
    }

    public void close(){
        try {
            this.in.close();
        }
        // In caso di eccezioni non succede niente
        catch (IOException ignored) {

        }
        /*
        Tramite il finally sono sicuro che, sia che la chiusura fallisca o venga fatta correttamente, il mio oggetto
        sia posto a null
        */
        finally {
            this.in = null;
        }

        try {
            this.out.close();
        } catch (IOException ignored) {

        } finally {
            this.out = null;
        }

        try {
            this.socket.close();
        } catch (IOException ignored) {

        } finally {
            this.socket = null;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
