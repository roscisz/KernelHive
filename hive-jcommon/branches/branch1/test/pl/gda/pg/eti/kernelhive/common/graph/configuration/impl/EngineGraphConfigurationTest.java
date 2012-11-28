package pl.gda.pg.eti.kernelhive.common.graph.configuration.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.repository.graph.node.GraphNodeBuilderException;

public class EngineGraphConfigurationTest {

	private EngineGraphConfiguration config;

	@Before
	public void setUp() throws Exception {
		config = new EngineGraphConfiguration();
	}

	@Test
	public void testSetAndGetInputDataURL() throws ConfigurationException {
		final String url = "url";
		config.setInputDataURL(url);
		assertEquals(url, config.getInputDataURL());
	}

	@Test
	public void testSaveAndLoadGraphForEngine() throws IOException,
			ConfigurationException, GraphNodeBuilderException {
		// File f = File.createTempFile("test", "test");
		// List<EngineGraphNodeDecorator> list = new
		// ArrayList<EngineGraphNodeDecorator>();
		// list.add(new EngineGraphNodeDecorator(new GraphNodeBuilder()
		// .setId("test").setType(GraphNodeType.GENERIC).build()));
		//
		// config.saveGraphForEngine(list, f);
		// List<EngineGraphNodeDecorator> result = config.loadGraphForEngine(f);
		// assertEquals(list, result);
	}
}
