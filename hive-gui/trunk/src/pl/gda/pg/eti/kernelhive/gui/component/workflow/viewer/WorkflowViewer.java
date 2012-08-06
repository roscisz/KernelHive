package pl.gda.pg.eti.kernelhive.gui.component.workflow.viewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.networking.IWorkflowService;
import pl.gda.pg.eti.kernelhive.gui.networking.WorkflowService;
import pl.gda.pg.eti.kernelhive.gui.networking.WorkflowServiceListenerAdapter;

public class WorkflowViewer extends JTabContent implements ActionListener {

	private static final long serialVersionUID = -3495327114777372433L;

	
	private WorkflowViewerPanel panel;
	private IWorkflowService service;
	private WorkflowServiceListenerAdapter adapter;
		
	public WorkflowViewer(MainFrame frame, String title) {
		super(frame);
		this.setName(title);
		panel = new WorkflowViewerPanel();
		panel.addRefreshBtnActionListener(this);
		add(panel);
		service = new WorkflowService();
		adapter = new WorkflowServiceListenerAdapter() {
			@Override
			public void workflowBrowseCompleted(List<WorkflowInfo> workflowInfo) {
				panel.reloadTableContents(workflowInfo);
			}
		};
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
		service.browseWorkflows(adapter);
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

}
