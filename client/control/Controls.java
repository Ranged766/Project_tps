package control;

import java.awt.event.*;

import model.Paddle;

//classe che permette la lettura dei tasti
public class Controls implements KeyListener{
	public Paddle paddle;
	
	//costruttore
	public Controls(Paddle paddle) {
		this.paddle = paddle;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override //tasto premuto
	public void keyPressed(KeyEvent e) {
		paddle.KeyPressed(e);
	}

	@Override //tasto rilasciato
	public void keyReleased(KeyEvent e) {
		paddle.KeyReleased(e);
	}
}