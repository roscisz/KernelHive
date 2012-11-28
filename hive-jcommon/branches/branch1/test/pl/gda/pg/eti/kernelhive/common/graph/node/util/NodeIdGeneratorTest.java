package pl.gda.pg.eti.kernelhive.common.graph.node.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.repository.graph.node.util.NodeIdGenerator;

public class NodeIdGeneratorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGenerateId() {
		assertNotNull(NodeIdGenerator.generateId());
	}

}
