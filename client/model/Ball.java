package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import standard.Dimensioni;

@SuppressWarnings("serial")
public class Ball extends Rectangle{
	public Random random;
	public int xVelocity;
	public int yVelocity;
	public Dimensioni d;
	
	public Ball(int x, int y, int width, int height) {
		super(x,y,width,height);
		random = new Random();
		d= new Dimensioni();
	}
	
	public void move(int xBall, int yBall) {
		x = xBall;
		y = yBall;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillOval(x, y, height, width);
	}
}