/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.gui.component.repository.viewer;

import static org.junit.Assert.*;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelPathEntry;
import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRepositoryEntry;

public class TransferableKernelRepositoryEntryTest {

	private TransferableKernelRepositoryEntry tkre;
	private KernelRepositoryEntry entry;

	@Before
	public void setUp() throws Exception {
		entry = new KernelRepositoryEntry(GraphNodeType.GENERIC, "",
				new ArrayList<KernelPathEntry>());
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
