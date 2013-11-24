package pl.gda.pg.eti.kernelhive.gui.component.workflow.viewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.component.workflow.preview.WorkflowPreview;
import pl.gda.pg.eti.kernelhive.gui.component.workflow.progress.WorkflowProgressViewer;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.helpers.WorkspaceHelper;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineService;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineServiceException;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineServiceListenerAdapter;
import pl.gda.pg.eti.kernelhive.gui.networking.IExecutionEngineService;

public class WorkflowViewer extends JTabContent implements ActionListener, WorkflowViewerHandler {

	private static final long serialVersionUID = -3495327114777372433L;
	private static final Logger LOGGER = Logger.getLogger(WorkflowViewer.class.getName());
	private WorkflowViewerPanel panel;
	private IExecutionEngineService service;
	private ExecutionEngineServiceListenerAdapter adapter;

	public WorkflowViewer(MainFrame frame, String title) {
		super(frame);
		this.setName(title);
		panel = new WorkflowViewerPanel();
		panel.addRefreshBtnActionListener(this);
		panel.setWorkflowViewerHandler(this);
		add(panel);
		try {
			service = ExecutionEngineService.getInstance();
			adapter = new ExecutionEngineServiceListenerAdapter() {
				@Override
				public void workflowBrowseCompleted(
						List<WorkflowInfo> workflowInfo) {
					panel.reloadTableContents(workflowInfo);
				}
			};
		} catch (ExecutionEngineServiceException e) {
			LOGGER.log(Level.SEVERE, "Engine execution exception", e);
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				refresh();
			}
		});
	}

	@Override
	public boolean saveContent(File file) {
		return true;
	}

	@Override
	public boolean loadContent(File file) {
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
		panel.selectAllTableContents();
	}

	@Override
	public void refresh() {
		if (service != null) {
			service.browseWorkflows(adapter);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		refresh();
	}

	@Override
	public boolean saveContent() {
		return true;
	}

	@Override
	public boolean loadContent() {
		return true;
	}

	@Override
	public void clearSelection() {
		panel.clearSelection();
	}

	@Override
	public void showProgress(WorkflowInfo workflowInfo) {
		WorkflowProgressViewer tab = new WorkflowProgressViewer(frame, workflowInfo);
		new WorkspaceHelper().addTab(frame.getWorkspacePane(), tab, true);
	}

	@Override
	public void terminate(WorkflowInfo workflowInfo) {
		service.terminateWorkflow(workflowInfo.ID, null);
	}

	@Override
	public void previewWork(WorkflowInfo workflowInfo) {
		WorkflowPreview tab = new WorkflowPreview(frame, workflowInfo);
		new WorkspaceHelper().addTab(frame.getWorkspacePane(), tab, true);
	}
}
