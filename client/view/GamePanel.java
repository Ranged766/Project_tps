package view;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.JPanel;

import control.Controls;
import model.Ball;
import model.Paddle;
import model.Score;
import standard.Dimensioni;
import standard.Pacchetto;

@SuppressWarnings("serial")
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
	public int id;
	public boolean game;
	public Pacchetto coordinate;

	public GamePanel(int id, Pacchetto coordinate) {
		this.coordinate = coordinate;
		this.id = id;
		game= true;
		d = new Dimensioni();
		newPaddle();
		newBall();
		if(id==1) {
			c = new Controls(paddle1);
		}
		if(id==2) {
			c = new Controls(paddle2);
		}
		score = new Score();
		this.setFocusable(true);
		this.addKeyListener(c);
		this.setPreferredSize(d.SCREEN_SIZE);
		
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void newBall() {
		ball = new Ball((d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2), (d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2),
				d.BALL_DIAMETER, d.BALL_DIAMETER);
	}

	public void newPaddle() {
		paddle1 = new Paddle(0, (d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2), d.PADDLE_WIDTH, d.PADDLE_HEIGHT, 1);
		
		paddle2 = new Paddle(d.GAME_WIDTH - d.PADDLE_WIDTH, (d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2), d.PADDLE_WIDTH,
							 d.PADDLE_HEIGHT, 2);
	}
	
	public void paint(Graphics g) {
		image = createImage(getWidth(), getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image, 0, 0, this);
	}

	public void draw(Graphics g) {
		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g);
		score.draw(g,id);
	}

	public void move() {
		if(id==1) {
			paddle1.move();
			paddle2.moveToCoordinate(coordinate.yPaddle2);
			ball.move(coordinate.xBall,coordinate.yBall);
		}
		if(id==2) {
			paddle1.moveToCoordinate(coordinate.yPaddle1);
			paddle2.move();
			ball.move(coordinate.xBall,coordinate.yBall);
		}
	}

	public void checkCollision() {
		if (paddle1.y <= 0) {
			paddle1.y = 0;
		}
		if (paddle1.y >= (d.GAME_HEIGHT - d.PADDLE_HEIGHT)) {
			paddle1.y = d.GAME_HEIGHT - d.PADDLE_HEIGHT;
		}
		if (paddle2.y <= 0) {
			paddle2.y = 0;
		}
		if (paddle2.y >= (d.GAME_HEIGHT - d.PADDLE_HEIGHT)) {
			paddle2.y = d.GAME_HEIGHT - d.PADDLE_HEIGHT;
		}
	}

	//dovrei aggiungere i due nomi dei player
	public void updateScore(int id) {
		if(id==1) {
			score.scorePlayer1++;
			//score.id=1;
		}
		if(id==2) {
			score.scorePlayer2++;
			//score.id=2;
		}
	}
	
	public void updateCoordinate(Pacchetto pacchetto) {
		if(id==1) {
			coordinate.yPaddle1=paddle1.y;
			coordinate.yPaddle2=pacchetto.yPaddle2;
			coordinate.yBall=pacchetto.yBall;
			coordinate.xBall=pacchetto.xBall;
		}
		if(id==2) {
			coordinate.yPaddle1=pacchetto.yPaddle1;
			coordinate.yPaddle2=paddle2.y;
			coordinate.yBall=pacchetto.yBall;
			coordinate.xBall=pacchetto.xBall;
		}
	}
	
	public void SetFinishedGame() {
		game=false;
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while (game) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				move();
				checkCollision();
				repaint();
				delta--;
			}
		}
	}
	
}