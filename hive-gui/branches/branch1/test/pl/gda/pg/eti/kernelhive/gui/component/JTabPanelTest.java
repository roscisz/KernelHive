package pl.gda.pg.eti.kernelhive.gui.component;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

public class JTabPanelTest {

	private JTabPanel panel;
	
	@Before
	public void setUp() throws Exception {
		MainFrame frame = new MainFrame();
		JTabContentStub contentStub = new JTabContentStub(frame);
		frame.getWorkspacePane().add(contentStub);
		panel = new JTabPanel(contentStub);
	}

	@Test
	public void testGetLabel() {
		assertNotNull(panel.getLabel());
	}
	
	@Test
	public void testGetPane(){
		assertNotNull(panel.getPane());		
	}
	
	@Test
	public void testGetPanel(){
		assertNotNull(panel.getTabContent());
	}
}
