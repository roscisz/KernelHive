package pl.gda.pg.eti.kernelhive.gui.component.repository.viewer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import pl.gda.pg.eti.kernelhive.repository.kernel.repository.IKernelRepositoryEntry;

/**
 * 
 * @author mschally
 *
 */
public class TransferableKernelRepositoryEntry implements Transferable {

	public static DataFlavor entryFlavour = new DataFlavor(
			IKernelRepositoryEntry.class, "Kernel Repository Entry");

	protected static DataFlavor[] supportedFlavors = {entryFlavour};
	
	protected IKernelRepositoryEntry entry;
	
	public TransferableKernelRepositoryEntry(IKernelRepositoryEntry entry){
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
