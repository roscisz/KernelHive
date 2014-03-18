/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.common.graph.configuration;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;

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
