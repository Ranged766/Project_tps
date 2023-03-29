package model;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import Chiper.Chacha20;
import operazioni.Operazione;
import standard.BytesPacchettoCriptato;
import standard.Dimensioni;
import standard.Pacchetto;
import standard.StatusGame;

public class Connection extends Thread {
	public Socket connection;
	public ObjectInputStream input;
	public ObjectOutputStream oos;
	public Dimensioni d;
	public int id;
	public StatusGame sg;
	public Pacchetto pacchetto;
	public Pacchetto coordinate;
	public Chacha20 chacha;

	public Connection(Socket richiestaClient, StatusGame sg1, Chacha20 chacha) throws NoSuchAlgorithmException {
		this.sg = sg1;
		this.chacha = chacha;
		System.out.println(sg);
		this.sg.numPlayer++;
		id = this.sg.numPlayer;
		this.d = new Dimensioni();
		coordinate = new Pacchetto((d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
				(d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2), (d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2),
				(d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2), sg.numPlayer, Operazione.NACK);
		this.pacchetto = new Pacchetto(-1, -1, -1, -1, 0, Operazione.NACK);
		try {
			connection = richiestaClient;
			System.out.println(
					"Connesione richiesta da: " + connection.getInetAddress().toString() + ":" + connection.getPort());
			sg.stabilisciStream(id, connection);
			this.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exception nel costruttore");
		}
	}

	public synchronized void run() {
		try {
			boolean flag=false;
			while(!flag) {
				//invio della chiave
				if (id == 1) {
					sg.oos1.writeObject(chacha);
				} else {
					sg.oos2.writeObject(chacha);
				}
				System.out.println("server ha inviato:"+chacha.toString());
				
				//ricezione conferma chiave
				Object risposta = null;
				try {
					if (id == 1) {
						risposta = sg.ois1.readObject();
					} else {
						risposta = sg.ois2.readObject();
					}
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				//qua ritorna un oggetto BytesPacchettoCrittato
				Operazione op = (Operazione) risposta;
				if (op instanceof Operazione) {
					flag=true;
				}
				else {
					System.out.println("no ack");
				}
			}
			
			
			while (true) {
				Object risposta = null;
				try {
					if (id == 1) {
						risposta = sg.ois1.readObject();
					} else {
						risposta = sg.ois2.readObject();
					}
					
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				
				//da byte a pacchetto
				BytesPacchettoCriptato s = (BytesPacchettoCriptato)risposta;
				Pacchetto pch = new Pacchetto();
				pch = chacha.PacchettoDecripter(s.getB());
				
				if (!(pch instanceof Pacchetto)) {
					System.out.println("pch non era un pacchetto");
				} else {

					//System.out.println("il thread numero " + id + " ha ricevuto: " + pch.toString());

					if (pch.operazione.equals(Operazione.Ready) && !sg.paddle1InGame && !sg.paddle2InGame) {
						System.out.println("ricezione pacchetto ready:" + pch.toString());
						System.out.println("numPlayer:" + sg.numPlayer);
						if (sg.numPlayer <= 2) {
							if (id == 1) {
								sg.name1 = pch.name;
								pacchetto = new Pacchetto((d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
										(d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
										(d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2),
										(d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2), id, Operazione.ACK);
							} else {
								sg.name2 = pch.name;
								pacchetto = new Pacchetto((d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
										(d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
										(d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2),
										(d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2), id, Operazione.ACK);
							}

							System.out.println("invio pacchetto ack:" + pacchetto.toString());
							try {
								if (id == 1) {
									BytesPacchettoCriptato st = new BytesPacchettoCriptato(null);
									st.setB(chacha.PacchettoCripter(pacchetto));
									sg.oos1.writeObject(st);
								} else {
									BytesPacchettoCriptato st = new BytesPacchettoCriptato(null);
									st.setB(chacha.PacchettoCripter(pacchetto));
									sg.oos2.writeObject(st);
								}
							} catch (IOException e) {
								System.out.println("errore nell'invio pacchetto ack");
							}
							if (sg.numPlayer == 2) {
								System.out.println("partita start");
								if (id == 1) {
									pacchetto = new Pacchetto((d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
											(d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
											(d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2),
											(d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2), id, Operazione.Start,
											sg.name2);
								} else {
									pacchetto = new Pacchetto((d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
											(d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
											(d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2),
											(d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2), id, Operazione.Start,
											sg.name1);
								}

								try {
									// invia ai client un messaggio di start
									if (id == 1) {
										sg.paddle1InGame = true;
										BytesPacchettoCriptato st = new BytesPacchettoCriptato(null);
										st.setB(chacha.PacchettoCripter(pacchetto));
										sg.oos1.writeObject(st);
										sg.oos2.writeObject(st);
									} else {
										sg.paddle2InGame = true;
										BytesPacchettoCriptato st = new BytesPacchettoCriptato(null);
										st.setB(chacha.PacchettoCripter(pacchetto));
										sg.oos1.writeObject(st);
										sg.oos2.writeObject(st);
									}
								} catch (IOException e) {
									System.out.println("errore nell'invio del pacchetto di start");
								}
							}
						} else {
							System.out.println("non c'� posto per te fra");
							// modalita spettatore, da implementare se voglio
						}
					} else if (pch.operazione.equals(Operazione.inGame)) {
						pacchetto = calcolaCoordinate(pch);
						//System.out.println("pacchetto post calcolaCoordinate: " + pacchetto.toString());
						try {
							// invia al client un messaggio di inGame
							if (pacchetto.operazione.equals(Operazione.Score)) {
								BytesPacchettoCriptato st = new BytesPacchettoCriptato(null);
								st.setB(chacha.PacchettoCripter(pacchetto));
								sg.oos1.writeObject(st);
								sg.oos2.writeObject(st);
							} else {
								if (id == 1) {
									BytesPacchettoCriptato st = new BytesPacchettoCriptato(null);
									st.setB(chacha.PacchettoCripter(pacchetto));
									sg.oos1.writeObject(st);
								} else {
									BytesPacchettoCriptato st = new BytesPacchettoCriptato(null);
									st.setB(chacha.PacchettoCripter(pacchetto));
									sg.oos2.writeObject(st);
								}
							}
							coordinate = pacchetto;
							//System.out.println("pacchetto inviato al client:" + pacchetto.toString());
						} catch (IOException e) {
						}
					} else if (pch.operazione.equals(Operazione.Stop)) {
						pacchetto = new Pacchetto((d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
								(d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2), (d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2),
								(d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2), id, Operazione.Stop);
						try {
							if (id == 1) {
								BytesPacchettoCriptato st = new BytesPacchettoCriptato(null);
								st.setB(chacha.PacchettoCripter(pacchetto));
								sg.oos1.writeObject(st);
							} else {
								BytesPacchettoCriptato st = new BytesPacchettoCriptato(null);
								st.setB(chacha.PacchettoCripter(pacchetto));
								sg.oos2.writeObject(st);
							}
						} catch (IOException e) {
							System.out.println("Exception scrittura pacchetto");
						}
					}
					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//System.out.println("situazione di gioco:" + sg.toString());
				}
				if (sg.scorePaddle1 == d.puntiVittoria || sg.scorePaddle2 == d.puntiVittoria) {
					if (sg.scorePaddle1 > sg.scorePaddle2) {
						pacchetto = new Pacchetto((d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
								(d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2), (d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2),
								(d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2), 1, Operazione.Win);
					} else {
						pacchetto = new Pacchetto((d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
								(d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2), (d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2),
								(d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2), 2, Operazione.Win);
					}
					try {
						if (id == 1) {
							BytesPacchettoCriptato st = new BytesPacchettoCriptato(null);
							st.setB(chacha.PacchettoCripter(pacchetto));
							sg.oos1.writeObject(st);
							sg.oos2.writeObject(st);
						}
					} catch (IOException e) {
						System.out.println("Exception scrittura pacchetto");
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Un client si � disconnesso, st� terminando la partita");
			try {
				if (id == 2) {
					BytesPacchettoCriptato st = new BytesPacchettoCriptato(null);
					st.setB(chacha.PacchettoCripter(new Pacchetto(1, 1, 1, 1, 1, Operazione.Stop)));
					sg.oos1.writeObject(st);
				}
				if (id == 1 && sg.oos2 != null) {
					BytesPacchettoCriptato st = new BytesPacchettoCriptato(null);
					st.setB(chacha.PacchettoCripter(new Pacchetto(1, 1, 1, 1, 2, Operazione.Stop)));
					sg.oos2.writeObject(st);
				}
			} catch (IOException e1) {
				System.out.println("Connection Reset By Peer");
			}
		}
	}

	public synchronized Pacchetto calcolaCoordinate(Pacchetto pch) {
		if (pch.id == 1) {
			sg.yPaddle1 = pch.yPaddle1;
		}
		if (pch.id == 2) {
			sg.yPaddle2 = pch.yPaddle2;
		}
		Random random = new Random();
		// se la palla si trova al centro
		if ((sg.yBall == (d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2)
				&& sg.xBall == (d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2))
				|| (sg.yVelocity == 0 && sg.xVelocity == 0)) {
			int randomXDirection = random.nextInt(2);
			if (randomXDirection == 0) {
				randomXDirection--;
			}
			sg.xVelocity = randomXDirection * d.ballSpeed;

			int randomYDirection = random.nextInt(2);
			if (randomYDirection == 0) {
				randomYDirection--;
			}
			sg.yVelocity = randomYDirection * d.ballSpeed;
		}

		checkCollision(pch);
		sg.xBall += sg.xVelocity;
		sg.yBall += sg.yVelocity;
		if (sg.score1) {
			sg.score1 = false;
			sg.yVelocity = 0;
			sg.xVelocity = 0;
			return new Pacchetto((d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
					(d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2), (d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2),
					(d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2), 1, Operazione.Score);
		}
		if (sg.score2) {
			sg.score2 = false;
			sg.yVelocity = 0;
			sg.xVelocity = 0;
			return new Pacchetto((d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2),
					(d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2), (d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2),
					(d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2), 2, Operazione.Score);
		}
		return new Pacchetto(sg.yPaddle1, sg.yPaddle2, sg.xBall, sg.yBall, id, Operazione.inGame);
	}

	public synchronized void checkCollision(Pacchetto pch) {
		Rectangle ball = new Rectangle(sg.xBall, sg.yBall, d.BALL_DIAMETER, d.BALL_DIAMETER);
		Rectangle paddle1 = new Rectangle(0, sg.yPaddle1, d.PADDLE_WIDTH, d.PADDLE_HEIGHT);
		Rectangle paddle2 = new Rectangle((d.GAME_WIDTH - d.PADDLE_WIDTH), sg.yPaddle2, d.PADDLE_WIDTH,
				d.PADDLE_HEIGHT);

		if (ball.y <= 0 || ball.y >= d.GAME_HEIGHT - d.BALL_DIAMETER) {
			sg.yVelocity = -1 * (sg.yVelocity);
		}

		if (ball.intersects(paddle1)) {
			sg.xVelocity = -1 * (sg.xVelocity);
			sg.xVelocity++;
			if (sg.yVelocity > 0) {
				sg.yVelocity++;
			} else {
				sg.yVelocity--;
			}
		}
		if (ball.intersects(paddle2)) {
			sg.xVelocity = -1 * (sg.xVelocity);
			sg.xVelocity--;
			if (sg.yVelocity > 0) {
				sg.yVelocity++;
			} else {
				sg.yVelocity--;
			}
		}
		if (ball.x <= 0) {
			System.out.println("player2 ha segnato");
			sg.scorePaddle2++;
			sg.score2 = true;
			sg.xBall = (d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2);
			sg.yBall = (d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2);
			sg.yPaddle1 = (d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2);
			sg.yPaddle2 = (d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2);
		}
		if (ball.x >= (d.GAME_WIDTH - d.BALL_DIAMETER)) {
			System.out.println("player1 ha segnato");
			sg.scorePaddle1++;
			sg.score1 = true;
			sg.xBall = (d.GAME_WIDTH / 2) - (d.BALL_DIAMETER / 2);
			sg.yBall = (d.GAME_HEIGHT / 2) - (d.BALL_DIAMETER / 2);
			sg.yPaddle1 = (d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2);
			sg.yPaddle2 = (d.GAME_HEIGHT / 2) - (d.PADDLE_HEIGHT / 2);
		}

		//System.out.println("YVelocity:" + sg.yVelocity);
		//System.out.println("XVelocity:" + sg.xVelocity);
	}
}