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
package pl.gda.pg.eti.kernelhive.gui.component.source;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.gui.component.JTabPanel;
import pl.gda.pg.eti.kernelhive.gui.component.source.SourceCodeEditor;
import pl.gda.pg.eti.kernelhive.gui.component.source.SourceCodeEditor.SyntaxStyle;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

public class SourceCodeEditorTest {
	
	SourceCodeEditor editor;

	@SuppressWarnings("unused")
	@Before
	public void setUp() throws Exception {
		MainFrame frame = new MainFrame();
		editor = new SourceCodeEditor(frame, "name");
		frame.getWorkspacePane().add(editor);
		JTabPanel panel = new JTabPanel(editor);
	}

	@Test
	public void testSaveContent() {
		File f = new File("testfile");
		editor.setText("text");
		assertEquals(true, editor.saveContent(f));
		assertEquals(true, f.delete());
	}

	@Test
	public void testLoadContent() {
		File f = new File("testfile");
		editor.setText("text");
		assertEquals(true, editor.saveContent(f));
		assertEquals(true, editor.loadContent(f));
		assertEquals(true, f.delete());
	}

	@Test
	public void testSetAndGetText() {
		String text = "text";
		assertEquals("", editor.getText());
		editor.setText(text);
		assertEquals(text, editor.getText());
	}

	@Test
	public void testSetAndGetName() {
		String name = "name1";
		assertEquals("name", editor.getFileName());
		editor.setFileName(name);
		assertEquals(name, editor.getFileName());
	}

	@Test
	public void testSetAndGetSyntaxStyle() {
		SyntaxStyle style = SyntaxStyle.C;
		SyntaxStyle none = SyntaxStyle.NONE;
		assertEquals(none, editor.getSyntaxStyle());
		editor.setSyntaxStyle(style);
		assertEquals(style, editor.getSyntaxStyle());
	}
}
