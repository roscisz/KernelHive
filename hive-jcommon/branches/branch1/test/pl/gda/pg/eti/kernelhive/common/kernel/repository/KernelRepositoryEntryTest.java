
package pl.gda.pg.eti.kernelhive.common.kernel.repository;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;

public class KernelRepositoryEntryTest {

	private KernelRepositoryEntry entry;
	private GraphNodeType type;
	private List<KernelPathEntry> list;
	private String desc;
	
	@Before
	public void setUp() throws Exception {
		list = new ArrayList<KernelPathEntry>();
		list.add(new KernelPathEntry("test", "test", new URL("http://test.com"), null));
		type = GraphNodeType.GENERIC;
		desc = "test";
		entry = new KernelRepositoryEntry(type, desc, list);
	}

	@Test
	public void testGetGraphNodeType() {
		assertEquals(type, entry.getGraphNodeType());
	}

	@Test
	public void testGetKernelPaths() {
		assertEquals(list, entry.getKernelPaths());
	}

	@Test
	public void testGetKernelPathForName() {
		assertEquals(list.get(0).getPath(), entry.getKernelPathEntryForName("test").getPath());
	}
	
	@Test
	public void testGetDescription(){
		assertEquals(desc, entry.getDescription());
	}

}
