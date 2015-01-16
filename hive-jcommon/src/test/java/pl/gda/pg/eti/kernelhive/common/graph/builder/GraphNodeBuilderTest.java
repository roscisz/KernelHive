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
package pl.gda.pg.eti.kernelhive.common.graph.builder;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.builder.impl.GraphNodeBuilder;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;

public class GraphNodeBuilderTest {

	private IGraphNodeBuilder builder;
	
	@Before
	public void setUp() throws Exception {
		builder = new GraphNodeBuilder();
	}

	@Test
	public void testBuild() throws GraphNodeBuilderException {
		GraphNodeType type = GraphNodeType.GENERIC;
		String nodeId = "test";
		String name = "name";
		IGraphNode node = builder.setId(nodeId).setType(type).setName(name).build();
		assertEquals(type, node.getType());
		assertEquals(nodeId, node.getNodeId());
		assertEquals(name, node.getName());
	}

}
