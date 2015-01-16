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

public class PasswordKeyStore {

	private static PasswordKeyStore keyStore = null;
	private byte[] salt;
	private byte[] password;
	private String username;
	
	private PasswordKeyStore(){
		salt = null;
		password = null;
		username = null;
	}
	
	/**
	 * gets {@link PasswordKeyStore} instance
	 * @return {@link PasswordKeyStore}
	 */
	public static PasswordKeyStore getInstance(){
		if(keyStore==null){
			keyStore = new PasswordKeyStore();
		}
		return keyStore;
	}
	
	/**
	 * gets salt
	 * @return byte[]
	 */
	public byte[] getSalt(){
		return salt;
	}
	
	/**
	 * gets encrypted password
	 * @return byte[]
	 */
	public byte[] getPassword(){
		return password;
	}
	
	/**
	 * gets username
	 * @return String
	 */
	public String getUsername(){
		return username;
	}
	
	/**
	 * sets new encrypted password and salt used to generate it
	 * @param password byte[]
	 * @param salt byte[]
	 */
	public void setNewCredentials(String username, byte[] password, byte[] salt){
                this.username = username;
		this.password = password;
		this.salt = salt;
	}	
}
