package view;

import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Dimensioni;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Menu extends JPanel{
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtPort;
	private JButton btnConnect;
	private Dimensioni d;
	
	public Menu() {
		d = new Dimensioni();
		this.setPreferredSize(new Dimension(d.GAME_WIDTH, d.GAME_HEIGHT));
		this.setBackground(Color.green);
		setLayout(null);
		
		txtName = new JTextField();
		txtName.setBounds(148, 75, 86, 20);
		add(txtName);
		txtName.setColumns(10);
		
		txtAddress = new JTextField();
		txtAddress.setBounds(148, 117, 86, 20);
		add(txtAddress);
		txtAddress.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setBounds(148, 155, 86, 20);
		add(txtPort);
		txtPort.setColumns(10);
		
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(148, 210, 89, 23);
		add(btnConnect);
		
		JLabel lblNewLabel = new JLabel("MENU");
		lblNewLabel.setBounds(174, 29, 46, 14);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("nome:");
		lblNewLabel_1.setBounds(78, 78, 46, 14);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("indirizzo:");
		lblNewLabel_2.setBounds(78, 120, 46, 14);
		add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("porta:");
		lblNewLabel_3.setBounds(78, 158, 46, 14);
		add(lblNewLabel_3);
	}
	
	public JTextField getTxtName() {
		return txtName;
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
