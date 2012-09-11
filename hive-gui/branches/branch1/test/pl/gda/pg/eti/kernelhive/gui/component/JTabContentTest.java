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
