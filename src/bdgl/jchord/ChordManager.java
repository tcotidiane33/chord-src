    package bdgl.jchord;

    import java.io.BufferedReader;
    import java.io.FileReader;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.List;

    public class ChordManager {

        private Chord chord;

        public ChordManager() {
            this.chord = new Chord();
        }

        // Méthode pour créer un nouveau nœud dans l'anneau Chord
        public void createNode(String nodeId) throws ChordException {
            chord.createNode(nodeId);
        }

        // Méthode pour supprimer un nœud spécifié de l'anneau Chord
        public void deleteNode(String nodeId) {
            ChordNode nodeToDelete = findNodeById(nodeId);
            if (nodeToDelete != null) {
                chord.nodeList.remove(nodeToDelete);
                chord.sortedNodeMap.remove(nodeToDelete.getNodeKey());
            }
        }

        // Méthode pour obtenir la liste de tous les nœuds présents dans l'anneau Chord
        public List<ChordNode> listNodes() {
            List<ChordNode> sortedNodes = new ArrayList<>(chord.nodeList);
            Collections.sort(sortedNodes, (node1, node2) -> node1.getNodeId().compareTo(node2.getNodeId()));
            return sortedNodes;
        }


        // Méthode pour lire des données à partir d'un nœud spécifié de l'anneau Chord
        public String readDataFromNode(String nodeId) {
            ChordNode node = findNodeById(nodeId);
            if (node != null) {
                return "Data read from node " + nodeId;
            } else {
                return "Node with ID " + nodeId + " not found.";
            }
        }



        // Méthode pour charger des données sur les nœuds de l'anneau Chord à partir d'un fichier
        public void loadDataOntoNode(String nodeId, String filePath) {
            ChordNode node = findNodeById(nodeId);
            if (node != null) {
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        node.addData(line);
                    }
                    System.out.println("Data loaded onto node " + nodeId + " successfully.");
                } catch (IOException e) {
                    System.err.println("Error loading data onto node " + nodeId + ": " + e.getMessage());
                }
            } else {
                System.err.println("Node with ID " + nodeId + " not found.");
            }
        }

        // Méthode pour afficher les partitions de données sur les nœuds de l'anneau Chord
        public void displayDataPartitions(String nodeId) {
            // Parcourir chaque nœud de l'anneau Chord
            for (ChordNode node : chord.nodeList) {
                System.out.println("Node ID: " + node.getNodeId());
                System.out.println("Data partitions:");
                // Afficher les données stockées dans le nœud
                for (String data : node.getDataList()) {
                    System.out.println("- " + data);
                }
                System.out.println("----------------------------");
            }
        }


        // Méthode pour ajouter des données à un nœud spécifié de l'anneau Chord
        public void addData(String nodeId, String data) {
            ChordNode node = findNodeById(nodeId);
            if (node != null) {
                node.addData(data);
                System.out.println("Data added to node " + nodeId + ": " + data);
            } else {
                System.err.println("Node with ID " + nodeId + " not found. Data not added.");
            }
        }

        // Méthode pour obtenir de l'aide sur les commandes disponibles
        public String getHelp() {
            return "Available commands:\n" +
                    "1. createNode [nodeId]: Create a new node with the given nodeId.\n" +
                    "2. deleteNode [nodeId]: Delete the node with the given nodeId.\n" +
                    "3. listNodes: List all nodes present in the Chord ring.\n" +
                    "4. readDataFromNode [nodeId]: Read data from the specified node.\n" +
                    "5. loadDataOntoNode [nodeId] [data]: Load data onto the specified node.\n" +
                    "6. displayDataPartitions: Display data partitions on the nodes.\n" +
                    "7. help: Get help on available commands.\n" +
                    "8. quit: Quit the program.";
        }

        // Méthode pour quitter le programme
        public void quit() {
            System.exit(0);
        }

        // Méthode utilitaire pour rechercher un nœud par son ID
        private ChordNode findNodeById(String nodeId) {
            for (ChordNode n : chord.nodeList) {
                if (n.getNodeId().equals(nodeId)) {
                    return n;
                }
            }
            return null;
        }
    }
