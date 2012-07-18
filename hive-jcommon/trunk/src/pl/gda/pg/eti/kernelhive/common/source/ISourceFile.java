package pl.gda.pg.eti.kernelhive.common.source;

import java.io.File;

/**
 * 
 * @author mschally
 *
 */
public interface ISourceFile {

	/**
	 * 
	 * @return
	 */
	public File getFile();
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getProperty(String key);
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, Object value);
	/**
	 * 
	 * @return
	 */
	public String getId();
}
