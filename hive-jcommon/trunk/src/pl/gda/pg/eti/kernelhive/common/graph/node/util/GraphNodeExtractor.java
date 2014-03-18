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
package pl.gda.pg.eti.kernelhive.common.graph.node.util;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;

public class GraphNodeExtractor {

	/**
	 * 
	 * @param decorators
	 * @return
	 */
	public static List<IGraphNode> extractGraphNodesForGUI(
			List<GUIGraphNodeDecorator> decorators) {
		List<IGraphNode> result = new ArrayList<IGraphNode>();
		for (GUIGraphNodeDecorator a : decorators) {
			result.add(a.getGraphNode());
		}
		return result;
	}

	/**
	 * 
	 * @param decorators
	 * @return
	 */
	public static List<IGraphNode> extractGraphNodesForEngine(
			List<EngineGraphNodeDecorator> decorators) {
		List<IGraphNode> result = new ArrayList<IGraphNode>();
		for (EngineGraphNodeDecorator a : decorators) {
			result.add(a.getGraphNode());
		}
		return result;
	}
}
