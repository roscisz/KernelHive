package pl.gda.pg.eti.kernelhive.common.graph.node;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GraphNodeTypeTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetType() {
		GraphNodeType type = GraphNodeType.getType("generic");
		assertEquals(GraphNodeType.GENERIC, type);
	}

}
