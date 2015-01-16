/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Szymon Bultrowicz
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
package pl.gda.pg.eti.kernelhive.gui.monitoring.infrastructure.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.ClusterDefinition;

public class ClusterVertex extends JPanel {

	private ClusterDefinition cluster;
	private static final long serialVersionUID = 100L;

	public ClusterVertex(ClusterDefinition cluster) {
		this.cluster = cluster;
		add(new JLabel("bla bla" + cluster.getHostname()));
		setBorder(BorderFactory.createLineBorder(Color.red, 2));
		setLayout(new BorderLayout());
		//setSize(500, 100);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		JLabel label = new JLabel("Cluster " + cluster.getHostname());
		label.setSize(200, 20);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		add(label, BorderLayout.NORTH);
		//setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500, 100);
	}
}
