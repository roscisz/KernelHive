package pl.gda.pg.eti.kernelhive.common.source;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;

/**
 * 
 * @author mschally
 *
 */
public class SourceFile implements ISourceFile, Serializable {

	private static final long serialVersionUID = 192995314148788181L;
	protected File file;
	protected String id;
	protected Map<String, Object> properties;
	
	public SourceFile(File file, String id){
		this.file = file;
		this.id = id;
		properties = new HashMap<String, Object>();
	}
	
	public SourceFile(File file, String id, Map<String, Object> map){
		this.file = file;
		this.properties = map;
		this.id = id;
	}
	
	@Override
	public File getFile() {
		return file;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String toString(){
		return this.file.getAbsolutePath();
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public List<ValidationResult> validate() {
		List<ValidationResult> result = new ArrayList<ValidationResult>();
		
		return result;		
	}

	@Override
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}
