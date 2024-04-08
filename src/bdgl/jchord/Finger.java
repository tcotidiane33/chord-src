package bdgl.jchord;

public class Finger {

	private ChordKey start;
	private ChordNode node;

	public Finger(ChordKey start, ChordNode node) {
		this.start = start;
		this.node = node;
	}

	public ChordKey getStart() {
		return start;
	}

	public void setStart(ChordKey start) {
		this.start = start;
	}

	public ChordNode getNode() {
		return node;
	}

	public void setNode(ChordNode node) {
		this.node = node;
	}
}
