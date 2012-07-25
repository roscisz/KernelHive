package pl.gda.pg.eti.kernelhive.common.graph.builder;

import static org.junit.Assert.*;

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
