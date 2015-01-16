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
package pl.gda.pg.eti.kernelhive.common.source;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class SourceFileTest {
	
	private static IKernelFile sf;
	private static File file;
	private static String id;

	@Before
	public void setUp() throws Exception {
		file = File.createTempFile("test", ".test");
		id = "test";
		sf = new KernelFile(file, id);
	}

	@Test
	public void testGetFile() {
		assertEquals(file, sf.getFile());
	}

	@Test
	public void testGetId() {
		assertEquals(id, sf.getId());
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception{
		file.delete();
	}

}
