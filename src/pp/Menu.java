package pp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class Menu extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsername;
	private JTextField txtAddress;
	private JTextField txtPort;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Menu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(160, 153, 89, 23);
		contentPane.add(btnStart);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.setBounds(279, 200, 89, 23);
		contentPane.add(btnQuit);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(160, 62, 86, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		txtAddress = new JTextField();
		txtAddress.setBounds(160, 93, 86, 20);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setBounds(160, 122, 86, 20);
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		
		JLabel lblUser = new JLabel("Username:");
		lblUser.setBounds(72, 65, 62, 14);
		contentPane.add(lblUser);
		
		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setBounds(72, 96, 46, 14);
		contentPane.add(lblAddress);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(72, 125, 46, 14);
		contentPane.add(lblPort);
	}
}
