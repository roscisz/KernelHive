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

/**
 *
 * @author marcel
 *
 */
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
