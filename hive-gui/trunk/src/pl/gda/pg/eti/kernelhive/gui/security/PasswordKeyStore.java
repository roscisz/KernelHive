package pl.gda.pg.eti.kernelhive.gui.security;

/**
 * 
 * @author mschally
 *
 */
public class PasswordKeyStore {

	private static PasswordKeyStore keyStore = null;
	private byte[] salt;
	private byte[] password;
	
	private PasswordKeyStore(){
		salt = null;
		password = null;
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
	 * sets new encrypted password and salt used to generate it
	 * @param password byte[]
	 * @param salt byte[]
	 */
	public void setNewPassword(byte[] password, byte[] salt){
		this.password = password;
		this.salt = salt;
	}	
}
