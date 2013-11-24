/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author szymon
 */
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
