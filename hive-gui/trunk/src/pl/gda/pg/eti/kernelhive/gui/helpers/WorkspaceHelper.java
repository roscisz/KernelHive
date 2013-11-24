/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.helpers;

import javax.swing.JTabbedPane;
import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.component.JTabPanel;

/**
 *
 * @author szymon
 */
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
