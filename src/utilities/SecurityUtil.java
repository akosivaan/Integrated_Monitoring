package utilities;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import ch.ethz.ssh2.crypto.Base64;

public class SecurityUtil {
	private final static String KEY = "BlackBlueBrownUI";
	private final static String ALGO = "AES";
	
	protected static String encrypt(String value){
		String enc = "";
		try {
			Cipher cp = Cipher.getInstance(ALGO);
			Key key = generateKey();
			cp.init(Cipher.ENCRYPT_MODE, key);
			enc = new String(Base64.encode(cp.doFinal(value.getBytes())));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return enc;
	}
	
	protected static String decrypt(String value){
		String dec = "";
		Key key = generateKey();
		try {
			Cipher cp = Cipher.getInstance(ALGO);
			cp.init(Cipher.DECRYPT_MODE, key);
			dec = new String(cp.doFinal(Base64.decode(value.toCharArray())));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dec;
	}
	
	private static Key generateKey(){
		Key aesKey = new SecretKeySpec(KEY.getBytes(), ALGO);
		return aesKey;
	}
}
