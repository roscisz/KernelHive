package pl.gda.pg.eti.kernelhive.common.graph.configuration;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;

/**
 * 
 * @author mschally
 *
 */
public interface IEngineGraphConfiguration extends IGraphConfiguration {
	/**
	 * loads graph as a list of {@link EngineGraphNodeDecorator} objects
	 * @return {@link List} of {@link EngineGraphNodeDecorator} objects
	 * @throws ConfigurationException
	 */
	List<EngineGraphNodeDecorator> loadGraphForEngine() throws ConfigurationException;
	/**
	 * loads graph as a list of {@link EngineGraphNodeDecorator} objects from given {@link File}
	 * @param file {@link File}
	 * @return {@link List} of {@link EngineGraphNodeDecorator} objects
	 * @throws ConfigurationException
	 */
	List<EngineGraphNodeDecorator> loadGraphForEngine(File file) throws ConfigurationException;
}
