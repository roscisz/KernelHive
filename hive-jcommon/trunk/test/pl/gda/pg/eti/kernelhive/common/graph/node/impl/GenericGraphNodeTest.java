package pl.gda.pg.eti.kernelhive.common.graph.node.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;
import pl.gda.pg.eti.kernelhive.common.source.SourceFile;

public class GenericGraphNodeTest {
	
	IGraphNode node;

	@Before
	public void setUp() throws Exception {
		node = new GenericGraphNode();
	}
	
	@Test
	public void testSetAndGetNodeId(){
		String id = "id";
		assertNull(node.getNodeId());
		node.setNodeId(id);
		assertEquals(id, node.getNodeId());
	}

	@Test
	public void testAddAndRemoveAndGetFollowingNodes() {
		List<IGraphNode> list = new ArrayList<IGraphNode>();
		IGraphNode followingNode = new GenericGraphNode();
		assertEquals(list, node.getFollowingNodes());
		list.add(followingNode);
		node.addFollowingNode(node);
		assertEquals(list.get(0), node.getFollowingNodes().get(0));
		list.remove(followingNode);
		node.removeFollowingNode(followingNode);
		assertEquals(list, node.getFollowingNodes());		
	}

	@Test
	public void testGetPreviousNodes() {
		List<IGraphNode> list = new ArrayList<IGraphNode>();
		IGraphNode previousNode = new GenericGraphNode();
		assertEquals(list, node.getPreviousNodes());
		list.add(previousNode);
		node.addPreviousNode(node);
		assertEquals(list.get(0), node.getPreviousNodes().get(0));
		list.remove(previousNode);
		node.removePreviousNode(previousNode);
		assertEquals(list, node.getPreviousNodes());
	}

	@Test
	public void testGetChildrenNodes() {
		List<IGraphNode> list = new ArrayList<IGraphNode>();
		IGraphNode childNode = new GenericGraphNode();
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
		IGraphNode parent = new GenericGraphNode();
		assertNull(node.getParentNode());
		node.setParentNode(parent);
		assertEquals(parent, node.getParentNode());
	}

	@Test
	public void testSetAndGetX() {
		int x = 10;
		assertEquals(0, node.getX());
		node.setX(x);
		assertEquals(x, node.getX());		
	}

	@Test
	public void testGetY() {
		int y = 10;
		assertEquals(0, node.getY());
		node.setY(y);
		assertEquals(y, node.getY());
	}

	@Test
	public void testAddAndRemoveAndGetSourceFiles() {
		SourceFile sf = new SourceFile(new File("test"), "test");
		List<ISourceFile> list = new ArrayList<ISourceFile>();
		assertEquals(list, node.getSourceFiles());
		list.add(sf);
		node.addSourceFile(sf);
		assertEquals(list, node.getSourceFiles());
		list.remove(sf);
		node.removeSourceFile(sf);
		assertEquals(list, node.getSourceFiles());
	}

}
