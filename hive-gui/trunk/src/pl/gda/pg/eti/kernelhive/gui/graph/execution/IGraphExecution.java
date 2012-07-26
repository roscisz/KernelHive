package pl.gda.pg.eti.kernelhive.gui.graph.execution;

import java.net.URL;

/**
 * 
 * @author mschally
 *
 */
public interface IGraphExecution {
	/**
	 * 
	 * @param url
	 */
	void setInputDataUrl(URL url);
	/**
	 * 
	 * @param stream
	 */
	void setSerializedGraphStream(byte[] stream);
	/**
	 * 
	 * @param username
	 */
	void setUsername(String username);
	/**
	 * 
	 * @param password
	 */
	void setPassword(char[] password);
	/**
	 * 
	 * @return
	 */
	int execute();	
}
