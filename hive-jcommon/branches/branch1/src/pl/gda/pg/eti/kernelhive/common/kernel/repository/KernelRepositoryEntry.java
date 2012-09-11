package pl.gda.pg.eti.kernelhive.common.kernel.repository;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;

/**
 * 
 * @author mschally
 *
 */
public class KernelRepositoryEntry implements Serializable {

	private static final long serialVersionUID = -4658587858383597251L;
	private final GraphNodeType type;
	private final String description;
	private final List<KernelPathEntry> kernelsPaths;
	
	
	public KernelRepositoryEntry(GraphNodeType type, String description, List<KernelPathEntry> kernelsPaths){
		this.type = type;
		this.description = description;
		this.kernelsPaths = kernelsPaths;
	}
	
	/**
	 * return graph node type
	 * @return {@link GraphNodeType}
	 */
	public GraphNodeType getGraphNodeType(){
		return type;
	}
	
	/**
	 * return list of {@link KernelPathEntry} objects
	 * @return {@link List}
	 */
	public List<KernelPathEntry> getKernelPaths(){
		return kernelsPaths;
	}
	
	/**
	 * gets {@link KernelPathEntry} for given kernel name
	 * @param name String
	 * @return {@link URL}
	 */
	public KernelPathEntry getKernelPathEntryForName(String name){
		for(KernelPathEntry e : kernelsPaths){
			if(e.getName().equals(name)){
				return e;
			}
		}
		return null;
	}
	
	/**
	 * gets {@link KernelPathEntry} for given kernel id
	 * @param id {@link String}
	 * @return {@link KernelPathEntry}
	 */
	public KernelPathEntry getKernelPathEntryForId(String id){
		for(KernelPathEntry e: kernelsPaths){
			if(e.getId().equals(id)){
				return e;
			}
		}
		return null;
	}
	
	/**
	 * gets entry description
	 * @return {@link String}
	 */
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
