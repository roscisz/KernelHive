package pl.gda.pg.eti.kernelhive.common.source;

import java.util.HashMap;
import java.util.Map;

public class KernelString implements IKernelString {

	private String id;
	private String kernel;
	private Map<String, Object> properties;
	
	public KernelString(String id, String kernel){
		this.id = id;
		this.kernel = kernel;
		this.properties = new HashMap<String, Object>();
	}
	
	public KernelString(String id, String kernel, Map<String, Object> properties){
		this.id = id;
		this.kernel = kernel;
		if(properties!=null){
			this.properties = properties;
		} else{
			this.properties = new HashMap<String, Object>();
		}
	}
	
	@Override
	public String getKernel() {
		return kernel;
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public String getId() {
		return id;
	}

}
