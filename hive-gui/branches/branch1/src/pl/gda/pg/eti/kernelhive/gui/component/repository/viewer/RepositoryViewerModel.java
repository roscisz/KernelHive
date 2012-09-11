package pl.gda.pg.eti.kernelhive.gui.component.repository.viewer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRepositoryEntry;

/**
 * 
 * @author mschally
 *
 */
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
