package main;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import model.Connection;
import standard.StatusGame;

public class ServerMain extends Thread { 
	private ServerSocket server; 
	private Socket richiestaClient;
	public StatusGame sg;
	public ArrayList<Connection> connections;
	
	public ServerMain() { 
		sg = new StatusGame();
		connections = new ArrayList<>();
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
					connections.add(new Connection(richiestaClient,sg));
					System.out.println("connessione: "+richiestaClient);
				}
			} 
		}catch (IOException e) {
			e.printStackTrace(); 
		}
	}
	
	public static void main(String[] args) {
        new ServerMain();
    }
}