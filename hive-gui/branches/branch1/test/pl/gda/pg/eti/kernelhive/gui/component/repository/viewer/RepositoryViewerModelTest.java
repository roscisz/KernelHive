package pl.gda.pg.eti.kernelhive.gui.component.repository.viewer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.repository.kernel.repository.IKernelRepositoryEntry;

public class RepositoryViewerModelTest {

	private RepositoryViewerModel model;
	private List<IKernelRepositoryEntry> dataList;

	@Before
	public void setUp() throws Exception {
		// dataList = new ArrayList<IKernelRepositoryEntry>();
		// dataList.add(new KernelRepositoryEntry(GraphNodeType.GENERIC, "",
		// new ArrayList<IKernelPathEntry>()));
		// model = new RepositoryViewerModel(dataList);
	}

	@Test
	public void testGetSize() {
		final int size = model.getSize();
		assertEquals(1, size);
	}

	@Test
	public void testGetElementAt() {
		final IKernelRepositoryEntry kre = model.getElementAt(0);
		assertEquals(dataList.get(0), kre);
	}

}
