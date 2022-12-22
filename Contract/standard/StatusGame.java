package standard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class StatusGame {
	public int numPlayer;
	public int speedBall;
	public int yPaddle1;
	public int yPaddle2;
	public int yBall;
	public int xBall;
	public boolean score1;
	public boolean score2;
	public boolean paddle1InGame;
	public boolean paddle2InGame;
	public int scorePaddle1;
	public int scorePaddle2;
	public int yVelocity;
	public int xVelocity;
	public String name1;
	public String name2;
	public ObjectInputStream ois1;
	public ObjectInputStream ois2;
	public ObjectOutputStream oos1;
	public ObjectOutputStream oos2;
	public Dimensioni d;
	
	public StatusGame(){
		d= new Dimensioni();
		xBall = (d.GAME_WIDTH/2)-(d.BALL_DIAMETER/2);
		yBall = (d.GAME_HEIGHT/2)-(d.BALL_DIAMETER/2);
		yPaddle1 = (d.GAME_HEIGHT/2)-(d.PADDLE_HEIGHT/2);
		yPaddle2 = (d.GAME_HEIGHT/2)-(d.PADDLE_HEIGHT/2);
		this.numPlayer = 0;
		this.scorePaddle1 = 0;
		this.scorePaddle2 = 0;
		this.yVelocity = 0;
		this.xVelocity = 0;
		this.name1 = "";
		this.name2 = "";
		this.paddle1InGame=false;
		this.paddle2InGame=false;
		this.score1 = false;
		this.score2 = false;
	}

	public void stabilisciStream(int id,Socket connection) {
		if(id==1) {
			try {
				ois1 = new ObjectInputStream(connection.getInputStream());
				oos1 = new ObjectOutputStream(connection.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				ois2 = new ObjectInputStream(connection.getInputStream());
				oos2 = new ObjectOutputStream(connection.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String toString() {
		return "StatusGame [numPlayer=" + numPlayer + ", score1=" + score1 + ", score2=" + score2 + ", speedBall="
				+ speedBall + ", yPaddle1=" + yPaddle1 + ", yPaddle2=" + yPaddle2 + ", yBall=" + yBall + ", xBall="
				+ xBall + ", scorePaddle1=" + scorePaddle1 + ", scorePaddle2=" + scorePaddle2 + ", d=" + d + "]";
	}
}
