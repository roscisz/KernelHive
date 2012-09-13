package pl.gda.pg.eti.kernelhive.gui.component.repository.viewer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.repository.graph.node.type.GraphNodeType;
import pl.gda.pg.eti.kernelhive.repository.kernel.repository.IKernelPathEntry;
import pl.gda.pg.eti.kernelhive.repository.kernel.repository.IKernelRepositoryEntry;
import pl.gda.pg.eti.kernelhive.repository.kernel.repository.KernelRepositoryEntry;

public class TransferableKernelRepositoryEntryTest {

	private TransferableKernelRepositoryEntry tkre;
	private IKernelRepositoryEntry entry;

	@Before
	public void setUp() throws Exception {
		entry = new KernelRepositoryEntry(GraphNodeType.GENERIC, "",
				new ArrayList<IKernelPathEntry>());
		tkre = new TransferableKernelRepositoryEntry(entry);
	}

	@Test
	public void testGetTransferDataFlavors() {
		DataFlavor[] df = tkre.getTransferDataFlavors();
		DataFlavor d = null;
		for (DataFlavor i : df) {
			if (i.equals(TransferableKernelRepositoryEntry.entryFlavour)) {
				d = i;
				break;
			}
		}
		assertNotNull(d);
	}

	@Test
	public void testIsDataFlavorSupported() {
		assertTrue(tkre
				.isDataFlavorSupported(TransferableKernelRepositoryEntry.entryFlavour));
		DataFlavor d = DataFlavor.stringFlavor;
		assertFalse(tkre.isDataFlavorSupported(d));
	}

	@Test
	public void testGetTransferData() throws UnsupportedFlavorException,
			IOException {
		KernelRepositoryEntry k = (KernelRepositoryEntry) tkre
				.getTransferData(TransferableKernelRepositoryEntry.entryFlavour);
		assertNotNull(k);
	}
}
