package pl.gda.pg.eti.kernelhive.common.graph.node.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.builder.GraphNodeBuilderException;
import pl.gda.pg.eti.kernelhive.common.graph.builder.impl.GraphNodeBuilder;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;

public class GraphNodeDecoratorConverterTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConvertGuiToEngine() throws GraphNodeBuilderException {
		IGraphNode node = new GraphNodeBuilder().setType(GraphNodeType.GENERIC).setId("test").build();
		GUIGraphNodeDecorator guiNode = new GUIGraphNodeDecorator(node);
		EngineGraphNodeDecorator engineNode = GraphNodeDecoratorConverter.convertGuiToEngine(guiNode);
		assertEquals(guiNode.getGraphNode(), engineNode.getGraphNode());
	}

}
