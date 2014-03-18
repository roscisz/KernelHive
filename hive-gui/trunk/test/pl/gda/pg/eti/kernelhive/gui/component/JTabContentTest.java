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
package pl.gda.pg.eti.kernelhive.gui.component;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

public class JTabContentTest {
	
	JTabContent content;

	@Before
	public void setUp() throws Exception {
		MainFrame frame = new MainFrame();
		content = new JTabContentStub(frame);
		frame.getWorkspacePane().add(content);
	}

	@Test
	public void testSetAndGetTabPanel() {
		assertNull(content.getTabPanel());
		content.setTabPanel(new JTabPanel(content));
		assertNotNull(content.getTabPanel());
	}
	
	@Test
	public void testGetFrame(){
		assertNotNull(content.getFrame());
	}
	
	@Test
	public void testSetAndGetIsDirty(){
		content.setDirty(false);
		assertEquals(false, content.isDirty());
		content.setDirty(true);
		assertEquals(true, content.isDirty());
	}

}
