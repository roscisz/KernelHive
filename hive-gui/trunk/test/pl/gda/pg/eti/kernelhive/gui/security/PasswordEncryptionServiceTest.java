package pl.gda.pg.eti.kernelhive.gui.security;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.Before;
import org.junit.Test;

public class PasswordEncryptionServiceTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testAuthenticate() throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] salt = PasswordEncryptionService.generateSalt();
		char[] pass = new char[] {'t', 'e', 's', 't'};
		byte[] encrypted = PasswordEncryptionService.getEncryptedPassword(pass, salt);
		assertTrue(PasswordEncryptionService.authenticate(pass, encrypted, salt));
	}

	@Test
	public void testGetEncryptedPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] salt = PasswordEncryptionService.generateSalt();
		byte[] pass = PasswordEncryptionService.getEncryptedPassword(new char[]{'t', 'e', 's', 't'}, salt);
		assertNotNull(pass);
	}

	@Test
	public void testGenerateSalt() throws NoSuchAlgorithmException {
		byte[] salt = PasswordEncryptionService.generateSalt();
		assertNotNull(salt);
	}

}
