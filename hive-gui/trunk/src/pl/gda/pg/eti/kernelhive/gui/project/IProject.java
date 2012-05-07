package pl.gda.pg.eti.kernelhive.gui.project;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

public interface IProject {

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
	 * saves project configuration to xml file
	 * @return true if successful, false if not.
	 * @throws ConfigurationException
	 */
	void save() throws ConfigurationException;
	/**
	 * loads project configuration from xml file
	 */
	void load();
	/**
	 * 
	 * @return list of project nodes
	 */
	List<IProjectNode> getProjectNodes();
	/**
	 * sets list of project nodes
	 * @param nodes List of project nodes
	 */
	void setProjectNodes(List<IProjectNode> nodes);
	/**
	 * add new project node
	 * @param node new project node
	 */
	void addProjectNode(IProjectNode node);
	/**
	 * removes the selected prject node
	 * @param node project node to be removed
	 * @param removeFromDisc boolean indicating whether to delete corresponding files from disc
	 */
	void removeProjectNode(IProjectNode node, boolean removeFromDisc);
}