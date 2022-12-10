package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GestionePannelli extends JFrame /*implements ActionListener*/ {
	
	public Menu m;
	public GamePanel gm;
	
	public GestionePannelli() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Pong");
		this.setBackground(Color.yellow);

		m = new Menu();
		gm = new GamePanel();
		
		this.add(gm);
		//m.getBtnConnect().addActionListener(this);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	//@Override
	/*public void actionPerformed(ActionEvent e) {
		if(e.getSource()==m.getBtnConnect()) {
			/*fa la connessione
			 * qua ci va la classe connection
			if connessione andata bene{
			
			this.remove(m);
			this.add(gm);
			this.revalidate();
		}
		
	}*/
}
