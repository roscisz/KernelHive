package pl.gda.pg.eti.kernelhive.repository.kernel.repository;

import java.net.URL;
import java.util.Map;

public interface IKernelPathEntry {

	String getName();
	URL getPath();
	Map<String, Object> getProperties();
	String getId();
}
