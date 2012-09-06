package pl.gda.pg.eti.kernelhive.common.source;

import java.io.File;
import java.util.Map;

/**
 * 
 * @author mschally
 *
 */
public interface IKernelFile extends IKernelSource {

	/**
	 * 
	 * @return
	 */
	File getFile();
	
	/**
	 * 
	 * @param properties
	 */
	void setProperties(Map<String, Object> properties);
}
