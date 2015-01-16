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
package pl.gda.pg.eti.kernelhive.gui.component.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FileTreeModel implements TreeModel {

	protected File root;
	protected List<TreeModelListener> treeModelListeners;

	public FileTreeModel(final File root) {
		this.root = root;
		treeModelListeners = new ArrayList<TreeModelListener>();
	}

	@Override
	public void addTreeModelListener(final TreeModelListener tml) {
		treeModelListeners.add(tml);
	}

	@Override
	public Object getChild(final Object node, final int index) {
		final String[] chilren = ((File) node).list();
		if ((chilren == null) || (index >= chilren.length))
			return null;
		else
			return new File((File) node, chilren[index]);
	}

	@Override
	public int getChildCount(final Object node) {
		final String[] children = ((File) node).list();
		if (children == null)
			return 0;
		else
			return children.length;
	}

	@Override
	public int getIndexOfChild(final Object node, final Object child) {
		final String[] children = ((File) node).list();
		if (children == null)
			return -1;
		final String childname = ((File) child).getName();
		for (int i = 0; i < children.length; i++) {
			if (children[i].equalsIgnoreCase(childname)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(final Object node) {
		return ((File) node).isFile();
	}

	@Override
	public void removeTreeModelListener(final TreeModelListener tml) {
		treeModelListeners.remove(tml);
	}

	@Override
	public void valueForPathChanged(final TreePath arg0, final Object arg1) {

	}

}
