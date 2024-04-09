package peer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Peer {
    private String nodeName;
    private int port;

    public Peer(String nodeName, int port) {
        this.nodeName = nodeName;
        this.port = port;
    }

    public void sendMessage(String message, String recipientAddress, int recipientPort) {
        try (Socket socket = new Socket(recipientAddress, recipientPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage(int listenPort) {
        try (ServerSocket serverSocket = new ServerSocket(listenPort)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                    String message = (String) in.readObject();
                    System.out.println("Received message: " + message);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Example usage:
        Peer peer = new Peer("Node1", 9000);
        peer.receiveMessage(9000);
        // To send a message, use peer.sendMessage("Your message", "recipientAddress", recipientPort);
    }
}
