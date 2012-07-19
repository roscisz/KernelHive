package pl.gda.pg.eti.kernelhive.common.graph.configuration;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;

/**
 * gui graph configuration interface
 * @author mschally
 *
 */
public interface IGUIGraphConfiguration extends IGraphConfiguration {
	List<GUIGraphNodeDecorator> loadGraphForGUI() throws ConfigurationException;
	List<GUIGraphNodeDecorator> loadGraphForGUI(File file) throws ConfigurationException;
	void saveGraphForGUI(List<GUIGraphNodeDecorator> guiGraphNodes) throws ConfigurationException;
	void saveGraphForGUI(List<GUIGraphNodeDecorator> guiGraphNodes, File file) throws ConfigurationException;
}
