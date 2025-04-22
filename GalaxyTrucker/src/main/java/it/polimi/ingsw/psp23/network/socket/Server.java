package it.polimi.ingsw.psp23.network.socket;

import it.polimi.ingsw.psp23.network.messages.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    private static Server instance;
    private ServerSocket serverSocket;
    private HashMap<String,SocketHandler> clients = null;


    // Qui creo il server
    Server(int port) {
        try {

            serverSocket = new ServerSocket(port);

            clients = new HashMap<>();

            System.out.println("Server started at port " + port + " and with address " + serverSocket.getInetAddress());

        }
        catch (IOException e) {
            throw new RuntimeException("Could not load the server: error in constructor of class Server" + e.getMessage()+")");
        }
    }

    public static Server getInstance(){
        if(instance == null){
            throw new RuntimeException("The server has not been initialized yet(from getInstance in class Server)");
        }
        else{
            return instance;
        }
    }

    public static Server getInstance(int port){
        if(instance == null){
            instance = new Server(port);
        }

        return instance;
    }

    public void connectClients(String nameConnection){
        if(clients.containsKey(nameConnection)){
            throw new RuntimeException("La connessione è già presente!!");
        }
        synchronized (serverSocket){
            try {

                Socket socket = serverSocket.accept();

                socket.setSoTimeout(5000);

                SocketHandler socketHandler = new SocketHandler(socket);

                synchronized (clients) {

                    clients.put(nameConnection, socketHandler);
                    System.out.println("Client connected: " + nameConnection);

                }

            } catch (IOException e) {
                throw new RuntimeException("Could not accept the client in connectClients in Server" + e.getMessage());
            }

        }
    }

    // Questo metodo ci fornirà l'indirizzo ip del server
    public InetAddress getInetAddress() {

        // Sto ritornando un riferimento all'InetAddress, ma tanto gli InetAddress sono immutabili, quindi
        // non corriamo rischi
        return serverSocket.getInetAddress();

    }



    // Metodo incaricato dell'invio dei messaggi ai client. Il parametro indice ci dice quale elemento della lista di
    // socket bisogna considerare
    public void sendMessage(Message message, String nameConnection) {

        SocketHandler socketHandler = null;

        synchronized (clients) {
            socketHandler = clients.get(nameConnection);
        }

        if(socketHandler != null){
            System.out.println("Messaggio inviato con esito: " + socketHandler.sendMessage(message));
        }
        else{
            throw new RuntimeException("No client with name " + nameConnection);
        }
    }

    public Message receiveMessage(String nameConnection) {

        SocketHandler socketHandler = null;

        synchronized (clients) {
            socketHandler = clients.get(nameConnection);
        }

        if(socketHandler != null) {
            return socketHandler.readMessage();
        }
        return null;
    }

    // Metodo per chiudere la connessione con un solo client specifico
    public void closeConnection(String nameConnection) {
        synchronized(clients){
            SocketHandler socketHandler = clients.get(nameConnection);

            if(socketHandler != null){
                socketHandler.close();
                clients.remove(nameConnection);
            }
        }
    }

    public void stampa(){
        if(clients.isEmpty()){
            System.out.println("No client connected");
        }
        else {
            for (HashMap.Entry<String, SocketHandler> entry : clients.entrySet()) {
                String connectionId = entry.getKey();
                SocketHandler handler = entry.getValue();
                Socket socket = handler.socket;  // stessa package; altrimenti aggiungi getSocket()

                System.out.println("ConnectionId: " + connectionId + ", RemoteAddress: " + socket.getRemoteSocketAddress());
                System.out.println("");
            }
        }
    }

    public int getSize(){
        return clients.size();
    }



}
