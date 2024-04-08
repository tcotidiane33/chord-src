package run;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private String serverAddress;
    private int serverPort;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public String sendRequest(String request) {
        try (Socket socket = new Socket(serverAddress, serverPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Envoyer la requête au serveur
            out.writeObject(request);
            out.flush();

            // Lire la réponse du serveur
            String response = (String) in.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "Error processing request";
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 8081); // Adresse et port du serveur
        String response = client.sendRequest("LIST_NODES Node1"); // Exemple de commande avec l'ID de nœud
        System.out.println("Response from server: " + response);
    }
}
