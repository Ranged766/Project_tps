package view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

@SuppressWarnings("serial")
public class FinalPanel extends JPanel {
	private JLabel lblVictory;
	//private JLabel lblNewLabel;
	
	public FinalPanel(int id, int winner) {
		setLayout(null);
		lblVictory = new JLabel("Ciao");
		//lblNewLabel = new JLabel("");
		lblVictory.setForeground(Color.RED);
		lblVictory.setFont(new Font("Roboto Bk", Font.BOLD, 20));
		if(winner == 0) {
			lblVictory = new JLabel("L'avversario oppure il Server si sono disconnessi");
		}
		else {
			if(id==winner) {
				/*
				lblNewLabel.setIcon(new ImageIcon(Menu.class.getResource("/img/victoryb.png")));
				lblNewLabel.setForeground(new Color(0, 0, 0));
				lblNewLabel.setFont(new Font("Sitka Text", Font.BOLD, 25));
				lblNewLabel.setBounds(10, 0, 1000, 215);
				add(lblNewLabel);
				*/
				lblVictory = new JLabel("Complimenti! Hai vinto");
			}
			else {
				lblVictory = new JLabel("HAHAHAHAH! Hai perso");
			}
		}
		
		lblVictory.setBounds(39, 137, 381, 24);
		add(lblVictory);
	}
}
