/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
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
