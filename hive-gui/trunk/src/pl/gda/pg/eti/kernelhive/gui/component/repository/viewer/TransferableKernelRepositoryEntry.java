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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRepositoryEntry;

public class TransferableKernelRepositoryEntry implements Transferable {

	public static DataFlavor entryFlavour = new DataFlavor(
			KernelRepositoryEntry.class, "Kernel Repository Entry");

	protected static DataFlavor[] supportedFlavors = {entryFlavour};
	
	protected KernelRepositoryEntry entry;
	
	public TransferableKernelRepositoryEntry(KernelRepositoryEntry entry){
		this.entry = entry;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return supportedFlavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if(flavor.equals(entryFlavour)){
			return true;
		} else{
			return false;
		}
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if(flavor.equals(entryFlavour)){
			return entry;
		} else{
			throw new UnsupportedFlavorException(flavor);
		}
	}
}
