package operazioni;

import java.io.Serializable;

public enum Operazione implements Serializable{
    Ready,	// Il client annuncia di essere pronto per lo scambio di dati
    
    Start,	// Il server annuncia che la partita � cominciata
    
    Stop,	// Il server annuncia che la partita � chiusa
    
    Score,	// Il server annuncia un punto, insieme all'id di chi ha segnato
    
    inGame,	// Il server comunica la posizione degli altri
    
    Win,	// Il server annuncia la vittoria
    
    ACK,
    NACK
}