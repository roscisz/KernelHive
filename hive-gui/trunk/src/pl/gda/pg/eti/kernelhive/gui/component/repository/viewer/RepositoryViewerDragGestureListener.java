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

import java.awt.Cursor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;

import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRepositoryEntry;

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
