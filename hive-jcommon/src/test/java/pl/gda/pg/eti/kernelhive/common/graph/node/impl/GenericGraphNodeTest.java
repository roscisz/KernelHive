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
package pl.gda.pg.eti.kernelhive.common.graph.node.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;

public class GenericGraphNodeTest {
	
	IGraphNode node;
	String id;
	
	@Before
	public void setUp() throws Exception {
		id = "id";
		node = new GenericGraphNode(id);
	}
	
	@Test
	public void testGetNodeId(){
		assertEquals(id, node.getNodeId());
	}

	@Test
	public void testAddAndRemoveAndGetFollowingNodes() {
		List<IGraphNode> list = new ArrayList<IGraphNode>();
		IGraphNode followingNode = new GenericGraphNode("test", null);
		assertEquals(list, node.getFollowingNodes());
		list.add(followingNode);
		node.addFollowingNode(followingNode);
		assertEquals(list.get(0), node.getFollowingNodes().get(0));
		list.remove(followingNode);
		node.removeFollowingNode(followingNode);
		assertEquals(list, node.getFollowingNodes());		
	}

	@Test
	public void testGetPreviousNodes() {
		List<IGraphNode> list = new ArrayList<IGraphNode>();
		IGraphNode previousNode = new GenericGraphNode("test", null);
		assertEquals(list, node.getPreviousNodes());
		list.add(previousNode);
		node.addPreviousNode(previousNode);
		assertEquals(list.get(0), node.getPreviousNodes().get(0));
		list.remove(previousNode);
		node.removePreviousNode(previousNode);
		assertEquals(list, node.getPreviousNodes());
	}

	@Test
	public void testGetChildrenNodes() {
		List<IGraphNode> list = new ArrayList<IGraphNode>();
		IGraphNode childNode = new GenericGraphNode("test", null);
		assertEquals(list, node.getChildrenNodes());
		list.add(childNode);
		node.addChildNode(childNode);
		assertEquals(list.get(0), node.getChildrenNodes().get(0));
		list.remove(childNode);
		node.removeChildNode(childNode);
		assertEquals(list, node.getChildrenNodes());
	}

	@Test
	public void testSetAndGetParentNode() {
		IGraphNode parent = new GenericGraphNode("test", null);
		assertNull(node.getParentNode());
		node.setParentNode(parent);
		assertEquals(parent, node.getParentNode());
	}
}
