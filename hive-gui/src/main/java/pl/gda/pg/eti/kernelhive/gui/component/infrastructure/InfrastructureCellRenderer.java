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
package pl.gda.pg.eti.kernelhive.gui.component.infrastructure;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.DeviceInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.UnitInfo;

public class InfrastructureCellRenderer implements TreeCellRenderer {

	protected TreeCellRenderer renderer;

	public InfrastructureCellRenderer(final TreeCellRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public Component getTreeCellRendererComponent(final JTree tree,
			final Object value, final boolean selected, final boolean expanded,
			final boolean leaf, final int row, final boolean hasFocus) {
		String newValue;
		if (value instanceof ClusterInfo) {
			newValue = "Cluster " + ((ClusterInfo) value).ID + "@"
					+ ((ClusterInfo) value).hostname;
		} else if (value instanceof UnitInfo) {
			newValue = "Unit " + ((UnitInfo) value).ID;
		} else if (value instanceof DeviceInfo) {
			newValue = "Device: " + ((DeviceInfo) value).deviceString;
		} else {
			newValue = value.toString();
		}
		return renderer.getTreeCellRendererComponent(tree, newValue, selected,
				expanded, leaf, row, hasFocus);
	}
}
