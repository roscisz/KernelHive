package pl.gda.pg.eti.kernelhive.common.source;

import java.io.File;
import java.util.Map;

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
	public Map<String, Object> getProperties();
	/**
	 * 
	 * @return
	 */
	public String getId();
}
