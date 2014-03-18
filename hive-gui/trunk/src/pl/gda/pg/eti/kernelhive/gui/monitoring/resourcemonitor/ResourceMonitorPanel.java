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
package pl.gda.pg.eti.kernelhive.gui.monitoring.resourcemonitor;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ResourceMonitorPanel extends JPanel {

	private final JButton btnRefresh;
	private final JPanel picturePanel;
	private JCheckBox autoRefreshCheckbox;

	/**
	 *
	 */
	public ResourceMonitorPanel() {
		setLayout(new BorderLayout(0, 0));

		picturePanel = new JPanel();
		picturePanel.setLayout(new BoxLayout(picturePanel, BoxLayout.PAGE_AXIS));

		JScrollPane scroll = new JScrollPane(picturePanel);
		add(scroll);

		final JPanel buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.SOUTH);

		btnRefresh = new JButton("Refresh");
		buttonPanel.add(btnRefresh);

		autoRefreshCheckbox = new JCheckBox("Auto-refresh", true);
		buttonPanel.add(autoRefreshCheckbox);
	}

	public void addAutoRefreshCheckboxActionListener(final ActionListener l) {
		autoRefreshCheckbox.addActionListener(l);
	}

	public void removeAutoRefreshCheckboxActionListener(final ActionListener l) {
		autoRefreshCheckbox.removeActionListener(l);
	}

	public void addRefreshBtnActionListener(final ActionListener l) {
		btnRefresh.addActionListener(l);
	}

	public void removeRefreshBtnActionListener(final ActionListener l) {
		btnRefresh.removeActionListener(l);
	}

	public boolean getAutoRefreshState() {
		return autoRefreshCheckbox.isSelected();
	}

	public void addImage(String path) {
		try {
			BufferedImage bi = ImageIO.read(new URL(path));
			ImageIcon icon = new ImageIcon(bi);
			JLabel pictureLabel = new JLabel(icon);
			picturePanel.add(pictureLabel);
			picturePanel.revalidate();
		} catch (IOException ex) {
			Logger.getLogger(ResourceMonitorPanel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void clearImages() {
		picturePanel.removeAll();
		picturePanel.revalidate();
	}
}
