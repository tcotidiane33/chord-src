package bdgl.jchord;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Chord {

	List<ChordNode> nodeList = new ArrayList<>();
	SortedMap<ChordKey, ChordNode> sortedNodeMap = new TreeMap<>();
	private Object[] sortedKeyArray;

	public void createNode(String nodeId) throws ChordException {
		ChordNode node = new ChordNode(nodeId);
		nodeList.add(node);

		if (sortedNodeMap.containsKey(node.getNodeKey())) {
			throw new ChordException("Duplicated Key: " + node);
		}

		sortedNodeMap.put(node.getNodeKey(), node);
	}

	public void deleteNode(String nodeId) {
		ChordNode nodeToRemove = null;
		for (ChordNode node : nodeList) {
			if (node.getNodeId().equals(nodeId)) {
				nodeToRemove = node;
				break;
			}
		}
		if (nodeToRemove != null) {
			nodeList.remove(nodeToRemove);
			sortedNodeMap.remove(nodeToRemove.getNodeKey());
		}
	}

	public List<ChordNode> listNodes() {
		List<ChordNode> sortedNodes = new ArrayList<>(sortedNodeMap.values());
		return sortedNodes;
	}


	public String readDataFromNode(String nodeId) {
		ChordNode node = getNodeById(nodeId);
		if (node != null) {
			// Add logic to read data from the node
			return "Data read from node " + nodeId;
		} else {
			return "Node not found: " + nodeId;
		}
	}

	public void loadDataOntoNode(String nodeId, String data) {
		ChordNode node = getNodeById(nodeId);
		if (node != null) {
			// Add logic to load data onto the node
			System.out.println("Data loaded onto node " + nodeId + ": " + data);
		} else {
			System.out.println("Node not found: " + nodeId);
		}
	}

	public void displayDataPartitions() {
		// Add logic to display data partitions on nodes
		System.out.println("Displaying data partitions:");
		for (ChordNode node : nodeList) {
			System.out.println("Node " + node.getNodeId() + ": Data partition");
		}
	}

	private ChordNode getNodeById(String nodeId) {
		for (ChordNode node : nodeList) {
			if (node.getNodeId().equals(nodeId)) {
				return node;
			}
		}
		return null;
	}

	public ChordNode getSortedNode(int i) {
		if (i < 0 || i >= nodeList.size()) {
			return null;
		}
		return nodeList.get(i);
	}
	public ChordNode getNode(int i) {
		if (i < 0 || i >= nodeList.size()) {
			return null;
		}
		return nodeList.get(i);
	}

	public List<ChordNode> getAllNodes() {
		List<ChordNode> nodeList = new ArrayList<>();
		ChordNode currentNode = this.getNode(0); // Le premier nœud dans l'anneau

		// Parcourir tous les nœuds de l'anneau en partant du premier nœud
		do {
			nodeList.add(currentNode);
			currentNode = currentNode.getSuccessor(); // Passer au nœud suivant
		} while (currentNode != this.getNode(0)); // Arrêter lorsque nous revenons au premier nœud

		return nodeList;
	}
}
