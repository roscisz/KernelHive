package pl.gda.pg.eti.kernelhive.common.source;

import java.io.File;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;

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
	 * @param properties
	 */
	void setProperties(Map<String, Object> properties);
	/**
	 * 
	 * @return
	 */
	String getId();
	/**
	 * 
	 * @return
	 */
	List<ValidationResult> validate();
	
}
