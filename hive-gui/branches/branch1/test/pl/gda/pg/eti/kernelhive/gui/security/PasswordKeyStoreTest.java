package pl.gda.pg.eti.kernelhive.gui.security;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PasswordKeyStoreTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetSalt() {
		byte[] salt = PasswordKeyStore.getInstance().getSalt();
		assertNull(salt);
	}

	@Test
	public void testGetPassword() {
		byte[] pass = PasswordKeyStore.getInstance().getPassword();
		assertNull(pass);
	}

	@Test
	public void testSetNewCredentials() {
		byte[] salt = new byte[1];
		byte[] pass = new byte[1];
		String username = "test";
		PasswordKeyStore.getInstance().setNewCredentials(username, salt, pass);
		assertNotNull(PasswordKeyStore.getInstance().getSalt());
		assertNotNull(PasswordKeyStore.getInstance().getPassword());
		assertNotNull(PasswordKeyStore.getInstance().getUsername());
	}
}
