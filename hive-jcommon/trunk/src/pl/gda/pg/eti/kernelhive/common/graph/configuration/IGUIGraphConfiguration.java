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
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;

public interface IGUIGraphConfiguration extends IGraphConfiguration {
	List<GUIGraphNodeDecorator> loadGraphForGUI() throws ConfigurationException;
	List<GUIGraphNodeDecorator> loadGraphForGUI(File file) throws ConfigurationException;
	void saveGraphForGUI(List<GUIGraphNodeDecorator> guiGraphNodes) throws ConfigurationException;
	void saveGraphForGUI(List<GUIGraphNodeDecorator> guiGraphNodes, File file) throws ConfigurationException;
}
