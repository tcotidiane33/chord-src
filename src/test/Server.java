package test;

import bdgl.jchord.ChordException;
import bdgl.jchord.ChordManager;
import bdgl.jchord.ChordNode;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private ChordManager chordManager;
    private List<ChordNode> createdNodes;

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
        } finally {
            stopServer(); // Assurez-vous que le serveur s'arrête même en cas d'exception
        }
    }

    // Dans la classe Server, modifiez la méthode stopServer pour éviter de fermer le socket inutilement
    public void stopServer() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Server stopped.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCreatedNodes(List<ChordNode> createdNodes) {
        this.createdNodes = createdNodes;
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                // Tant que la connexion n'est pas fermée par le client

                while (!clientSocket.isClosed()) {
                    try {
                        String request = (String) in.readObject();
                        if (request == null || "quit".equals(request)) {
                            break; // Sort de la boucle si la requête est nulle ou si elle est égale à "quit"
                        }
                        String response = handleRequest(request);
                        out.writeObject(response);
                    } catch (EOFException e) {
                        System.out.println("Connection closed by client.");
                        break; // Sort de la boucle si la connexion est fermée par le client
                    }
                }



                System.out.println("Connection closed by client.");
            } catch (IOException | ClassNotFoundException | ChordException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String handleRequest(String request) throws ChordException {
        String response;
        String[] parts = request.split(" ");
        String command = parts[0];

        switch (command) {
            case "list_nodes":
                response = getListNodesResponse();
                break;
            case "delete_node":
                if (parts.length < 2) {
                    response = "Missing node ID.";
                } else {
                    String nodeId = parts[1];
                    response = deleteNodeResponse(nodeId);
                }
                break;
            case "load_data":
                if (parts.length < 2) {
                    response = "Missing node ID.";
                } else {
                    String nodeId = parts[1];
                    response = loadDataResponse(nodeId);
                }
                break;
            case "display_partition":
                if (parts.length < 2) {
                    response = "Missing node ID.";
                } else {
                    String nodeId = parts[1];
                    response = displayPartitionResponse(nodeId);
                }
                break;
            case "help":
                response = getHelpResponse();
                break;
            case "quit":
                response = "Program terminated.";
                stopServer();
                break;
            default:
                response = "Unknown request. Type 'help' to see available commands.";
                break;
        }

        return response;
    }

    private String getListNodesResponse() {
        StringBuilder nodeListStr = new StringBuilder("Nodes in the Chord ring:\n");
        for (ChordNode node : createdNodes) {
            nodeListStr.append(node.getNodeId()).append("\n");
        }
        return nodeListStr.toString();
    }

    private String deleteNodeResponse(String nodeId) {
        chordManager.deleteNode(nodeId);
        return "Node deleted successfully.";
    }

    private String loadDataResponse(String nodeId) {
        String filePath = "data.txt"; // Spécifiez le chemin du fichier de données à charger
        chordManager.loadDataOntoNode(nodeId, filePath);
        return "Data loaded onto node successfully.";
    }

    private String displayPartitionResponse(String nodeId) {
        chordManager.displayDataPartitions(nodeId);
        return "Data partitions displayed.";
    }

    private String getHelpResponse() {
        // Retourner un message d'aide général sur l'utilisation des commandes.
        return "Available commands:\n" +
                "list_nodes\n" +
                "delete_node <node_id>\n" +
                "load_data <node_id>\n" +
                "display_partition <node_id>\n" +
                "help\n" +
                "quit";
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer(9091); // Utilisez un autre port disponible
    }
}
