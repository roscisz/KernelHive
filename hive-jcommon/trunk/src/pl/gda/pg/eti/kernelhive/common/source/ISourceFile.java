package pl.gda.pg.eti.kernelhive.common.source;

import java.io.File;

/**
 * 
 * @author mschally
 *
 */
public interface ISourceFile {

	public File getFile();
	public Object getProperty(String key);
	public void setProperty(String key, Object value);
}
