package test;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void connect() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public String sendRequest(String request) {
        try {
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

    public void close() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 9090); // Adresse et port du serveur

        try {
            client.connect();

            // Boucle pour envoyer des demandes tant que l'utilisateur ne quitte pas
            while (true) {
                // Envoyer une demande de l'utilisateur
                String request = getUserInput();
                if ("quit".equalsIgnoreCase(request)) {
                    break; // Quitte la boucle si l'utilisateur demande de quitter
                }
                String response = client.sendRequest(request);
                System.out.println("Response from server: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.close(); // Assurez-vous de fermer la connexion une fois que vous avez fini d'utiliser le client
        }
    }

    private static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your request: ");
        return scanner.nextLine().trim(); // Lire la ligne saisie par l'utilisateur
    }
}




//public class Main {
//
//    public static final String HASH_FUNCTION = "SHA-1";
//
//    public static final int KEY_LENGTH = 160;
//
//    public static final int NUM_OF_NODES = 1000;
//
//    public static void main(String[] args) throws Exception {
//
//        PrintStream out = System.out;
//
//        out = new PrintStream("result.log");
//
//        long start = System.currentTimeMillis();
//
//        String host = InetAddress.getLocalHost().getHostAddress();
//        int port = 9000;
//
//        Hash.setFunction(HASH_FUNCTION);
//        Hash.setKeyLength(KEY_LENGTH);
//
//        Chord chord = new Chord();
//        for (int i = 0; i < NUM_OF_NODES; i++) {
//            URL url = new URL("http", host, port + i, "");
//            try {
//                chord.createNode(url.toString());
//            } catch (ChordException e) {
//                e.printStackTrace();
//                System.exit(0);
//            }
//        }
//        out.println(NUM_OF_NODES + " nodes are created.");
//
//        for (int i = 0; i < NUM_OF_NODES; i++) {
//            ChordNode node = chord.getSortedNode(i);
//            out.println(node);
//        }
//
//        for (int i = 1; i < NUM_OF_NODES; i++) {
//            ChordNode node = chord.getNode(i);
//            node.join(chord.getNode(0));
//            ChordNode preceding = node.getSuccessor().getPredecessor();
//            node.stabilize();
//            if (preceding == null) {
//                node.getSuccessor().stabilize();
//            } else {
//                preceding.stabilize();
//            }
//        }
//        out.println("Chord ring is established.");
//
//        for (int i = 0; i < NUM_OF_NODES; i++) {
//            ChordNode node = chord.getNode(i);
//            node.fixFingers();
//        }
//        out.println("Finger Tables are fixed.");
//
//        for (int i = 0; i < NUM_OF_NODES; i++) {
//            ChordNode node = chord.getSortedNode(i);
//            node.printFingerTable(out);
//        }
//
//        long end = System.currentTimeMillis();
//
//        int interval = (int) (end - start);
//        System.out.printf("Elapsed Time : %d.%d", interval / 1000,
//                interval % 1000);
//    }
//}
