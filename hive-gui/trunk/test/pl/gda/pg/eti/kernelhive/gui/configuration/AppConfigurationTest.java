package pl.gda.pg.eti.kernelhive.gui.configuration;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AppConfigurationTest {
	
	private AppConfiguration config;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetInstance() {
		assertNotNull(AppConfiguration.getInstance());
	}
	
	@Test
	public void testgetResourceBundle(){
		testGetInstance();
		assertNotNull(AppConfiguration.getInstance().getLanguageResourceBundle());
	}

}
