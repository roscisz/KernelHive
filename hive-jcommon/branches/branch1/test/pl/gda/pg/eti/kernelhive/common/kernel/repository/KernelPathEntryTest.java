package pl.gda.pg.eti.kernelhive.common.kernel.repository;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.repository.kernel.repository.KernelPathEntry;

public class KernelPathEntryTest {

	private KernelPathEntry entry;
	private String name;
	private String path;
	private String id;
	
	@Before
	public void setUp() throws Exception {
		name = "test";
		id = "test";
		path = "http://www.test.com";
		entry = new KernelPathEntry(id, name, new URL(path), new HashMap<String, Object>());
	}

	@Test
	public void testGetName() {
		assertEquals(name, entry.getName());
	}

	@Test
	public void testGetPath() {
		assertEquals(path, entry.getPath().toExternalForm());
	}

	@Test
	public void testGetId() {
		assertEquals(id, entry.getId());
	}

}
