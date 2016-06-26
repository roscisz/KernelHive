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

package pl.gda.pg.eti.kernelhive.common.kernel.repository;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;

public class KernelRepositoryEntryTest {

	private KernelRepositoryEntry entry;
	private GraphNodeType type;
	private List<KernelPathEntry> list;
	private String desc;
	private Map<String, Object> props;

	@Before
	public void setUp() throws Exception {
		list = new ArrayList<KernelPathEntry>();
		list.add(new KernelPathEntry("test", "test", new URL("http://test.com"), null));
		type = GraphNodeType.GENERIC;
		desc = "test";
		props = new HashMap<>();
		entry = new KernelRepositoryEntry(type, desc, list, props);
	}

	@Test
	public void testGetGraphNodeType() {
		assertEquals(type, entry.getGraphNodeType());
	}

	@Test
	public void testGetKernelPaths() {
		assertEquals(list, entry.getKernelPaths());
	}

	@Test
	public void testGetKernelPathForName() {
		assertEquals(list.get(0).getPath(), entry.getKernelPathEntryForName("test").getPath());
	}

	@Test
	public void testGetDescription(){
		assertEquals(desc, entry.getDescription());
	}

}
