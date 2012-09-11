package pl.gda.pg.eti.kernelhive.common.graph.configuration;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
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
	/**
	 * saves graph to the engine-specific xml format
	 * @param graphNodes {@link List} of {@link EngineGraphNodeDecorator} objects
	 * @throws ConfigurationException
	 */
	void saveGraphForEngine(List<EngineGraphNodeDecorator> graphNodes) throws ConfigurationException;
	/**
	 * saves graph to the engine-specific xml format
	 * @param graphNodes {@link List} of {@link EngineGraphNodeDecorator} objects
	 * @param file {@link File}
	 * @throws ConfigurationException
	 */
	void saveGraphForEngine(List<EngineGraphNodeDecorator> graphNodes, File file) throws ConfigurationException;
	/**
	 * gets input data url string
	 * @return {@link String}
	 * @throws ConfigurationException
	 */
	String getInputDataURL() throws ConfigurationException;
	/**
	 * sets input data url string
	 * @param inputDataUrl {@link String}
	 * @throws ConfigurationException
	 */
	void setInputDataURL(String inputDataUrl) throws ConfigurationException;
	/**
	 * saves Graph to the engine-specific xml format
	 * @param graphNodes
	 * @param writer
	 * @throws ConfigurationException
	 */
	void saveGraphForEngine(List<EngineGraphNodeDecorator> graphNodes, Writer writer) throws ConfigurationException;
	/**
	 * loads graph from the engine-specific xml format
	 * @param reader
	 * @return
	 * @throws ConfigurationException
	 */
	List<EngineGraphNodeDecorator> loadGraphForEngine(Reader reader) throws ConfigurationException;
}
