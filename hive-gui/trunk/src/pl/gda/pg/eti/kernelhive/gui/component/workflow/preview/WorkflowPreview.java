/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.component.workflow.preview;

import java.awt.BorderLayout;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.MonitoringClientBean;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.PreviewObject;
import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.helpers.RuntimeClassFactory;
import pl.gda.pg.eti.kernelhive.gui.helpers.WebServiceHelper;

/**
 *
 * @author szymon
 */
public class WorkflowPreview extends JTabContent {

	WorkflowPreviewPanel panel;
	WorkflowInfo workflowInfo;
	MonitoringClientBean service;
	Timer updateTimer;

	public WorkflowPreview(MainFrame frame, WorkflowInfo workflowInfo) {
		super(frame);
		this.setName("Preview of workflow " + workflowInfo.ID);
		setLayout(new BorderLayout());

		this.workflowInfo = workflowInfo;
		service = new WebServiceHelper().getMonitoringService();
		updateTimer = new Timer();

		File projectDir = frame.getController().getProject().getProjectDirectory();
		File sourceFile = FileSystems.getDefault()
				.getPath(projectDir.getPath(), "PreviewProvider.java")
				.toFile();
		IPreviewProvider previewProvider;
		if (sourceFile.exists()) {
			previewProvider = RuntimeClassFactory.getPreviewProvider(sourceFile);
			panel = new WorkflowPreviewPanel(previewProvider);
			add(panel, BorderLayout.CENTER);
		} else {
			Logger.getLogger(getClass().getName())
					.warning("Preview source file does not exist: " + sourceFile.getAbsolutePath());
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updatePreview();
				updateTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						updatePreview();
					}
				}, 1000, 1000);
			}
		});
	}

	private void updatePreview() {
		try {
			List<PreviewObject> preview = service.getPreviewData(workflowInfo.ID);
			panel.setPreviewData(preview);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Error getting preview data", e);
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
