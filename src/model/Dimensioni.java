package model;

import java.awt.*;

public class Dimensioni {
	public final int GAME_WIDTH = 1000;
	public final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.55555));
	public final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
	public final int BALL_DIAMETER = 20;
	public final int PADDLE_WIDTH = 25;
	public final int PADDLE_HEIGHT = 100;
	public final int speed = 10;
	public final int ballSpeed = 2;
	
	public Dimensioni(){
		
	}
}
