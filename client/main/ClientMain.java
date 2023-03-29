package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import Chiper.Chacha20;
import operazioni.Operazione;
import standard.BytesPacchettoCriptato;
import standard.Pacchetto;
import view.GestionePannelli;

public class ClientMain extends Thread {
	
	private Socket connection;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	public int winner;
	public int id;
	public boolean inGame1;
	public Pacchetto pacchetto;
	public Pacchetto coordinate;
	public Chacha20 chacha;
	public GestionePannelli gp;

	public ClientMain() throws NoSuchAlgorithmException {
		
		coordinate = new Pacchetto(-1, -1, -1, -1, -1, Operazione.NACK);
		inGame1 = false;
		winner = 0;
		
		try {
			chacha=new Chacha20();
			connection = new Socket(InetAddress.getLocalHost(), 20000);
			gp = new GestionePannelli();
			output = new ObjectOutputStream(connection.getOutputStream());
			input = new ObjectInputStream(connection.getInputStream());
			this.start();
		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException");
		} catch (IOException e) {
			System.out.println("Connsessione non riuscita");
		}
	}

	public void run() {
		try {
			boolean fla=false;
			while(!fla) {
				Object risposta = null;
				try {
					risposta = input.readObject();
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				Chacha20 cha = (Chacha20) risposta;
				if (!(cha instanceof Chacha20)) {
					System.out.println("risposta: non era un cha cha");
				}
				else {
					chacha=cha;
					System.out.println("questo è il chacha inviato dal server:"+ chacha.toString());
					fla=true;
					output.writeObject(Operazione.ACK);
				}
			}
			
			boolean flag = false;
			System.out.println("richiesta di startare");
			while (!flag && !inGame1) {
				flag = requestToStart();
			}
			System.out.println("richiesta accettata");

			boolean flag1 = false;
			System.out.println("attesa di startare");
			while (!flag1 && !inGame1) {
				flag1 = waitToStart();
			}

			System.out.println("partita iniziata");
			gp.startGame(id, coordinate);
			inGame1 = true;

			System.out.println("comincia la conversazione");
			conversazione();
			System.out.println("finisce la conversazione");

			gp.endGame(id, winner);
			System.out.println("cambia schermata");

			input.close();
			output.close();
			connection.close();
		} catch (IOException e) {
			System.out.println("C'� stato un errore negli ObjectStream");
		}
	}
	
	public boolean requestToStart() {
		try {
			pacchetto = new Pacchetto(-1, -1, -1, -1, 0, Operazione.Ready);
			System.out.println("pacchetto iniziale:" + pacchetto.toString());
			try {
				byte[] st = chacha.PacchettoCripter(pacchetto);
				BytesPacchettoCriptato bpc = new BytesPacchettoCriptato(st);
				output.writeObject(bpc);
			} catch (IOException e) {
				System.out.println("errore nell'invio del pacchetto");
			}

			Object risposta = null;
			try {
				risposta = input.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//da byte a pacchetto, s è null
			BytesPacchettoCriptato s = (BytesPacchettoCriptato)risposta;
			Pacchetto pch = new Pacchetto();
			pch = chacha.PacchettoDecripter(s.getB());
			
			if (!(pch instanceof Pacchetto)) {
				System.out.println("risposta requestToStart: non era un pacchetto");
			} else {
				System.out.println("pacchetto di ACK:" + pch.toString());
				if (pch.operazione.equals(Operazione.ACK)) {
					id = pch.id;
					coordinate = new Pacchetto(pch.yPaddle1, pch.yPaddle2, pch.xBall, pch.yBall, pch.id,
							Operazione.Ready);
					System.out.println("il tuo id: " + id);
					return true;
				}
				return false;
			}
			return false;
		} catch (ClassNotFoundException e) {
			System.out.println("errore nella richiesta di start");
			return false;
		}
	}

	//attesa che il server invii un pacchetto con Operazione.Start
	public boolean waitToStart() {
		try {
			try {
				sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				System.out.println("errore nello sleep");
			}
			Object risposta = null;
			try {
				risposta = input.readObject();
			} catch (IOException e) {
				System.out.println("waitToStart: errore nella lettura");
			}
			
			//da byte a pacchetto
			BytesPacchettoCriptato s = (BytesPacchettoCriptato)risposta;
			Pacchetto pch = new Pacchetto();
			pch = chacha.PacchettoDecripter(s.getB());

			if (!(pch instanceof Pacchetto)) {
				System.out.println("waitToStart: non era un pacchetto");
			} else {

				if (pch.operazione.equals(Operazione.Start)) {
					//opponentName = pch.name;
					coordinate = new Pacchetto(pch.yPaddle1, pch.yPaddle2, pch.xBall, pch.yBall, pch.id,
							Operazione.inGame);
					System.out.println("pacchetto iniziale:" + coordinate.toString());
					return true;
				}
				return false;
			}
			return false;
		} catch (ClassNotFoundException e) {
			try {
				System.out.println("errore");
				sleep(100);
				return false;
			} catch (InterruptedException e1) {
			}
		}
		return false;
	}

	public void conversazione() {
		boolean inGame = true;
		try {
			try {
				sleep(100);
			} catch (InterruptedException e1) {
				System.out.println("errore nello sleep");
			}
			while (inGame) {
				try {
					coordinate = updateCoordinate();
					//System.out.println("coordinate:" + coordinate.toString());
					
					BytesPacchettoCriptato st = new BytesPacchettoCriptato(null);
					st.setB(chacha.PacchettoCripter(coordinate));
					output.writeObject(st);

					Object risposta = null;
					risposta = input.readObject();
					
					//da byte a pacchetto
					BytesPacchettoCriptato s = (BytesPacchettoCriptato)risposta;
					Pacchetto pch = new Pacchetto();
					pch = chacha.PacchettoDecripter(s.getB());
					
					if (!(pch instanceof Pacchetto)) {
						System.out.println("risposta non era un pacchetto");
					} else {
						//System.out.println("pacchetto ricevuto: " + pch.toString());
						if (pch.operazione.equals(Operazione.inGame)) {
							updateBall(pch);
							//System.out.println("coordinate in Game: " + coordinate.toString());
							gp.gm.updateCoordinate(pch);
						} else if (pch.operazione.equals(Operazione.Score)) {
							System.out.println("qualcuno ha segnato");
							gp.gm.updateCoordinate(pch);
							gp.gm.updateScore(pch.id);
						} else if (pch.operazione.equals(Operazione.Stop)) {
							System.out.println("il tuo avversario oppure il server si � disconnesso");
							inGame = false;
							gp.gm.SetFinishedGame();
						} else if (pch.operazione.equals(Operazione.Win)) {
							inGame = false;
							winner = pch.id;
							gp.gm.SetFinishedGame();
						}
					}
					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				} catch (ClassNotFoundException e) {
					System.out.println("errore");
				}
			}
		} catch (IOException e) {
			System.out.println("il server ha chiuso la connesione ");
			inGame=false;
		}
	}

	//aggiorna coordinate palla
	public void updateBall(Pacchetto pch) {
		coordinate.xBall = pch.xBall;
		coordinate.yBall = pch.yBall;
	}

	//aggiorna coordinate generali
	public Pacchetto updateCoordinate() {
		if (id == 1) {
			int yPaddle = gp.gm.paddle1.y;
			return new Pacchetto(yPaddle, coordinate.yPaddle2, coordinate.xBall, coordinate.yBall, id,
					Operazione.inGame);
		} else {
			int yPaddle = gp.gm.paddle2.y;
			return new Pacchetto(coordinate.yPaddle1, yPaddle, coordinate.xBall, coordinate.yBall, id,
					Operazione.inGame);
		}
	}

	//main
	public static void main(String[] args) throws NoSuchAlgorithmException {
		new ClientMain();
	}
}