package pl.gda.pg.eti.kernelhive.gui.component.repository.viewer;

import java.awt.Cursor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;

import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRepositoryEntry;

/**
 * 
 * @author mschally
 *
 */
public class RepositoryViewerDragGestureListener implements DragGestureListener {

	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		Cursor cursor = null;
		if(dge.getComponent() instanceof RepositoryViewer){
			RepositoryViewer rv = (RepositoryViewer) dge.getComponent();
			KernelRepositoryEntry kre = (KernelRepositoryEntry) rv.getSelectedValue();
			
			if(dge.getDragAction()==DnDConstants.ACTION_COPY){
				cursor = DragSource.DefaultCopyDrop;
			}
			
			dge.startDrag(cursor, new TransferableKernelRepositoryEntry(kre));
		}
	}

}
