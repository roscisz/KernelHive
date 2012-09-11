package pl.gda.pg.eti.kernelhive.common.graph.configuration.impl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.builder.GraphNodeBuilderException;
import pl.gda.pg.eti.kernelhive.common.graph.builder.impl.GraphNodeBuilder;
import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;

public class GUIGraphConfigurationTest {

	private GUIGraphConfiguration config;

	@Before
	public void setUp() throws Exception {
		config = new GUIGraphConfiguration();
	}

	@Test
	public void testSetAndGetProjectName() throws ConfigurationException {
		config.setProjectName("test");
		assertEquals("test", config.getProjectName());
	}

	@Test
	public void testSaveAndLoadGraphForGui() throws ConfigurationException,
			GraphNodeBuilderException, IOException {
		File f = File.createTempFile("test", "test");
		List<GUIGraphNodeDecorator> list = new ArrayList<GUIGraphNodeDecorator>();
		list.add(new GUIGraphNodeDecorator(new GraphNodeBuilder().setId("test")
				.setType(GraphNodeType.GENERIC).build()));
		config.saveGraphForGUI(list, f);
		List<GUIGraphNodeDecorator> result = config.loadGraphForGUI(f);
		assertEquals(list, result);
	}

}
