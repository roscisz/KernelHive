package pl.gda.pg.eti.kernelhive.common.kernel.repository;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;

public class KernelRepositoryEntryTest {

	private KernelRepositoryEntry entry;
	private GraphNodeType type;
	private Map<String, URL> resourceMap;
	
	@Before
	public void setUp() throws Exception {
		resourceMap = new HashMap<String, URL>();
		resourceMap.put("test", new URL("http://test.com"));
		type = GraphNodeType.GENERIC;
		entry = new KernelRepositoryEntry(type, resourceMap);
	}

	@Test
	public void testGetGraphNodeType() {
		assertEquals(type, entry.getGraphNodeType());
	}

	@Test
	public void testGetKernelPaths() {
		assertEquals(resourceMap, entry.getKernelPaths());
	}

	@Test
	public void testGetKernelPathForName() {
		assertEquals(resourceMap.get("test"), entry.getKernelPathForName("test"));
	}

}
