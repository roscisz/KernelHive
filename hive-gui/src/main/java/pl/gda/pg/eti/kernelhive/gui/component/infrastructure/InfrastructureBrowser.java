/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
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
package pl.gda.pg.eti.kernelhive.gui.component.infrastructure;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineService;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineServiceException;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineServiceListenerAdapter;
import pl.gda.pg.eti.kernelhive.gui.networking.IExecutionEngineService;

public class InfrastructureBrowser extends JTabContent implements
		ActionListener {

	private static final long serialVersionUID = 4776693860508469103L;
	private IExecutionEngineService service;
	private ExecutionEngineServiceListenerAdapter adapter;
	private final InfrastructureBrowserPanel panel;

	/**
	 *
	 * @param frame
	 * @param title
	 */
	public InfrastructureBrowser(final MainFrame frame, final String title) {
		super(frame);
		this.setName(title);
		panel = new InfrastructureBrowserPanel();
		panel.addRefreshBtnActionListener(this);
		add(panel);
		try {
			service = ExecutionEngineService.getInstance();
			adapter = new ExecutionEngineServiceListenerAdapter() {
				@Override
				public void infrastractureBrowseCompleted(
						final List<ClusterInfo> clusterInfo) {
					panel.reloadTreeContents(clusterInfo);
				}
			};
		} catch (final ExecutionEngineServiceException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
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
	public void refresh() {
		if (service != null) {
			service.browseInfrastructure(adapter);
		}
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		refresh();
	}

	@Override
	public void clearSelection() {
	}
}
