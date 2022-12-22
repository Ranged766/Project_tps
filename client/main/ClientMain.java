package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import operazioni.Operazione;
import standard.Pacchetto;
import view.GestionePannelli;

//classe che gestisce lo scambio di pacchetti del client
public class ClientMain extends Thread {
	
	//variabile socket per connettersi al server
	private Socket connection;
	
	//input&outputStream
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	//variabile utilizzata per indicare il vincitore
	public int winner;
	
	//variabile utilizzata per indicare il paddle utilizzato
	public int id;
	
	//variabile utilizzata per evitare il ripetersi delle richieste di start
	public boolean inGame1;
	
	//public String name;
	//public String opponentName;
	
	//variabili utilizzate per ricevere pacchetti e ricordare le coordinate
	public Pacchetto pacchetto;
	public Pacchetto coordinate;
	
	//parte grafica
	public GestionePannelli gp;

	public ClientMain() {
		
		//imposto le varibili con dei valori di base
		coordinate = new Pacchetto(-1, -1, -1, -1, -1, Operazione.NACK);
		inGame1 = false;
		winner = 0;
		
		try {
			//provo a connettermi al server (indirizzo da modificare se non si usa lo stesso pc)
			connection = new Socket(InetAddress.getLocalHost(), 20000);
			
			//stampo a video il menu
			gp = new GestionePannelli();
			
			//creo gli ObjectStream 
			output = new ObjectOutputStream(connection.getOutputStream());
			input = new ObjectInputStream(connection.getInputStream());
			
			//faccio partire il Thread
			this.start();
		} catch (UnknownHostException e) {
			//indirizzo inserito in connection non è valido
			System.out.println("UnknownHostException");
		} catch (IOException e) {
			//non si sono riusciti a creare gli ObjectStream
			System.out.println("Connsessione non riuscita");
		}
	}

	public void run() {
		try {
			/*parte riguardo al nome (temporaneamente tolta)
			while (!gp.start) {
				name = gp.m.getTxtName();
				//try { sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
				//System.out.println("wait " + name + " game: " + gp.start);
			}*/

			//ciclo che attende la propria richiesta venga accettata
			boolean flag = false;
			System.out.println("richiesta di startare");
			while (!flag && !inGame1) {
				flag = requestToStart();
			}
			System.out.println("richiesta accettata");

			//ciclo che attende la partita cominci
			boolean flag1 = false;
			System.out.println("attesa di startare");
			while (!flag1 && !inGame1) {
				flag1 = waitToStart();
			}

			//passaggio al pannello di gioco
			System.out.println("partita iniziata");
			gp.startGame(id, coordinate);
			inGame1 = true;

			//conversazione tra il client ed il server
			System.out.println("comincia la conversazione");
			conversazione();
			System.out.println("finisce la conversazione");

			//annuncio del vincitore e passaggio al pannello finale
			gp.endGame(id, winner);
			System.out.println("cambia schermata");

			//chiudo la connessione e gli ObjectStream
			input.close();
			output.close();
			connection.close();
		} catch (IOException e) {
			System.out.println("C'è stato un errore negli ObjectStream");
		}
	}
	
	//richiesta di partecipazione ad una partita
	public boolean requestToStart() {
		try {
			try {
				sleep(100);
			} catch (InterruptedException e1) {
				System.out.println("errore nello sleep");
			}
			pacchetto = new Pacchetto(-1, -1, -1, -1, 0, Operazione.Ready, " "/*name*/);
			System.out.println("pacchetto iniziale:" + pacchetto.toString());
			try {
				output.writeObject(pacchetto);
			} catch (IOException e) {
				System.out.println("errore nell'invio del pacchetto");
			}

			Object risposta = null;
			try {
				risposta = input.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Pacchetto pch = (Pacchetto) risposta;
			if (!(pch instanceof Pacchetto)) {
				System.out.println("risposta requestToStart: non era un pacchetto");
			} else {
				System.out.println("pacchetto di ACK:" + pch.toString());
				// ricevo operazione di ack più il nome dell'avversario
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
			Pacchetto pch = (Pacchetto) risposta;

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

	//scambio di dati tra server e client (scambio pacchetti contenenti coordinate)
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
					// client invia le proprie coordinate
					coordinate = updateCoordinate();
					System.out.println("coordinate:" + coordinate.toString());
					output.writeObject(coordinate);

					Object risposta = null;
					risposta = input.readObject();
					Pacchetto pch = (Pacchetto) risposta;
					if (!(pch instanceof Pacchetto)) {
						System.out.println("risposta non era un pacchetto");
					} else {
						System.out.println("pacchetto ricevuto: " + pch.toString());
						if (pch.operazione.equals(Operazione.inGame)) {
							updateBall(pch);
							System.out.println("coordinate in Game: " + coordinate.toString());
							gp.gm.updateCoordinate(pch);
						} else if (pch.operazione.equals(Operazione.Score)) {
							System.out.println("qualcuno ha segnato");
							gp.gm.updateCoordinate(pch);
							gp.gm.updateScore(pch.id);
						} else if (pch.operazione.equals(Operazione.Stop)) {
							System.out.println("il tuo avversario oppure il server si è disconnesso");
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
	public static void main(String[] args) {
		new ClientMain();
	}
}