package pl.gda.pg.eti.kernelhive.common.kernel.repository;

import java.io.Serializable;
import java.net.URL;
import java.util.Map;

public class KernelPathEntry implements Serializable {
	
	private static final long serialVersionUID = -3446193945659803907L;
	private final String name;
	private final URL path;
	private final Map<String, String> properties;
	
	public KernelPathEntry(String name, URL path, Map<String, String> properties){
		this.name = name;
		this.path = path;
		this.properties = properties;
	}

	public String getName() {
		return name;
	}

	public URL getPath() {
		return path;
	}

	public Map<String, String> getProperties() {
		return properties;
	}
	
	public String getProperty(String key){
		return properties.get(key);
	}
}
