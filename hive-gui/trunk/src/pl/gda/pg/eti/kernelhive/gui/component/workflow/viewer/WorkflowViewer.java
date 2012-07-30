package pl.gda.pg.eti.kernelhive.gui.component.workflow.viewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

public class WorkflowViewer extends JTabContent implements ActionListener {

	private static final long serialVersionUID = -3495327114777372433L;

	
	private WorkflowViewerPanel panel;
	
	public WorkflowViewer(MainFrame frame) {
		super(frame);
		this.setName("Workflow Viewer");
		panel = new WorkflowViewerPanel();
		panel.addRefreshBtnActionListener(this);
		add(panel);
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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO retrieve data
		panel.refreshTableContents();		
	}

}
