package pl.gda.pg.eti.kernelhive.gui.component;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.gui.component.source.SourceCodeEditor.SyntaxStyle;

public class SyntaxStyleTest {
	
	SyntaxStyle style;

	@Before
	public void setUp() throws Exception {
		style = SyntaxStyle.C;
	}

	@Test
	public void testGetStyle() {
		assertNotNull(style.getStyle());
	}

	@Test
	public void testGetFileType() {
		assertNotNull(style.getFileType());
	}

	@Test
	public void testGetSyntaxStyle() {
		String mime = "text/plain";
		style = SyntaxStyle.getSyntaxStyle(mime);
		assertEquals(SyntaxStyle.NONE, style);
	}

	@Test
	public void testResolveSyntaxStyle() {
		String fileType = "c";
		style = SyntaxStyle.resolveSyntaxStyle(fileType);
		assertEquals(SyntaxStyle.C, style);
	}

}
