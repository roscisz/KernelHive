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

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRepositoryEntry;

public class RepositoryViewerModel implements ListModel {

	private List<KernelRepositoryEntry> list;
	private List<ListDataListener> listenersList;
	
	
	public RepositoryViewerModel(List<KernelRepositoryEntry> dataList) {
		this.list = dataList;
		listenersList = new ArrayList<ListDataListener>();
	}
	
	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public KernelRepositoryEntry getElementAt(int index) {
		return list.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		if(!listenersList.contains(l)){
			listenersList.add(l);
		}
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listenersList.remove(l);
	}

}
