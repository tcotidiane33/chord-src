package run;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        try {
            serverSocket.close();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
            ) {
                while (true) {
                    // Lire la requête du client
                    try {
                        String request = (String) in.readObject();
                        String response = handleRequest(request);
                        out.writeObject(response);
                    } catch (EOFException e) {
                        System.out.println("Connection closed by client.");
                        break;
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String handleRequest(String request) {
            String[] parts = request.split(" ");
            if (parts.length < 2) {
                return "Invalid request. Missing node ID.";
            }

            String command = parts[0];
            String nodeId = parts[1];

            switch (command) {
                case "LIST_NODES":
                    // Traiter la commande LIST_NODES avec l'ID du nœud
                    return "List of nodes for node ID: " + nodeId;
                // Ajoutez d'autres cas pour les autres commandes
                default:
                    return "Unknown request. Type 'HELP' to see available commands.";
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer(8081); // Port à utiliser pour le serveur
    }
}
