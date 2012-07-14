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

	/**
	 * 
	 */
	private static final long serialVersionUID = 192995314148788181L;
	protected File file;
	protected Map<String, Object> properties;
	
	public SourceFile(File file){
		this.file = file;
		properties = new HashMap<String, Object>();
	}
	
	public SourceFile(File file, Map<String, Object> properties){
		this.file = file;
		this.properties = properties;
	}
	
	@Override
	public File getFile() {
		return file;
	}

	@Override
	public Object getProperty(String key) {
		return properties.get(key);
	}

	@Override
	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}
	
	@Override
	public String toString(){
		return this.file.getAbsolutePath();
	}

}
