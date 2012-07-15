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
	 * 
	 * @param type {@link GraphNodeType}
	 * @return {@link KernelRepositoryEntry}
	 * @throws ConfigurationException
	 */
	KernelRepositoryEntry getEntryForGraphNodeType(GraphNodeType type) throws ConfigurationException;
	/**
	 * 
	 * @return list of {@link KernelRepositoryEntry} 
	 * @throws ConfigurationException
	 */
	List<KernelRepositoryEntry> getEntries() throws ConfigurationException;

}
