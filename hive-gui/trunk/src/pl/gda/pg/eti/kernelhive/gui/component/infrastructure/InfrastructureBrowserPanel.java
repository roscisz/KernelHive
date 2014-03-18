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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;

public class InfrastructureBrowserPanel extends JPanel {

	private static final long serialVersionUID = 5693611653576023015L;
	private final JTree tree;
	private TreeModel model;
	private final JButton btnRefresh;

	/**
	 * 
	 */
	public InfrastructureBrowserPanel() {
		setLayout(new BorderLayout(0, 0));

		final JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

		btnRefresh = new JButton("Refresh");
		panel.add(btnRefresh);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(600, 500));
		add(scrollPane, BorderLayout.CENTER);
		model = new InfrastructureTreeModel(new ArrayList<ClusterInfo>());
		tree = new JTree(model);
		tree.setCellRenderer(new InfrastructureCellRenderer(tree
				.getCellRenderer()));
		scrollPane.setViewportView(tree);
	}

	private void fillInfrastructureTree(final List<ClusterInfo> infrastructure) {
		model = new InfrastructureTreeModel(infrastructure);
		tree.setModel(model);
		tree.updateUI();
	}

	public void addRefreshBtnActionListener(final ActionListener l) {
		btnRefresh.addActionListener(l);
	}

	public void removeRefreshBtnActionListener(final ActionListener l) {
		btnRefresh.removeActionListener(l);
	}

	public void reloadTreeContents(final List<ClusterInfo> infrastructure) {
		fillInfrastructureTree(infrastructure);
	}

}
