package control;

import java.awt.event.*;

import model.Paddle;

public class Controls implements KeyListener{
	public Paddle paddle;
	public Paddle paddle2;
	
	public Controls(Paddle paddle, Paddle paddle2) {
		this.paddle = paddle;
		this.paddle2 = paddle2;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		paddle.KeyPressed(e);
		paddle2.KeyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		paddle.KeyReleased(e);
		paddle2.KeyReleased(e);
	}
}
