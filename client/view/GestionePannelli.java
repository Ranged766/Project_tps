package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import standard.Pacchetto;

@SuppressWarnings("serial")
public class GestionePannelli extends JFrame implements ActionListener{
	
	public Menu m;
	public GamePanel gm;
	public FinalPanel fp;
	public int id;
	public boolean start;
	
	public GestionePannelli() {
		this.id=0;
		this.start = false;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Pong");
		
		m = new Menu();
		
		this.add(m);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void startGame(int id, Pacchetto coordinate) {
		this.remove(m);
		gm = new GamePanel(id,coordinate);
		this.add(gm);
		this.revalidate();
	}
	
	public void endGame(int id, int winner) {
		this.remove(gm);
		fp = new FinalPanel(id,winner);
		this.add(fp);
		this.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("ciao actionperformed");
		if(e.getSource()==m.getBtnConnect()) {
			start=true;
			System.out.println("uau");
		}
	}
}