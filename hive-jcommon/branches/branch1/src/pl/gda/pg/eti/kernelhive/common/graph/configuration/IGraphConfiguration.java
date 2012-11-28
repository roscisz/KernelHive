package pl.gda.pg.eti.kernelhive.common.graph.configuration;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNode;

public interface IGraphConfiguration {
	/**
	 * sets file containing the xml project configuration
	 * @param file {@link File} - XML configuration file
	 */
	void setConfigurationFile(File file);
	/**
	 * gets the configuration file
	 * @return {@link File}
	 */
	File getConfigurationFile();
	/**
	 * gets project name
	 * @return {@link String}
	 * @throws ConfigurationException
	 */
	String getProjectName() throws ConfigurationException;
	/**
	 * sets project name
	 * @param name {@link String}
	 * @throws ConfigurationException
	 */
	void setProjectName(String name) throws ConfigurationException;
	/**
	 * saves graph to the xml configuration file associated with this graph configuration object
	 * @param nodes {@link List} of {@link IGraphNode} objects to save
	 * @throws ConfigurationException
	 */
	void saveGraph(List<IGraphNode> nodes) throws ConfigurationException;
	/**
	 * saves graph to the given xml configuration file
	 * @param nodes {@link List} of {@link IGraphNode} objects to save
	 * @param file {@link File} xml config file
	 * @throws ConfigurationException
	 */
	void saveGraph(List<IGraphNode> nodes, File file) throws ConfigurationException;
	/**
	 * loads graph from the xml configuration file associated with this graph configuration object
	 * @return {@link List} of {@link IGraphNode} objects
	 * @throws ConfigurationException
	 */
	List<IGraphNode> loadGraph() throws ConfigurationException;
	/**
	 * 
	 * @throws ConfigurationException
	 */
	void save() throws ConfigurationException;
	/**
	 * 
	 * @param file
	 * @throws ConfigurationException
	 */
	void save(File file) throws ConfigurationException;
	/**
	 * loads graph form the xml configuration fils
	 * @param file {@link File}
	 * @return {@link List} of {@link IGraphNode} objects
	 * @throws ConfigurationException
	 */
	List<IGraphNode> loadGraph(File file) throws ConfigurationException;
}
