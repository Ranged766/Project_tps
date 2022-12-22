package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import standard.Dimensioni;

@SuppressWarnings("serial")
public class Score extends Rectangle{
	public Dimensioni d;
	public int id;
	public int scorePlayer1;
	public int scorePlayer2;
	
	public Score() {
		d= new Dimensioni();
		id=0;
	}
	
	public void draw(Graphics g,int giacomo) {
		id=giacomo;
		g.setColor(Color.BLACK);
		g.setFont(new Font("Consolas", Font.PLAIN, 60));
		
		g.drawLine(d.GAME_WIDTH/2, 0, d.GAME_WIDTH/2, d.GAME_HEIGHT);
		
		g.drawString(String.valueOf(scorePlayer1), (d.GAME_WIDTH/2)-80, 50);
		g.drawString(String.valueOf(scorePlayer2), (d.GAME_WIDTH/2)+20, 50);
		
		g.setFont(new Font("Consolas", Font.PLAIN, 15));
		
		if(id==1) {
			g.drawString(String.valueOf("Tu sei il colore blu"), 60,10);
		}else {
			g.drawString(String.valueOf("Tu sei il colore rosso"), 780,10);
		}
		//fare il drawstring dei due nomi dei player
	}
}
