package pl.gda.pg.eti.kernelhive.gui.project;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;

/**
 * 
 * @author mschally
 *
 */
public interface IProject {
	/**
	 * gets project file
	 * @return {@link File}
	 */
	File getProjectFile();
	/**
	 * sets project file
	 * @param file {@link File}
	 */
	void setProjectFile(File file);
	/**
	 * 
	 * @return project name
	 */
	String getProjectName();
	/**
	 * 
	 * @return project directory
	 */
	File getProjectDirectory();
	/**
	 * sets new project name
	 * @param name
	 */
	void setProjectName(String name);
	/**
	 * sets new project directory
	 * @param dir
	 */
	void setProjectDirectory(File dir);
	/**
	 * inits the project
	 * @throws ConfigurationException
	 */
	void initProject() throws ConfigurationException;
	/**
	 * saves project configuration to default xml file
	 * @return true if successful, false if not.
	 * @throws ConfigurationException
	 */
	void save() throws ConfigurationException;
	
	/**
	 * saves project configuration to given file
	 * @param file File to save
	 * @throws ConfigurationException
	 */
	void save(File file) throws ConfigurationException;
	/**
	 * loads project configuration from default xml file
	 */
	void load() throws ConfigurationException;
	
	/**
	 * loads project from given xml file
	 * @param file File to load
	 */
	void load(File file) throws ConfigurationException;
	/**
	 * 
	 * @return list of project nodes
	 */
	List<IGraphNode> getProjectNodes();
	/**
	 * sets list of project nodes
	 * @param nodes List of project nodes
	 */
	void setProjectNodes(List<IGraphNode> nodes);
	/**
	 * add new project node
	 * @param node new project node
	 */
	void addProjectNode(IGraphNode node);
	/**
	 * removes the selected project node
	 * @param node project node to be removed
	 * @param removeFromDisc boolean indicating whether to delete corresponding files from disc
	 */
	void removeProjectNode(IGraphNode node, boolean removeFromDisc);
}