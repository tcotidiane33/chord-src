package test;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;

import bdgl.jchord.Chord;
import bdgl.jchord.ChordException;
import bdgl.jchord.ChordNode;
import bdgl.jchord.Hash;

public class Main {

	public static final String HASH_FUNCTION = "SHA-1";
	public static final int KEY_LENGTH = 160;
	public static final int STARTING_PORT = 9000;
	public static final int NUM_OF_NODES = 10;

	public static void main(String[] args) throws Exception {
		PrintStream out = new PrintStream("result.log");
		long start = System.currentTimeMillis();

		Hash.setFunction(HASH_FUNCTION);
		Hash.setKeyLength(KEY_LENGTH);

		Chord chord = new Chord();
		for (int i = 0; i < NUM_OF_NODES; i++) {
			URL url = new URL("http", InetAddress.getLocalHost().getHostAddress(), STARTING_PORT + i, "");
			try {
				chord.createNode(url.toString());
			} catch (ChordException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		out.println(NUM_OF_NODES + " nodes are created.");

		establishChordRing(chord);

		fixFingerTables(chord, out);

		List<ChordNode> createdNodes = chord.getAllNodes(); // Récupération de tous les nœuds créés

		out.println("Chord ring is established.");
		out.println("Finger Tables are fixed.");

		long end = System.currentTimeMillis();

		int interval = (int) (end - start);
		System.out.printf("Elapsed Time : %d.%d", interval / 1000, interval % 1000);

		// Lancement du serveur avec les informations des nœuds créés
		Server server = new Server();
		server.setCreatedNodes(createdNodes);
		server.startServer(9090); // Utilisez un autre port disponible
	}

	private static void establishChordRing(Chord chord) {
		for (int i = 1; i < NUM_OF_NODES; i++) {
			ChordNode node = chord.getNode(i);
			node.join(chord.getNode(0));
			ChordNode preceding = node.getSuccessor().getPredecessor();
			node.stabilize();
			if (preceding == null) {
				node.getSuccessor().stabilize();
			} else {
				preceding.stabilize();
			}
		}
	}

	private static void fixFingerTables(Chord chord, PrintStream out) {
		for (int i = 0; i < NUM_OF_NODES; i++) {
			ChordNode node = chord.getNode(i);
			node.fixFingers();
		}
		out.println("Finger Tables are fixed.");

		for (int i = 0; i < NUM_OF_NODES; i++) {
			ChordNode node = chord.getSortedNode(i);
			node.printFingerTable(out);
		}
	}
}
