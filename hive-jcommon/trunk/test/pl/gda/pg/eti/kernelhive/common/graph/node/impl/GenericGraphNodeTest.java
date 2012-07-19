package pl.gda.pg.eti.kernelhive.common.graph.node.impl;

import static org.junit.Assert.*;

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
