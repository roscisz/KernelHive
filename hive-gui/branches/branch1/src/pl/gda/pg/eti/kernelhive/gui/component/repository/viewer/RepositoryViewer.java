package pl.gda.pg.eti.kernelhive.gui.component.repository.viewer;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import pl.gda.pg.eti.kernelhive.repository.kernel.repository.IKernelRepositoryEntry;

/**
 * 
 * @author mschally
 *
 */
public class RepositoryViewer extends JList {

	private static final long serialVersionUID = 5736151002744175310L;

	public RepositoryViewer() {
		super();
		initRepositoryViewer();
	}

	public RepositoryViewer(IKernelRepositoryEntry[] array) {
		super(array);
		initRepositoryViewer();
	}

	public RepositoryViewer(ListModel model) {
		super(model);
		initRepositoryViewer();
	}

	public RepositoryViewer(Vector<IKernelRepositoryEntry> vector) {
		super(vector);
		initRepositoryViewer();
	}
	
	private void initRepositoryViewer(){
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DragSource ds = new DragSource();
		ds.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY,
				new RepositoryViewerDragGestureListener());
	}

}
