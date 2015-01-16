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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.ClusterDefinition;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.MonitoredEntity;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.MonitoredEntityType;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.MonitoringClientBean;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.MonitoringClientBeanService;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.UnitDefinition;

import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.monitoring.MonitoredEntityBuilder;

public class ResourceMonitor extends JTabContent implements
		ActionListener {

	private MonitoringClientBean service;
	private final ResourceMonitorPanel panel;
	private final JPanel dropdownPanel;
	private final JComboBox clustersDropdown;
	private final JComboBox unitsDropdown;
	private UnitDefinition selectedUnit;
	private Map<String, ClusterDefinition> clusters = new HashMap<>();
	private Map<String, UnitDefinition> units = new HashMap<>();
	private Timer refreshTimer = new Timer(6000, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			refresh();
		}
	});
	private boolean enableAutoRefresh = true;

	/**
	 *
	 * @param frame
	 * @param title
	 */
	public ResourceMonitor(final MainFrame frame, final String title, final UnitDefinition selectedUnit) {
		super(frame);
		this.setName(title);
		setLayout(new BorderLayout());
		panel = new ResourceMonitorPanel();
		panel.addRefreshBtnActionListener(this);
		panel.addAutoRefreshCheckboxActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableAutoRefresh = panel.getAutoRefreshState();
				if (enableAutoRefresh) {
					refresh();
				} else {
					refreshTimer.stop();
				}
			}
		});
		add(panel, BorderLayout.CENTER);

		dropdownPanel = new JPanel();
		add(dropdownPanel, BorderLayout.NORTH);
		clustersDropdown = new JComboBox();
		clustersDropdown.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String clusterHost = (String) e.getItem();
				ClusterDefinition cluster = clusters.get(clusterHost);
				loadUnits(cluster != null ? cluster.getId() : null);
			}
		});
		dropdownPanel.add(clustersDropdown);
		unitsDropdown = new JComboBox();
		dropdownPanel.add(unitsDropdown);

		initService();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				loadClusters();
				if (selectedUnit != null) {
					Logger.getLogger(getClass().getName()).info("unit given " + selectedUnit.getClusterId() + " " + selectedUnit.getUnitId());
					remove(dropdownPanel);
					setSelectedUnit(selectedUnit);
				} else {
					Logger.getLogger(getClass().getName()).info("unit not given");
					setSelectedUnit(units.get((String) unitsDropdown.getSelectedItem()));
				}
				refresh();
			}
		});
	}

	private void setSelectedUnit(UnitDefinition selectedUnit) {
		this.selectedUnit = selectedUnit;
	}

	private void initService() {
		try {
			URL url = new URL(AppConfiguration.getInstance().getEngineAddress(),
					"MonitoringClientBeanService/MonitoringClientBean?wsdl");
			service = new MonitoringClientBeanService(url)
					.getMonitoringClientBeanPort();
		} catch (MalformedURLException ex) {
			Logger.getLogger(ResourceMonitor.class.getName()).log(Level.SEVERE, null, ex);
			service = null;
		}
	}

	private void loadClusters() {
		if (service != null) {
			clusters.clear();
			for (ClusterDefinition cluster : service.getClusters()) {
				clusters.put(cluster.getHostname(), cluster);
				clustersDropdown.addItem(cluster.getHostname());
			}
		}
		loadUnits(clusters.size() > 0
				? clusters.get((String) clustersDropdown.getSelectedItem()).getId()
				: null);
	}

	private void loadUnits(Long clusterId) {
		unitsDropdown.removeAllItems();
		if (service != null && clusterId != null) {
			units.clear();
			for (UnitDefinition unit : service.getUnitsForCluster(new BigDecimal(clusterId).intValue())) {
				units.put(unit.getHostname(), unit);
				unitsDropdown.addItem(unit.getHostname());
			}
		}
	}

	@Override
	public boolean saveContent(final File file) {
		return true;
	}

	@Override
	public boolean saveContent() {
		return true;
	}

	@Override
	public boolean loadContent(final File file) {
		return true;
	}

	@Override
	public boolean loadContent() {
		return true;
	}

	@Override
	public void redoAction() {
	}

	@Override
	public void undoAction() {
	}

	@Override
	public void cut() {
	}

	@Override
	public void copy() {
	}

	@Override
	public void paste() {
	}

	@Override
	public void selectAll() {
	}

	@Override
	public final void refresh() {
		Logger.getLogger(getClass().getName()).info("refresh");
		if (enableAutoRefresh) {
			refreshTimer.restart();
		}
		if (service != null && selectedUnit != null) {
			Logger.getLogger(getClass().getName()).info("getting data");
			panel.clearImages();
			for (int i = 0; i < selectedUnit.getCpuCores(); i++) {
				addEntityGraph(new MonitoredEntityBuilder()
						.setClusterId(selectedUnit.getClusterId())
						.setUnitId(selectedUnit.getUnitId())
						.setType(MonitoredEntityType.CPU_USAGE)
						.setId(i)
						.get());
			}
			addEntityGraph(new MonitoredEntityBuilder()
					.setClusterId(selectedUnit.getClusterId())
					.setUnitId(selectedUnit.getUnitId())
					.setType(MonitoredEntityType.MEMORY)
					.get());
			addEntityGraph(new MonitoredEntityBuilder()
					.setClusterId(selectedUnit.getClusterId())
					.setUnitId(selectedUnit.getUnitId())
					.setType(MonitoredEntityType.CPU_SPEED)
					.get());

			for (int i = 0; i < selectedUnit.getDevicesCount(); i++) {
				addEntityGraph(new MonitoredEntityBuilder()
						.setClusterId(selectedUnit.getClusterId())
						.setUnitId(selectedUnit.getUnitId())
						.setDeviceId(i)
						.setType(MonitoredEntityType.GPU_USAGE)
						.get());
				addEntityGraph(new MonitoredEntityBuilder()
						.setClusterId(selectedUnit.getClusterId())
						.setUnitId(selectedUnit.getUnitId())
						.setDeviceId(i)
						.setType(MonitoredEntityType.GPU_GLOBAL_MEMORY)
						.get());
				addEntityGraph(new MonitoredEntityBuilder()
						.setClusterId(selectedUnit.getClusterId())
						.setUnitId(selectedUnit.getUnitId())
						.setDeviceId(i)
						.setType(MonitoredEntityType.FAN)
						.get());
			}
		}
	}

	private void addEntityGraph(MonitoredEntity entity) {
		try {
			String path = service.getGraphPath(entity);
			panel.addImage(new URL(AppConfiguration.getInstance().getEngineAddress(), path).toString());
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"Error while getting graph path", e);
		}
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		selectedUnit = units.get((String) unitsDropdown.getSelectedItem());
		refresh();
	}

	@Override
	public void clearSelection() {
	}

	@Override
	public void onClose() {
		refreshTimer.stop();
	}
}
