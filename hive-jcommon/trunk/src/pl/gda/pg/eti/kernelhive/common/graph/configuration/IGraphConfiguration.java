package pl.gda.pg.eti.kernelhive.common.graph.configuration;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;

public interface IGraphConfiguration {
	
	void setConfigurationFile(File file);
	File getConfigurationFile();
	String getProjectName() throws ConfigurationException;
	void setProjectName(String name) throws ConfigurationException;
	void saveGraph(List<IGraphNode> nodes) throws ConfigurationException;
	void saveGraph(List<IGraphNode> nodes, File file) throws ConfigurationException;
	List<IGraphNode> loadGraph() throws ConfigurationException;
	List<IGraphNode> loadGraph(File file) throws ConfigurationException;
}
