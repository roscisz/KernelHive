package pl.gda.pg.eti.kernelhive.gui.wizard;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WizardTest {

	private Wizard wizard;
	
	@Before
	public void setUp() throws Exception {
		wizard = new Wizard();
	}

	@Test
	public void testGetDialog() {
		assertNotNull(wizard.getDialog());
	}

	@Test
	public void testGetModel() {
		assertNotNull(wizard.getModel());
	}

	@Test
	public void testGetReturnCode() {
		assertEquals(-1, wizard.getReturnCode());
	}
}
