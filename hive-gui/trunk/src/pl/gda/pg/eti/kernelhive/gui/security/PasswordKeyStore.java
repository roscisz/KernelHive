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
		this.password = password;
		this.salt = salt;
	}	
}
