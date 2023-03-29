package standard;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BytesPacchettoCriptato implements Serializable{
	public byte[] b;

	public BytesPacchettoCriptato(byte[] b) {
		this.b = b;
	}

	public byte[] getB() {
		return b;
	}

	public void setB(byte[] b) {
		this.b = b;
	}
}
