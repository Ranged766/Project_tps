package model;

import java.awt.*;

public class Score extends Rectangle{
	public Dimensioni d;
	public int scorePlayer1;
	public int scorePlayer2;
	
	public Score() {
		d= new Dimensioni();
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Consolas", Font.PLAIN, 60));
		
		g.drawLine(d.GAME_WIDTH/2, 0, d.GAME_WIDTH/2, d.GAME_HEIGHT);
		
		g.drawString(String.valueOf(scorePlayer1), (d.GAME_WIDTH/2)-80, 50);
		g.drawString(String.valueOf(scorePlayer2), (d.GAME_WIDTH/2)+20, 50);
	}
	
	
}
