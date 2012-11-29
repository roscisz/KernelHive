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
