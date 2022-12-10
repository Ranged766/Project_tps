package view;

import javax.swing.JPanel;

import control.Controls;
import model.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable{
	
	public Dimensioni d;
	public Controls c;
	public Thread gameThread;
	public Image image;
	public Graphics graphics;
	public Random random;
	public Paddle paddle1;
	public Paddle paddle2;
	public Ball ball;
	public Score score;
	
	public GamePanel() {
		d= new Dimensioni();
		newPaddle();
		newBall();
		c= new Controls(paddle1,paddle2);
		
		score = new Score();
		this.setFocusable(true);
		this.addKeyListener(c);
		this.setPreferredSize(d.SCREEN_SIZE);
		
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void newBall() {
		ball= new Ball((d.GAME_WIDTH/2)-(d.BALL_DIAMETER/2),(d.GAME_HEIGHT/2)-(d.BALL_DIAMETER/2),d.BALL_DIAMETER,d.BALL_DIAMETER);
	}
	
	public void newPaddle() {
		paddle1 = new Paddle(0,(d.GAME_HEIGHT/2)-(d.PADDLE_HEIGHT/2),d.PADDLE_WIDTH,d.PADDLE_HEIGHT,1);
		paddle2 = new Paddle(d.GAME_WIDTH-d.PADDLE_WIDTH,(d.GAME_HEIGHT/2)-(d.PADDLE_HEIGHT/2),d.PADDLE_WIDTH,d.PADDLE_HEIGHT,2);
	}
	
	public void paint(Graphics g) {
		image = createImage(getWidth(),getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image, 0, 0, this);
	}
	
	public void draw(Graphics g) {
		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g);
		score.draw(g);
	}
	
	public void move() {
		paddle1.move();
		paddle2.move();
		ball.move();
	}
	
	public void checkCollision() {
		//controlli che la palla non esca dalla schermata
		if(ball.y <= 0) {
			ball.setYDirection(-ball.yVelocity);
		}
		if(ball.y >= d.GAME_HEIGHT-d.BALL_DIAMETER) {
			ball.setYDirection(-ball.yVelocity);
		}
		//controlla la collisione tra un paddle e la palla
		if(ball.intersects(paddle1)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			
			ball.xVelocity++;
			if(ball.yVelocity>0) {
				ball.yVelocity++;
			}
			else {
				ball.yVelocity--;
			}
			
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		if(ball.intersects(paddle2)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			
			ball.xVelocity++;
			if(ball.yVelocity>0) {
				ball.yVelocity++;
			}
			else {
				ball.yVelocity--;
			}
			
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		
		//controlli che i paddle non escano dalla schermata
		if(paddle1.y <= 0) {
			paddle1.y = 0;
		}
		if(paddle1.y >= (d.GAME_HEIGHT-d.PADDLE_HEIGHT)) {
			paddle1.y = d.GAME_HEIGHT-d.PADDLE_HEIGHT;
		}
		
		if(paddle2.y <= 0) {
			paddle2.y = 0;
		}
		if(paddle2.y >= (d.GAME_HEIGHT-d.PADDLE_HEIGHT)) {
			paddle2.y = d.GAME_HEIGHT-d.PADDLE_HEIGHT;
		}
		
		//modifica punti e nuova palla
		if(ball.x <= 0) {
			score.scorePlayer2++;
			newPaddle();
			newBall();
			c= new Controls(paddle1,paddle2);
			this.addKeyListener(c);
		}
		if(ball.x >= (d.GAME_WIDTH-d.BALL_DIAMETER)) {
			score.scorePlayer1++;
			newPaddle();
			newBall();
			c= new Controls(paddle1,paddle2);
			this.addKeyListener(c);
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now-lastTime)/ns;
			lastTime = now;
			if(delta>=1) {
				move();
				checkCollision();
				repaint();
				delta--;
			}
		}
	}
}
