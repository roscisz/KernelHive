package pl.gda.pg.eti.kernelhive.repository.kernel.repository;

import java.util.List;

import pl.gda.pg.eti.kernelhive.repository.graph.node.type.IGraphNodeType;
import pl.gda.pg.eti.kernelhive.repository.kernel.repository.KernelRepositoryEntry;

/**
 * 
 * @author mschally
 *
 */
public interface IKernelRepository {
	
	/**
	 * gets {@link KernelRepositoryEntry} for the given {@link GraphNodeType}
	 * @param type {@link GraphNodeType}
	 * @return {@link KernelRepositoryEntry}
	 * @throws ConfigurationException
	 */
	IKernelRepositoryEntry getEntryForGraphNodeType(IGraphNodeType type) throws KernelRepositoryException;
	/**
	 * gets a {@link List} of {@link KernelRepositoryEntry} objects
	 * @return list of {@link KernelRepositoryEntry} 
	 * @throws ConfigurationException
	 */
	List<IKernelRepositoryEntry> getEntries() throws KernelRepositoryException;

}
