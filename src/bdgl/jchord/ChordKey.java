package bdgl.jchord;

public class ChordKey implements Comparable<ChordKey> {

	private String identifier;
	private byte[] key;

	public ChordKey(String identifier) {
		this.identifier = identifier;
		this.key = Hash.hash(identifier);
	}

	public ChordKey(byte[] key) {
		this.key = key;
	}

	// Méthode pour vérifier si cette clé est entre fromKey et toKey dans l'anneau Chord
	public boolean isBetween(ChordKey fromKey, ChordKey toKey) {
		if (fromKey.compareTo(toKey) < 0) {
			if (this.compareTo(fromKey) > 0 && this.compareTo(toKey) < 0) {
				return true;
			}
		} else if (fromKey.compareTo(toKey) > 0) {
			if (this.compareTo(toKey) < 0 || this.compareTo(fromKey) > 0) {
				return true;
			}
		}
		return false;
	}

	// Méthode pour créer une nouvelle clé à partir de cette clé en ajoutant un index
	public ChordKey createStartKey(int index) {
		byte[] newKey = new byte[key.length];
		System.arraycopy(key, 0, newKey, 0, key.length);
		int carry = 0;
		for (int i = (Hash.KEY_LENGTH - 1) / 8; i >= 0; i--) {
			int value = key[i] & 0xff;
			value += (1 << (index % 8)) + carry;
			newKey[i] = (byte) value;
			if (value <= 0xff) {
				break;
			}
			carry = (value >> 8) & 0xff;
		}
		return new ChordKey(newKey);
	}

	// Méthode de comparaison pour l'interface Comparable
	@Override
	public int compareTo(ChordKey otherKey) {
		for (int i = 0; i < key.length; i++) {
			int loperand = (this.key[i] & 0xff);
			int roperand = (otherKey.getKey()[i] & 0xff);
			if (loperand != roperand) {
				return (loperand - roperand);
			}
		}
		return 0;
	}

	// Méthode pour obtenir une représentation sous forme de chaîne de la clé
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (key.length > 4) {
			for (int i = 0; i < key.length; i++) {
				sb.append(Integer.toString(((int) key[i]) & 0xff) + ".");
			}
		} else {
			long n = 0;
			for (int i = key.length-1,j=0; i >= 0 ; i--, j++) {
				n |= ((key[i]<<(8*j)) & (0xffL<<(8*j)));
			}
			sb.append(Long.toString(n));
		}
		return sb.substring(0, sb.length() - 1).toString();
	}

	// Getters et Setters pour l'identifiant et la clé
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}
}
