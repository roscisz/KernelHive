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
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class KernelPathEntryTest {

	private KernelPathEntry entry;
	private String name;
	private String path;
	private String id;
	
	@Before
	public void setUp() throws Exception {
		name = "test";
		id = "test";
		path = "http://www.test.com";
		entry = new KernelPathEntry(id, name, new URL(path), new HashMap<String, Object>());
	}

	@Test
	public void testGetName() {
		assertEquals(name, entry.getName());
	}

	@Test
	public void testGetPath() {
		assertEquals(path, entry.getPath().toExternalForm());
	}

	@Test
	public void testGetId() {
		assertEquals(id, entry.getId());
	}

}
