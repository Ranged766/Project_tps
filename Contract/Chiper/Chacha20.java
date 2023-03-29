package Chiper;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.ChaCha20ParameterSpec;

import standard.Pacchetto;

@SuppressWarnings("serial")
public class Chacha20 implements Serializable{
	
	private static final String ENCRYPT_ALGO = "ChaCha20";
	public SecretKey key;
	public byte[] nonce;          			
    public int counter;                     
    public byte[] cText;
    
    public Chacha20() throws NoSuchAlgorithmException {
		this.key = getKey();
		this.nonce = getNonce();
		this.counter = 1;
		this.cText = null;
	}
    
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public void setKey(SecretKey key) {
		this.key = key;
	}
	public void setNonce(byte[] nonce) {
		this.nonce = nonce;
	}



	public byte[] encrypt(byte[] pText, SecretKey key, byte[] nonce, int counter) throws Exception {

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO); // Si pu� scegliere l'algoritmo che si preferisce

        ChaCha20ParameterSpec param = new ChaCha20ParameterSpec(nonce, counter);

        cipher.init(Cipher.ENCRYPT_MODE, key, param);

        byte[] encryptedText = cipher.doFinal(pText); // Esegue l'operazione impostata

        return encryptedText;
    }

    public byte[] decrypt(byte[] cText, SecretKey key, byte[] nonce, int counter) throws Exception {

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

        ChaCha20ParameterSpec param = new ChaCha20ParameterSpec(nonce, counter);

        cipher.init(Cipher.DECRYPT_MODE, key, param); 

        byte[] decryptedText = cipher.doFinal(cText); // Esegue l'operazione impostata

        return decryptedText;

    }
    
    // A 256-bit secret key (32 bytes)
	private SecretKey getKey() throws NoSuchAlgorithmException {
	    KeyGenerator keyGen = KeyGenerator.getInstance("ChaCha20");
	    keyGen.init(256, SecureRandom.getInstanceStrong());
	    return keyGen.generateKey();
	}

	// 96-bit nonce (12 bytes)
	private byte[] getNonce() {
	    byte[] newNonce = new byte[12];
	    new SecureRandom().nextBytes(newNonce);
	    return newNonce;
	}

	@SuppressWarnings("unused")
	private String convertBytesToHex(byte[] bytes) {
	    StringBuilder result = new StringBuilder();
	    for (byte temp : bytes) {
	        result.append(String.format("%02x", temp));
	    }
	    return result.toString();
	}
	
	//da modificare in modo che torni una stringa col messaggio e chiave
	public byte[] PacchettoCripter(Pacchetto p) {
		String s= p.pacchettoDaCrittografare();
		
		try {
			cText = encrypt(s.getBytes(), key, nonce, counter);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
		/*System.out.println("Input          : " + s);
	    System.out.println("Input     (hex): " + convertBytesToHex(s.getBytes()));
	    System.out.println("\n---Encryption---");
	    System.out.println("Key       (hex): " + convertBytesToHex(key.getEncoded()));
	    System.out.println("Nonce     (hex): " + convertBytesToHex(nonce));
	    System.out.println("Counter        : " + counter);
	    System.out.println("Encrypted (hex): " + convertBytesToHex(cText));*/
	    
		return cText;
	}
	
	public Pacchetto PacchettoDecripter(byte[] cText) {
	    byte[] pText = null;
		try {
			pText = decrypt(cText, key, nonce, counter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Pacchetto p = new Pacchetto(0, 0, 0, 0, 0, null);
		p.stingaAPacchetto(new String(pText));
		
		/*System.out.println("\n---Decryption---");
	    System.out.println("Key       (hex): " + convertBytesToHex(key.getEncoded()));
	    System.out.println("Nonce     (hex): " + convertBytesToHex(nonce));
	    System.out.println("Counter        : " + counter);
	    System.out.println("Decrypted (hex): " + convertBytesToHex(pText));
	    System.out.println("Decrypted      : " + new String(pText));*/
	    
	    return p;
	}

	@Override
	public String toString() {
		return "Chacha20 [key=" + key + ", nonce=" + Arrays.toString(nonce) + ", counter=" + counter + ", cText="
				+ Arrays.toString(cText) + "]";
	}
	
}