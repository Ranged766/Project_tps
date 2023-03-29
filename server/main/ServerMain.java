package main;

import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import Chiper.Chacha20;
import model.Connection;
import standard.StatusGame;

public class ServerMain extends Thread { 
	private ServerSocket server; 
	private Socket richiestaClient;
	public StatusGame sg;
	public ArrayList<Connection> connections;
	public Chacha20 chacha;
	
	public ServerMain() throws NoSuchAlgorithmException { 
		sg = new StatusGame();
		connections = new ArrayList<>();
		chacha= new Chacha20();
		try { 
			server = new ServerSocket(20000, 5); 
			System.out.println("Server attivo"); 
			this.start();
		} 
		catch (IOException e) {
			System.out.println("Server gia' presente su questo indirizzo");
		} 
	}
	public void run() { 
		try { 
			//boolean flag = true; 
			while (true) { 
				if(sg.numPlayer<2) {
					richiestaClient = server.accept();
					connections.add(new Connection(richiestaClient,sg,chacha));
					System.out.println("connessione: "+richiestaClient);
				}
			} 
		}catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace(); 
		}
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
        new ServerMain();
    }
}