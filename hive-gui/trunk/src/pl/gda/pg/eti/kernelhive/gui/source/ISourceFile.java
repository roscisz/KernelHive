package pl.gda.pg.eti.kernelhive.gui.source;

import java.io.File;

public interface ISourceFile {

	public File getFile();
	public Object getProperty(String key);
	public void setProperty(String key, Object value);
}
