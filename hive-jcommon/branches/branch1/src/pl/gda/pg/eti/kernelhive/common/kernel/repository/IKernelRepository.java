package pl.gda.pg.eti.kernelhive.common.kernel.repository;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;

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
	KernelRepositoryEntry getEntryForGraphNodeType(GraphNodeType type) throws ConfigurationException;
	/**
	 * gets a {@link List} of {@link KernelRepositoryEntry} objects
	 * @return list of {@link KernelRepositoryEntry} 
	 * @throws ConfigurationException
	 */
	List<KernelRepositoryEntry> getEntries() throws ConfigurationException;

}
