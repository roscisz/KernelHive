package pl.gda.pg.eti.kernelhive.gui.frame;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.file.FileUtils;
import pl.gda.pg.eti.kernelhive.common.graph.configuration.IEngineGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.configuration.impl.EngineGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeDecoratorConverter;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeDecoratorConverterException;
import pl.gda.pg.eti.kernelhive.common.kernel.repository.IKernelRepository;
import pl.gda.pg.eti.kernelhive.common.kernel.repository.impl.KernelRepository;
import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.component.JTabPanel;
import pl.gda.pg.eti.kernelhive.gui.component.repository.viewer.RepositoryViewer;
import pl.gda.pg.eti.kernelhive.gui.component.repository.viewer.RepositoryViewerModel;
import pl.gda.pg.eti.kernelhive.gui.component.source.SourceCodeEditor;
import pl.gda.pg.eti.kernelhive.gui.component.source.SourceCodeEditor.SyntaxStyle;
import pl.gda.pg.eti.kernelhive.gui.component.tree.FileCellRenderer;
import pl.gda.pg.eti.kernelhive.gui.component.tree.FileTree;
import pl.gda.pg.eti.kernelhive.gui.component.tree.FileTreeModel;
import pl.gda.pg.eti.kernelhive.gui.component.workflow.editor.WorkflowEditor;
import pl.gda.pg.eti.kernelhive.gui.component.workflow.viewer.WorkflowViewer;
import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;
import pl.gda.pg.eti.kernelhive.gui.dialog.MessageDialog;
import pl.gda.pg.eti.kernelhive.gui.dialog.NewFileDialog;
import pl.gda.pg.eti.kernelhive.gui.dialog.NewProjectDialog;
import pl.gda.pg.eti.kernelhive.gui.project.IProject;
import pl.gda.pg.eti.kernelhive.gui.project.impl.KernelHiveProject;
import pl.gda.pg.eti.kernelhive.gui.workflow.execution.WorkflowExecution;
import pl.gda.pg.eti.kernelhive.gui.workflow.execution.IWorkflowExecution;
import pl.gda.pg.eti.kernelhive.gui.workflow.execution.WorkflowExecutionListener;
import pl.gda.pg.eti.kernelhive.gui.workflow.wizard.IWorkflowWizardDisplay;
import pl.gda.pg.eti.kernelhive.gui.workflow.wizard.WorkflowWizardDisplay;
import pl.gda.pg.eti.kernelhive.gui.workflow.wizard.WorkflowWizardDisplayException;

/**
 * 
 * @author mschally
 * 
 */
public class MainFrameController {

	private static final Logger LOG = Logger
			.getLogger(MainFrameController.class.getName());
	private static ResourceBundle BUNDLE = AppConfiguration.getInstance()
			.getLanguageResourceBundle();

	private MainFrame frame;
	private IProject project;
	private int newFileCounter;

	/**
	 * constructor
	 * 
	 * @param frame
	 *            {@link MainFrame}
	 */
	public MainFrameController(MainFrame frame) {
		this.frame = frame;
		newFileCounter = 1;
	}

	private void addTabToWorkspacePane(JTabContent tab){
		frame.getWorkspacePane().addTab(tab.getName(), tab);
		int index = frame.getWorkspacePane().getTabCount() - 1;
		frame.getWorkspacePane().setTabComponentAt(index, new JTabPanel(tab));
		frame.getWorkspacePane().getModel().setSelectedIndex(index);
	}

	/**
	 * closes project
	 */
	public void closeProject() {
		saveAll();
		saveProject();
		project = null;
		newFileCounter = 1;
		frame.getProjectScrollPane().setViewportView(null);
		frame.setProjectTree(null);
		frame.getRepositoryScrollPane().setViewportView(null);
		frame.setRepositoryList(null);
		
		
		while(frame.getWorkspacePane().getTabCount()>0){
			Component c = frame.getWorkspacePane().getTabComponentAt(0);
			if (c instanceof JTabPanel) {
				closeTab((JTabPanel) c);
			}
		}
	}

	/**
	 * closes the selected tab
	 * 
	 * @param tab
	 *            {@link JTabPanel}
	 */
	public void closeTab(JTabPanel tab) {
		JTabContent content;
		int index = frame.getWorkspacePane().getSelectedIndex();
		if (tab != null) {
			content = tab.getTabContent();
		} else {
			if (index != -1) {
				content = (JTabContent) frame.getWorkspacePane()
						.getComponentAt(index);
			} else {
				return;
			}
		}
		// TODO i18n
		if (content.isDirty()) {
			int result = JOptionPane.showConfirmDialog(frame.getContentPane(),
					"The selected file has been modified. "
							+ "Do you wanto to save it?", "Save file?",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				File f = content.getFile();
				if (f != null && f.exists()) {
					content.saveContent(f);
				} else {
					saveTabAs(tab);
				}
				frame.getWorkspacePane().removeTabAt(index);
			} else if (result == JOptionPane.NO_OPTION) {
				frame.getWorkspacePane().removeTabAt(index);
			} else if (result == JOptionPane.CANCEL_OPTION) {
				//do nothing
			}
		} else {
			frame.getWorkspacePane().removeTabAt(index);
		}
	}

	/**
	 * copies content from the selected tab
	 */
	public void copy() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.copy();
	}

	/**
	 * cuts content from the selected tab
	 */
	public void cut() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.cut();
	}

	/**
	 * controls the display of the side panel component
	 * 
	 * @param visible
	 */
	public void displaySidePanel(boolean visible) {
		// FIXME
		// frame.getSidePane().setVisible(visible);
	}

	/**
	 * controls the display of the statusbar component
	 * 
	 * @param visible
	 *            boolean
	 */
	public void displayStatusbar(boolean visible) {
		frame.getStatusbar().setVisible(visible);
	}

	/**
	 * controls the display of the toolbox component
	 * 
	 * @param visible
	 *            boolean
	 */
	public void displayToolbox(boolean visible) {
		frame.getToolBar().setVisible(visible);
	}

	/**
	 * exits the application
	 */
	public void exit() {
		saveAll();
		saveProject();
		frame.setVisible(false);
		frame.dispose();
	}

	/**
	 * creates new project
	 */
	public void newProject() {

		NewProjectDialog npd = new NewProjectDialog();
		npd.setVisible(true);
		if (npd.getStatus() == NewProjectDialog.APPROVE_OPTION) {
			try {
				File projectDir = new File(npd.getProjectDirectory()
						+ System.getProperty("file.separator")
						+ npd.getProjectName());
				project = new KernelHiveProject(projectDir,
						npd.getProjectName());
				project.initProject();
				frame.setTitle(npd.getProjectName() + " - "
						+ BUNDLE.getString("MainFrame.this.title"));

				FileTreeModel model = new FileTreeModel(
						project.getProjectDirectory());
				FileTree tree = new FileTree(frame, model);
				tree.setCellRenderer(new FileCellRenderer(tree
						.getCellRenderer()));
				frame.setProjectTree(tree);
				frame.getProjectScrollPane().setViewportView(
						frame.getProjectTree());

				IKernelRepository repository = new KernelRepository(
						AppConfiguration.getInstance().getKernelRepositoryURL());
				ListModel repoModel = new RepositoryViewerModel(
						repository.getEntries());
				frame.setRepositoryList(new RepositoryViewer(repoModel));
				frame.getRepositoryScrollPane().setViewportView(
						frame.getRepositoryList());

			} catch (ConfigurationException e) {
				LOG.warning("KH: cannot create new project");
				MessageDialog
						.showErrorDialog(
								frame,
								BUNDLE.getString("MainFrameController.newProject.cannotCreate.title"),
								BUNDLE.getString("MainFrameController.newProject.cannotCreate.text"));
				e.printStackTrace();
			}
		}
	}

	/**
	 * opens project
	 */
	public void openProject() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setMultiSelectionEnabled(false);
		FileFilter ff = new FileNameExtensionFilter("xml", "xml");
		fc.setFileFilter(ff);
		if (fc.showDialog(frame.getContentPane(), "Select") == JFileChooser.APPROVE_OPTION) {
			try {
				File file = fc.getSelectedFile();
				project = new KernelHiveProject(file.getParentFile(), null);
				project.setProjectFile(file);
				project.load();
				FileTreeModel model = new FileTreeModel(
						project.getProjectDirectory());
				FileTree tree = new FileTree(frame, model);
				tree.setCellRenderer(new FileCellRenderer(tree
						.getCellRenderer()));
				frame.setProjectTree(tree);
				frame.getProjectScrollPane().setViewportView(
						frame.getProjectTree());

				IKernelRepository repository = new KernelRepository(
						AppConfiguration.getInstance().getKernelRepositoryURL());
				ListModel repoModel = new RepositoryViewerModel(
						repository.getEntries());
				frame.setRepositoryList(new RepositoryViewer(repoModel));
				frame.getRepositoryScrollPane().setViewportView(
						frame.getRepositoryList());

				openWorkflowEditor();
				
			} catch (ConfigurationException e) {
				// TODO
			}
		}
	}
	
	/**
	 * opens new Tab and associates it with the given {@link File}
	 * 
	 * @param f
	 *            {@link File}
	 */
	public void openTab(File f) {
		SourceCodeEditor sourcePanel;
		if (f != null) {
			sourcePanel = new SourceCodeEditor(frame, f.getName());
			sourcePanel.setFile(f);
			sourcePanel.loadContent(f);
			sourcePanel.setSyntaxStyle(SyntaxStyle.resolveSyntaxStyle(f
					.getName().substring(f.getName().indexOf(".") + 1,
							f.getName().length())));
		} else {
			sourcePanel = new SourceCodeEditor(frame, "new" + newFileCounter);
			newFileCounter++;
		}
		addTabToWorkspacePane(sourcePanel);
	}

	/**
	 * opens the workflow editor
	 */
	public void openWorkflowEditor() {
		if (project != null) {
			WorkflowEditor editor = new WorkflowEditor(
					frame,
					BUNDLE.getString("MainFrameController.openWorkflowEditor.workflowEditor.text"),
					project);
			addTabToWorkspacePane(editor);
		} else {
			MessageDialog
					.showErrorDialog(
							frame,
							BUNDLE.getString("MainFrameController.openWorkflowEditor.error.title"),
							BUNDLE.getString("MainFrameController.openWorkflowEditor.error.text"));
		}
	}

	public void openWorkflowViewer() {
		WorkflowViewer wv = new WorkflowViewer(frame, "Workflow Viewer");
		addTabToWorkspacePane(wv);
//		frame.getWorkspacePane().add(wv, 0);
//		JTabPanel tabControl = new JTabPanel(wv);
//		frame.getWorkspacePane().setTabComponentAt(0, tabControl);
	}

	/**
	 * pastes copied/cut content to the selected tab
	 */
	public void paste() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.paste();
	}

	/**
	 * 
	 */
	public void pauseWorkflowGraphExecution() {
		// TODO Auto-generated method stub
	}

	/**
	 * redoes the last action on the selected tab
	 */
	public void redoAction() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.redoAction();
	}

	/**
	 * refreshes all components of the application
	 */
	public void refresh() {
		// refresh tree
		this.frame.getProjectTree().setModel(
				this.frame.getProjectTree().getModel());
		this.frame.getProjectTree().updateUI();
		// refresh tabs
		Component[] tabContents = this.frame.getWorkspacePane().getComponents();
		for (Component c : tabContents) {
			if (c instanceof JTabContent) {
				((JTabContent) c).refresh();
			}
		}
	}

	/**
	 * saves all tabs
	 */
	public void saveAll() {
		for (int i = 0; i < frame.getWorkspacePane().getTabCount(); i++) {
			Component c = frame.getWorkspacePane().getTabComponentAt(i);
			if (c instanceof JTabPanel) {
				saveTab((JTabPanel) c);
//				
//				JTabContent tc = ((JTabPanel) c).getTabContent();
//				if (tc.getFile() != null) {
//					tc.saveContent(tc.getFile());
//				}
			}
		}
	}

	/**
	 * saves project
	 */
	public void saveProject() {
		if(project!=null){
			try {
				project.save();
			} catch (ConfigurationException e) {
				LOG.warning("KH: could not save project");
				e.printStackTrace();
			}
		}
	}

	/**
	 * saves the tab content
	 * 
	 * @param tab
	 *            {@link JTabPanel}
	 */
	public void saveTab(JTabPanel tab) {
		JTabContent content;
		if (tab != null) {
			content = tab.getTabContent();
		} else {
			int index = frame.getWorkspacePane().getSelectedIndex();
			content = (JTabContent) frame.getWorkspacePane().getComponentAt(
					index);
		}

		if(!content.saveContent()){
			saveTabAs(tab);
		}
	}

	/**
	 * saves the tab as...
	 * 
	 * @param tab
	 *            {@link JTabPanel}
	 */
	public void saveTabAs(JTabPanel tab) {
		JTabContent content;
		if (tab != null) {
			content = tab.getTabContent();
		} else {
			int index = frame.getWorkspacePane().getSelectedIndex();
			content = (JTabContent) frame.getWorkspacePane().getComponentAt(
					index);
		}

		NewFileDialog nfd = new NewFileDialog();
		nfd.setVisible(true);
		if (nfd.getStatus() == NewFileDialog.APPROVE_OPTION) {
			try {
				File f = FileUtils.createNewFile(nfd.getFileDirectory()
						+ System.getProperty("file.separator")
						+ nfd.getFileName());
				if (f == null) {
					// TODO overwrite file?
				} else {
					content.saveContent(f);
					if (content.getFile() == null) {
						tab.getLabel().setText(content.getName());
						content.setFile(f);
					}
				}
			} catch (IOException e) {
				LOG.severe("KH: cannot create new file!");
				e.printStackTrace();
			}
		}

	}

	/**
	 * selects all content in the selected tab
	 */
	public void selectAll() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.selectAll();
	}

	/**
	 * show application preferences window
	 */
	public void showPreferences() {

	}

	/**
	 * shows the project properties window
	 */
	public void showProjectProperties() {

	}

	/**
	 * 
	 */
	public void startWorkflowGraphExecution() {
		IEngineGraphConfiguration engConfig = new EngineGraphConfiguration();
		StringWriter w = new StringWriter();
		IWorkflowWizardDisplay wizardDisplay;
		IWorkflowExecution execution = new WorkflowExecution();
		execution.addWorkflowExecutionListener(new WorkflowExecutionListener() {
			
			@Override
			public void workflowSubmissionCompleted(Integer workflowId) {
				// TODO Auto-generated method stub
				
			}
		});

		if (project != null) {
			try {
				wizardDisplay = new WorkflowWizardDisplay(frame,
						"Send Workflow To Execution", project);
				if (wizardDisplay.displayWizard() == IWorkflowWizardDisplay.WIZARD_FINISH_RETURN_CODE) {
					engConfig.setProjectName(project.getProjectName());
					engConfig.setInputDataURL(wizardDisplay.getInputDataUrl().toExternalForm());
					engConfig.saveGraphForEngine(GraphNodeDecoratorConverter
							.convertGuiToEngine(project.getProjectNodes()), w);
					byte[] graphStream = w.getBuffer().toString()
							.getBytes("utf-8");					
					execution.setUsername(wizardDisplay.getUsername());
					execution.setPassword(wizardDisplay.getPassword());
					execution.setSerializedGraphStream(graphStream);
					execution.submitForExecution();
				}
			} catch (WorkflowWizardDisplayException e) {
				MessageDialog.showErrorDialog(frame, "Error",
						"Error occured in displaying workflow wizard!");
				e.printStackTrace();
			} catch (GraphNodeDecoratorConverterException e) {
				MessageDialog.showErrorDialog(frame, "Error",
						"Could not convert the graph node with id: "
								+ e.getNodeDecorator().getGraphNode()
										.getNodeId()
								+ " to the format required by engine!");
				e.printStackTrace();
			} catch (ConfigurationException e) {
				MessageDialog.showErrorDialog(frame, "Error",
						"Error saving graph configuration to stream!");
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				MessageDialog
						.showErrorDialog(frame, "Error",
								"The graph stream could not be created - unsupported encoding: 'utf-8'");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	public void stopWorkflowGraphExecution() {
		// TODO Auto-generated method stub
		// stop the last started execution
	}

	/**
	 * undoes the last action on the selected tab
	 */
	public void undoAction() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.undoAction();
	}
}