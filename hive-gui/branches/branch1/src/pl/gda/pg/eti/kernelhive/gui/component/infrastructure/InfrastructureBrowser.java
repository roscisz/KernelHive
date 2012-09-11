package pl.gda.pg.eti.kernelhive.gui.component.infrastructure;

import java.io.File;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineService;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineServiceException;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineServiceListenerAdapter;
import pl.gda.pg.eti.kernelhive.gui.networking.IExecutionEngineService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfrastructureBrowser extends JTabContent implements ActionListener{

	private static final long serialVersionUID = 4776693860508469103L;
	
	private IExecutionEngineService service;
	private ExecutionEngineServiceListenerAdapter adapter;
	private InfrastructureBrowserPanel panel;

	public InfrastructureBrowser(MainFrame frame, String title) {
		super(frame);
		this.setName(title);
		panel = new InfrastructureBrowserPanel();
		panel.addRefreshBtnActionListener(this);
		add(panel);
		try{
			service = ExecutionEngineService.getInstance();
			adapter = new ExecutionEngineServiceListenerAdapter() {
				@Override
				public void infrastractureBrowseCompleted(List<ClusterInfo> clusterInfo) {
					panel.reloadTreeContents(clusterInfo);
				}
			};
		} catch(ExecutionEngineServiceException e){
			e.printStackTrace();
		}
	}
	

	@Override
	public boolean saveContent(File file) {
		return true;
	}

	@Override
	public boolean saveContent() {
		return true;
	}

	@Override
	public boolean loadContent(File file) {
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
		if(service!=null){
			service.browseInfrastructure(adapter);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		refresh();
	}


	@Override
	public void clearSelection() {
		// TODO Auto-generated method stub
		
	}
}
