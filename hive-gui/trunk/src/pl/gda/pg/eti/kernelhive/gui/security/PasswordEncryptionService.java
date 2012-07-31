package pl.gda.pg.eti.kernelhive.gui.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Password Encryption Service
 * @author mschally
 *
 */
public class PasswordEncryptionService {

	/**
	 * authenticates given password
	 * @param attemptedPassword char[]
	 * @param encryptedPassword byte[]
	 * @param salt byte[]
	 * @return boolean
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static boolean authenticate(char[] attemptedPassword,
			byte[] encryptedPassword, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException{
		byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);
		return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
	}
	
	/**
	 * Generates encrypted password based on plaintext password and salt
	 * @param password char[]
	 * @param salt byte[]
	 * @return byte[] encrypted password
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static byte[] getEncryptedPassword(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
		// PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
		// specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
		String algorithm = "PBKDF2WithHmacSHA1";
		// SHA-1 generates 160 bit hashes, so that's what makes sense here
		int derivedKeyLength = 160;
		int iterations = 20000;
		
		KeySpec spec = new PBEKeySpec(password, salt, iterations, derivedKeyLength);
		SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
		
		return f.generateSecret(spec).getEncoded();
	}

	/**
	 * generates salt
	 * @return byte[] salt
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] generateSalt() throws NoSuchAlgorithmException{
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[8];
		random.nextBytes(salt);
		
		return salt;
	}
}
