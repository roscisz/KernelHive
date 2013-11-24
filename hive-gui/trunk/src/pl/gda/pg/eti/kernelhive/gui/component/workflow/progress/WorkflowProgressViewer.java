/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.component.workflow.progress;

import java.awt.BorderLayout;
import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import pl.gda.pg.eti.kernelhive.common.clientService.ClientBean;
import pl.gda.pg.eti.kernelhive.common.clientService.JobProgress;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.helpers.WebServiceHelper;

/**
 *
 * @author szymon
 */
public class WorkflowProgressViewer extends JTabContent {

	WorkflowProgressViewerPanel panel;
	WorkflowInfo workflowInfo;
	ClientBean service;
	Timer updateTimer;

	public WorkflowProgressViewer(MainFrame frame, WorkflowInfo workflowInfo) {
		super(frame);
		this.setName("Progress for workflow " + workflowInfo.ID);
		setLayout(new BorderLayout());

		this.workflowInfo = workflowInfo;
		service = new WebServiceHelper().getClientService();
		updateTimer = new Timer();

		panel = new WorkflowProgressViewerPanel();
		add(panel, BorderLayout.CENTER);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateProgress();
				updateTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						updateProgress();
					}
				}, 1000, 1000);
			}
		});
	}

	private void updateProgress() {
		try {
			List<JobProgress> progress = service.getWorkflowProgress(workflowInfo.ID);
			panel.setProgress(progress);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Error getting workflow progress", e);
		}
	}

	@Override
	public void onClose() {
		updateTimer.cancel();

		super.onClose();
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
