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
	File getFile();
	/**
	 * 
	 * @param key
	 * @return
	 */
	Map<String, Object> getProperties();
	/**
	 * 
	 * @return
	 */
	String getId();
}
