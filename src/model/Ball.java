package model;

import java.awt.*;
import java.util.*;

public class Ball extends Rectangle{
	public Random random;
	public int xVelocity;
	public int yVelocity;
	public Dimensioni d;
	
	public Ball(int x, int y, int width, int height) {
		super(x,y,width,height);
		random = new Random();
		d= new Dimensioni();
		int randomXDirection = random.nextInt(2);
		if(randomXDirection == 0) {
			randomXDirection--;
		}
		setXDirection(randomXDirection*d.ballSpeed);
		
		int randomYDirection = random.nextInt(2);
		if(randomYDirection == 0) {
			randomYDirection--;
		}
		setYDirection(randomYDirection*d.ballSpeed);
	}
	
	public void setXDirection(int randomXDirection) {
		xVelocity = randomXDirection;
	}
	
	public void setYDirection(int randomYDirection) {
		yVelocity = randomYDirection;
	}
	
	public void move() {
		x += xVelocity;
		y += yVelocity;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillOval(x, y, height, width);
	}
}
