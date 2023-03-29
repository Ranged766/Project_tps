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
	
	public Pacchetto() {
		
	}

	public String pacchettoDaCrittografare() {
		return "yPaddle1," + yPaddle1 + ",yPaddle2," + yPaddle2 + ",xBall," + xBall + ",yBall," + yBall + ",id," + id + ","
				+ "operazione," + operazione;
		
	}
	
	public void stingaAPacchetto(String a) {
		String[] ar= a.split(",");
		this.yPaddle1 = Integer.parseInt(ar[1]);
		this.yPaddle2 = Integer.parseInt(ar[3]);;
		this.xBall = Integer.parseInt(ar[5]);;
		this.yBall = Integer.parseInt(ar[7]);;
		this.id = Integer.parseInt(ar[9]);;
		this.operazione = Operazione.valueOf(ar[11]);
	}
	
	@Override
	public String toString() {
		return "Pacchetto [yPaddle1=" + yPaddle1 + ", yPaddle2=" + yPaddle2 + ", xBall=" + xBall + ", yBall=" + yBall
				+ ", id=" + id + ", operazione=" + operazione + "]";
	}
	
}