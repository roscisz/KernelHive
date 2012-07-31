package pl.gda.pg.eti.kernelhive.common.graph.node;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.builder.GraphNodeBuilderException;
import pl.gda.pg.eti.kernelhive.common.graph.builder.impl.GraphNodeBuilder;

public class GraphNodeDecoratorConverterTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConvertGuiToEngine() throws GraphNodeBuilderException, GraphNodeDecoratorConverterException {
		IGraphNode node = new GraphNodeBuilder().setType(GraphNodeType.GENERIC).setId("test").build();
		GUIGraphNodeDecorator guiNode = new GUIGraphNodeDecorator(node);
		EngineGraphNodeDecorator engineNode = GraphNodeDecoratorConverter.convertGuiToEngine(guiNode);
		assertEquals(guiNode.getGraphNode(), engineNode.getGraphNode());
	}

}
