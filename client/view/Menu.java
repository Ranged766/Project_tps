package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import standard.Dimensioni;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class Menu extends JPanel{
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtPort;
	private JButton btnConnect;
	private Dimensioni d;
	private JTextPane txtpnBenvenutoGiocatoreInserisci;
	
	public Menu() {
		d = new Dimensioni();
		this.setPreferredSize(new Dimension(d.GAME_WIDTH, d.GAME_HEIGHT));
		this.setBackground(new Color(255, 255, 255));
		setLayout(null);
		
		txtName = new JTextField();
		txtName.setBounds(70, 75, -42, -37);
		add(txtName);
		txtName.setColumns(10);
		
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(166, 74, -52, -42);
		add(btnConnect);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Menu.class.getResource("/img/pong.png")));
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("Sitka Text", Font.BOLD, 25));
		lblNewLabel.setBounds(10, 0, 455, 215);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("nome:");
		lblNewLabel_1.setBounds(35, 78, -37, -26);
		add(lblNewLabel_1);
		
		txtpnBenvenutoGiocatoreInserisci = new JTextPane();
		txtpnBenvenutoGiocatoreInserisci.setBackground(new Color(192, 192, 192));
		txtpnBenvenutoGiocatoreInserisci.setFont(new Font("Lucida Fax", Font.PLAIN, 20));
		txtpnBenvenutoGiocatoreInserisci.setForeground(new Color(0, 0, 0));
		txtpnBenvenutoGiocatoreInserisci.setText("Benvenuto giocatore,\r\nverrai messo in attesa di una partita\r\npotrai comandare il tuo paddle con i tasti:\r\n-W(su) e S(giù) per il colore blue (Client 1)\r\n-FRECCETTA SU(su) e FRECCETTA GIU'(giù) per il colore rosso (Client 2)\r\nvince chi arriva per primo a 5 punti\r\n\r\nMOSSA SPECIALE:\r\n!!!Colpisci la pallina con il lato corto per un tiro a giro!!!\r\n\r\nBuon fortnite!1!1!!");
		txtpnBenvenutoGiocatoreInserisci.setEditable(false);
		txtpnBenvenutoGiocatoreInserisci.setBounds(512, 61, 417, 427);
		add(txtpnBenvenutoGiocatoreInserisci);
		
		JTextPane txtpnAttualmenteSeiIn = new JTextPane();
		txtpnAttualmenteSeiIn.setFont(new Font("Lucida Fax", Font.BOLD, 23));
		txtpnAttualmenteSeiIn.setText("Attualmente sei in attesa di un altro giocatore...");
		txtpnAttualmenteSeiIn.setBounds(54, 226, 324, 91);
		add(txtpnAttualmenteSeiIn);
	}
	
	public String getTxtName() {
		return txtName.getText();
	}
	public void setTxtName(JTextField txtName) {
		this.txtName = txtName;
	}
	public JTextField getTxtAddress() {
		return txtAddress;
	}
	public void setTxtAddress(JTextField txtAddress) {
		this.txtAddress = txtAddress;
	}
	public JTextField getTxtPort() {
		return txtPort;
	}
	public void setTxtPort(JTextField txtPort) {
		this.txtPort = txtPort;
	}
	public JButton getBtnConnect() {
		return btnConnect;
	}
	public void setBtnConnect(JButton btnConnect) {
		this.btnConnect = btnConnect;
	}	
}