package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import standard.Dimensioni;

@SuppressWarnings("serial")
public class Paddle extends Rectangle{

	public int id;
	public int yVelocity;
	public Dimensioni d;
	
	public Paddle(int x, int y, int paddleWidth, int paddleHeigth, int id) {
		super(x,y,paddleWidth,paddleHeigth);
		this.id= id;
		d = new Dimensioni();
	}
	
	public void KeyPressed(KeyEvent e) {		
		switch (id) {
		case 1:
			if(e.getKeyCode()==KeyEvent.VK_W) {
				setYDirection(-d.speed);
				move();
			}
			if(e.getKeyCode()==KeyEvent.VK_S) {
				setYDirection(d.speed);
				move();
			}
		break;
		case 2:
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYDirection(-d.speed);
				move();
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYDirection(d.speed);
				move();
			}
		break;
		}
	}
	
	public void KeyReleased(KeyEvent e) {
		switch (id) {
		case 1:
			if(e.getKeyCode()==KeyEvent.VK_W) {
				setYDirection(0);
				move();
			}
			if(e.getKeyCode()==KeyEvent.VK_S) {
				setYDirection(0);
				move();
			}
		break;
		case 2:
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYDirection(0);
				move();
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYDirection(0);
				move();
			}
		break;
		}	
	}
	
	public void setYDirection(int yDirection) {
		yVelocity = yDirection;
	}
	public void move() {
		y = y + yVelocity;
	}
	public void moveToCoordinate(int yPaddle) {
		y = yPaddle;
	}
	public void draw(Graphics g) {
		if(id==1) {
			g.setColor(Color.blue);
		}
		else {
			g.setColor(Color.red);
		}
		g.fillRect(x, y, width, height);
	}
}