package pl.gda.pg.eti.kernelhive.gui.project.impl;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.gui.project.IProject;

public class KernelHiveProjectTest {

	IProject project;
	
	@Before
	public void setUp() throws Exception {
		//project = new KernelHiveProject("test", "test");
	}

	@Test
	public void testSetAndGetProjectDirectory() {
		fail("Not yet implemented");
//		String projectDir = "test1";
//		assertEquals("test", project.getProjectDirectory().getName());
//		project.setProjectDirectory(new File(projectDir));
//		assertEquals(projectDir, project.getProjectDirectory().getName());		
	}

	@Test
	public void testSetAndGetProjectFile() throws ConfigurationException {
		fail("Not yet implemented");
//		project.initProject();
//		assertNotNull(project.getProjectFile());
//		File f = new File("test");
//		project.setProjectFile(f);
//		assertEquals(f, project.getProjectFile());
	}

	@Test
	public void testGetProjectName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetProjectNodes() {
		fail("Not yet implemented");
	}

	@Test
	public void testLoad() {
		fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

}
