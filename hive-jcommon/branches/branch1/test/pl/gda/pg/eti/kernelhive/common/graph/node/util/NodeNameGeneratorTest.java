package pl.gda.pg.eti.kernelhive.common.graph.node.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.repository.graph.node.util.NodeNameGenerator;

public class NodeNameGeneratorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGenerateName() {
		assertNotNull(NodeNameGenerator.generateName());
	}

}
