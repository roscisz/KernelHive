package pl.gda.pg.eti.kernelhive.repository.kernel.repository;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

import pl.gda.pg.eti.kernelhive.repository.graph.node.type.GraphNodeType;

/**
 * 
 * @author mschally
 * 
 */
public class KernelRepositoryEntry implements IKernelRepositoryEntry,
		Serializable {

	private static final long serialVersionUID = -4658587858383597251L;
	private final GraphNodeType type;
	private final String description;
	private final List<IKernelPathEntry> kernelsPaths;

	public KernelRepositoryEntry(GraphNodeType type, String description,
			List<IKernelPathEntry> kernelsPaths) {
		this.type = type;
		this.description = description;
		this.kernelsPaths = kernelsPaths;
	}

	/**
	 * return graph node type
	 * 
	 * @return {@link GraphNodeType}
	 */
	public GraphNodeType getGraphNodeType() {
		return type;
	}

	/**
	 * return list of {@link KernelPathEntry} objects
	 * 
	 * @return {@link List}
	 */
	public List<IKernelPathEntry> getKernelPaths() {
		return kernelsPaths;
	}

	/**
	 * gets {@link KernelPathEntry} for given kernel name
	 * 
	 * @param name
	 *            String
	 * @return {@link URL}
	 */
	public IKernelPathEntry getKernelPathEntryForName(String name) {
		for (IKernelPathEntry e : kernelsPaths) {
			if (e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * gets {@link KernelPathEntry} for given kernel id
	 * 
	 * @param id
	 *            {@link String}
	 * @return {@link KernelPathEntry}
	 */
	public IKernelPathEntry getKernelPathEntryForId(String id) {
		for (IKernelPathEntry e : kernelsPaths) {
			if (e.getId().equals(id)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * gets entry description
	 * 
	 * @return {@link String}
	 */
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		if (description != null) {
			return description + " (" + type.toString() + ")";
		} else {
			return type.toString();
		}
	}
}
