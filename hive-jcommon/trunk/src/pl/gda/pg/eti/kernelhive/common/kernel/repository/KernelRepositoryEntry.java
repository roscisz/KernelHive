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
	private final String description;
	private final Map<String, URL> kernelsPaths;
	
	
	public KernelRepositoryEntry(GraphNodeType type, String description, Map<String, URL> kernelsPaths){
		this.type = type;
		this.description = description;
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
	
	public String getDescription(){
		return description;
	}
	
	@Override
	public String toString(){
		if(description!=null){
			return description+" ("+type.toString()+")";
		} else{
			return type.toString();
		}
		
	}
}
