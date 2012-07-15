package pl.gda.pg.eti.kernelhive.common.kernel.repository;

import java.io.Serializable;
import java.net.URL;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;

/**
 * 
 * @author mschally
 *
 */
public class KernelRepositoryEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4658587858383597251L;
	private final GraphNodeType type;
	private final Map<String, URL> kernelsPaths;
	
	
	public KernelRepositoryEntry(GraphNodeType type, Map<String, URL> kernelsPaths){
		this.type = type;
		this.kernelsPaths = kernelsPaths;
	}
	
	public GraphNodeType getGraphNodeType(){
		return type;
	}
	
	public Map<String, URL> getKernelPaths(){
		return kernelsPaths;
	}
	
	public URL getKernelPathForName(String name){
		return kernelsPaths.get(name);
	}
	
	@Override
	public String toString(){
		return type.toString();
	}
}
