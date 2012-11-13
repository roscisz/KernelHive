package pl.gda.pg.eti.kernelhive.gui.component.infrastructure;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.DeviceInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.UnitInfo;

public class InfrastructureTreeModel implements TreeModel {

	private class ClusterRoot {
		private static final String NAME = "Kernel Hive";
		private final List<ClusterInfo> clusters;

		private ClusterRoot(final List<ClusterInfo> clusters) {
			this.clusters = clusters;
		}

		@Override
		public String toString() {
			return NAME;
		}
	}

	private final ClusterRoot root;
	private final List<TreeModelListener> listeners;

	public InfrastructureTreeModel(final List<ClusterInfo> clusters) {
		root = new ClusterRoot(clusters);
		listeners = new ArrayList<TreeModelListener>();
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public Object getChild(final Object parent, final int index) {
		if (parent instanceof ClusterRoot) {
			return ((ClusterRoot) parent).clusters.get(index);
		} else if (parent instanceof ClusterInfo) {
			return ((ClusterInfo) parent).unitInfos.get(index);
		} else if (parent instanceof UnitInfo) {
			return ((UnitInfo) parent).deviceInfos.get(0);
		} else {
			return null;
		}
	}

	@Override
	public int getChildCount(final Object parent) {
		if (parent instanceof ClusterRoot) {
			return ((ClusterRoot) parent).clusters.size();
		} else if (parent instanceof ClusterInfo) {
			return ((ClusterInfo) parent).unitInfos.size();
		} else if (parent instanceof UnitInfo) {
			return ((UnitInfo) parent).deviceInfos.size();
		} else {
			return 0;
		}
	}

	@Override
	public boolean isLeaf(final Object node) {
		if (node instanceof DeviceInfo) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void valueForPathChanged(final TreePath path, final Object newValue) {

	}

	@Override
	public int getIndexOfChild(final Object parent, final Object child) {
		if (parent instanceof ClusterRoot) {
			return ((ClusterRoot) parent).clusters.indexOf(child);
		} else if (parent instanceof ClusterInfo) {
			return ((ClusterInfo) parent).unitInfos.indexOf(child);
		} else if (parent instanceof UnitInfo) {
			return ((UnitInfo) parent).deviceInfos.indexOf(child);
		} else {
			return -1;
		}
	}

	@Override
	public void addTreeModelListener(final TreeModelListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	@Override
	public void removeTreeModelListener(final TreeModelListener l) {
		listeners.remove(l);
	}

}
