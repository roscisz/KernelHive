package pl.gda.pg.eti.kernelhive.gui.component.repository.viewer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelPathEntry;
import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRepositoryEntry;

public class RepositoryViewerModelTest {

	private RepositoryViewerModel model;
	private List<KernelRepositoryEntry> dataList;
	
	@Before
	public void setUp() throws Exception {
		dataList = new ArrayList<KernelRepositoryEntry>();
		dataList.add(new KernelRepositoryEntry(GraphNodeType.GENERIC, "", new ArrayList<KernelPathEntry>()));
		model = new RepositoryViewerModel(dataList);
	}

	@Test
	public void testGetSize() {
		int size = model.getSize();
		assertEquals(1, size);
	}

	@Test
	public void testGetElementAt() {
		KernelRepositoryEntry kre = model.getElementAt(0);
		assertEquals(dataList.get(0), kre);
	}

}
