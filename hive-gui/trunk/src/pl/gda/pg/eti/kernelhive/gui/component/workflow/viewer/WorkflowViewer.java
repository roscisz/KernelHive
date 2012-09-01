package pl.gda.pg.eti.kernelhive.gui.component.workflow.viewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.networking.IExecutionEngineService;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineService;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineServiceException;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineServiceListenerAdapter;

public class WorkflowViewer extends JTabContent implements ActionListener {

	private static final long serialVersionUID = -3495327114777372433L;

	
	private WorkflowViewerPanel panel;
	private IExecutionEngineService service;
	private ExecutionEngineServiceListenerAdapter adapter;
		
	public WorkflowViewer(MainFrame frame, String title) {
		super(frame);
		this.setName(title);
		panel = new WorkflowViewerPanel();
		panel.addRefreshBtnActionListener(this);
		add(panel);
		try {
			service = new ExecutionEngineService();
			adapter = new ExecutionEngineServiceListenerAdapter() {
				@Override
				public void workflowBrowseCompleted(List<WorkflowInfo> workflowInfo) {
					panel.reloadTableContents(workflowInfo);
				}
			};
		} catch (ExecutionEngineServiceException e) {
			e.printStackTrace();
		}		
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
		if(service!=null){
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

}
