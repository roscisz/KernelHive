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
package pl.gda.pg.eti.kernelhive.repository.graph.node;

import java.io.Serializable;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.repository.graph.node.type.GraphNodeType;

public interface IGraphNodeBuilder extends Serializable {

	/**
	 * sets graph node type
	 * 
	 * @param type
	 *            {@link GraphNodeType}
	 * @return {@link IGraphNodeBuilder}
	 */
	IGraphNodeBuilder setType(GraphNodeType type);

	/**
	 * sets graph node id
	 * 
	 * @param id
	 *            {@link String}
	 * @return {@link IGraphNodeBuilder}
	 */
	IGraphNodeBuilder setId(String id);

	/**
	 * sets graph node name
	 * 
	 * @param name
	 *            {@link String}
	 * @return {@link IGraphNodeBuilder}
	 */
	IGraphNodeBuilder setName(String name);

	/**
	 * sets graph node properties
	 * 
	 * @param properties
	 *            {@link Map}
	 * @return {@link IGraphNodeBuilder}
	 */
	IGraphNodeBuilder setProperties(Map<String, Object> properties);

	/**
	 * builds the graph node according to the previously set properties
	 * 
	 * @return {@link IGraphNode}
	 * @throws GraphNodeBuilderException
	 */
	IGraphNode build() throws GraphNodeBuilderException;
}
