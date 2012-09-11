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
