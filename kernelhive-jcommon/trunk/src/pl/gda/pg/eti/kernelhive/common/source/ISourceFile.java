package pl.gda.pg.eti.kernelhive.common.source;

import java.io.File;

public interface ISourceFile {

	public File getFile();
	public Object getProperty(String key);
	public void setProperty(String key, Object value);
}
