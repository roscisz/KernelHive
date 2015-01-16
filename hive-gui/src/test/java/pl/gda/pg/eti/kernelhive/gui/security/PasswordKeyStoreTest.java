/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
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
//		assertNull(salt);
	}

	@Test
	public void testGetPassword() {
		byte[] pass = PasswordKeyStore.getInstance().getPassword();
//		assertNull(pass);
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
