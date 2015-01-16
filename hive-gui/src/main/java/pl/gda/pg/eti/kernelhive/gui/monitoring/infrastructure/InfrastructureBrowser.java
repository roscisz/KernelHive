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
package pl.gda.pg.eti.kernelhive.gui.monitoring.infrastructure;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.SwingUtilities;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.ClusterDefinition;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.DeviceDefinition;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.MonitoringClientBean;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.UnitDefinition;
import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.helpers.WebServiceHelper;

public class InfrastructureBrowser extends JTabContent {

	InfrastructureGraphPanel graphPanel;
	private MonitoringClientBean service;

	public InfrastructureBrowser(final MainFrame frame, final String title) {
		super(frame);
		this.setName(title);
		setLayout(new BorderLayout());

		graphPanel = new InfrastructureGraphPanel(frame);
		add(graphPanel, BorderLayout.CENTER);

		Button refreshButton = new Button("Refresh");
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadInfrastructure();
			}
		});
		add(refreshButton, BorderLayout.SOUTH);

		service = new WebServiceHelper().getMonitoringService();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				loadInfrastructure();
			}
		});
	}

	private void loadInfrastructure() {
		List<ClusterDefinition> clusters = service.getClusters();
		List<UnitDefinition> units = service.getUnits();
		List<DeviceDefinition> devices = service.getAllDevices();
		graphPanel.setInfrastructure(clusters, units, devices);
	}

	private List<UnitDefinition> loadUnits() {
		return null;
	}

	@Override
	public boolean saveContent(File file) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean saveContent() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean loadContent(File file) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean loadContent() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void redoAction() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void undoAction() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void cut() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void copy() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void paste() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void selectAll() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void clearSelection() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void refresh() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
