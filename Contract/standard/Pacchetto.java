package standard;

import java.io.Serializable;

import operazioni.Operazione;

@SuppressWarnings("serial")
public class Pacchetto implements Serializable{
	
	public int yPaddle1;
	public int yPaddle2;
	public int xBall;
	public int yBall;
	public int id;
	public String name;
	public Operazione operazione;

	public Pacchetto(int yPaddle1, int yPaddle2, int xBall, int yBall, int id, Operazione operazione) {
		this.yPaddle1 = yPaddle1;
		this.yPaddle2 = yPaddle2;
		this.xBall = xBall;
		this.yBall = yBall;
		this.id = id;
		this.operazione = operazione;
	}
	
	public Pacchetto(int yPaddle1, int yPaddle2, int xBall, int yBall, int id, Operazione operazione, String name) {
		this.yPaddle1 = yPaddle1;
		this.yPaddle2 = yPaddle2;
		this.xBall = xBall;
		this.yBall = yBall;
		this.id = id;
		this.operazione = operazione;
		this.name=name;
	}
	
	@Override
	public String toString() {
		return "Pacchetto [yPaddle1=" + yPaddle1 + ", yPaddle2=" + yPaddle2 + ", xBall=" + xBall + ", yBall=" + yBall
				+ ", id=" + id + ", operazione=" + operazione + "]";
	}
	
}