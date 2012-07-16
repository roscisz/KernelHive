package pl.gda.pg.eti.kernelhive.common.source;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author mschally
 *
 */
public class SourceFile implements ISourceFile, Serializable {

	private static final long serialVersionUID = 192995314148788181L;
	protected File file;
	protected Map<String, String> properties;
	
	public SourceFile(File file){
		this.file = file;
		properties = new HashMap<String, String>();
	}
	
	public SourceFile(File file, Map<String, String> map){
		this.file = file;
		this.properties = map;
	}
	
	@Override
	public File getFile() {
		return file;
	}

	@Override
	public String getProperty(String key) {
		return properties.get(key);
	}

	@Override
	public void setProperty(String key, String value) {
		properties.put(key, value);
	}
	
	@Override
	public String toString(){
		return this.file.getAbsolutePath();
	}
}
