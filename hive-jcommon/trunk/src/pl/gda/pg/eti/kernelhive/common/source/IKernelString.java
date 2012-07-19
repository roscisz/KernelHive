package pl.gda.pg.eti.kernelhive.common.source;

import java.util.Map;

public interface IKernelString {

	String getKernel();
	Map<String, Object> getProperties();
	String getId();
}
