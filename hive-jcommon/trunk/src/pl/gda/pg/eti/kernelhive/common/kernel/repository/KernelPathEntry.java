package pl.gda.pg.eti.kernelhive.common.kernel.repository;

import java.io.Serializable;
import java.net.URL;
import java.util.Map;

/**
 * 
 * @author mschally
 *
 */
public class KernelPathEntry implements Serializable {
	
	private static final long serialVersionUID = -3446193945659803907L;
	private final String name;
	private final String id;
	private final URL path;
	private final Map<String, Object> properties;
	
	public KernelPathEntry(String id, String name, URL path, Map<String, Object> properties){
		this.name = name;
		this.path = path;
		this.id = id;
		this.properties = properties;
	}

	/**
	 * gets the kernel name
	 * @return {@link String}
	 */
	public String getName() {
		return name;
	}

	/**
	 * gets the kernel java resource path
	 * @return {@link URL}
	 */
	public URL getPath() {
		return path;
	}

	/**
	 * gets kernel properties {@link Map}
	 * @return {@link Map}
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	/**
	 * gets kernel id
	 * @return {@link String}
	 */
	public String getId(){
		return id;
	}
}
