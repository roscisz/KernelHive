package pl.gda.pg.eti.kernelhive.common.graph.node.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NodeIdGeneratorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGenerateId() {
		assertNotNull(NodeIdGenerator.generateId());
	}

}
