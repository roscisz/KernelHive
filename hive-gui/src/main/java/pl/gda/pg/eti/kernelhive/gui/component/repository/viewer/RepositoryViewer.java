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

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRepositoryEntry;

public class RepositoryViewer extends JList {

	private static final long serialVersionUID = 5736151002744175310L;

	public RepositoryViewer() {
		super();
		initRepositoryViewer();
	}

	public RepositoryViewer(KernelRepositoryEntry[] array) {
		super(array);
		initRepositoryViewer();
	}

	public RepositoryViewer(ListModel model) {
		super(model);
		initRepositoryViewer();
	}

	public RepositoryViewer(Vector<KernelRepositoryEntry> vector) {
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
