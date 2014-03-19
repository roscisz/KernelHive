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
package pl.gda.pg.eti.kernelhive.gui.helpers;

import javax.swing.JTabbedPane;
import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.component.JTabPanel;

public class WorkspaceHelper {

	public void addTab(JTabbedPane workspace, JTabContent tab, boolean autoSwitch) {
		workspace.addTab(tab.getName(), tab);
		final int index = workspace.getTabCount() - 1;
		workspace.setTabComponentAt(index, new JTabPanel(tab));
		if (autoSwitch) {
			workspace.getModel().setSelectedIndex(index);
		}
	}
}