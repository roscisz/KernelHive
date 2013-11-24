/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author szymon
 */
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
