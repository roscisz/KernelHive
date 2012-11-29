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
import pl.gda.pg.eti.kernelhive.common.graph.node.util.GraphNodeDecoratorConverter;
import pl.gda.pg.eti.kernelhive.common.graph.node.util.GraphNodeDecoratorConverterException;
import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.component.JTabPanel;
import pl.gda.pg.eti.kernelhive.gui.component.infrastructure.InfrastructureBrowser;
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
import pl.gda.pg.eti.kernelhive.gui.dialog.FindReplaceDialog;
import pl.gda.pg.eti.kernelhive.gui.dialog.GoToLineDialog;
import pl.gda.pg.eti.kernelhive.gui.dialog.MessageDialog;
import pl.gda.pg.eti.kernelhive.gui.dialog.NewFileDialog;
import pl.gda.pg.eti.kernelhive.gui.dialog.NewProjectDialog;
import pl.gda.pg.eti.kernelhive.gui.dialog.PreferencesDialog;
import pl.gda.pg.eti.kernelhive.gui.dialog.ProjectPropertiesDialog;
import pl.gda.pg.eti.kernelhive.gui.project.IProject;
import pl.gda.pg.eti.kernelhive.gui.project.impl.KernelHiveProject;
import pl.gda.pg.eti.kernelhive.gui.workflow.execution.IWorkflowExecution;
import pl.gda.pg.eti.kernelhive.gui.workflow.execution.WorkflowExecution;
import pl.gda.pg.eti.kernelhive.gui.workflow.execution.WorkflowExecutionListener;
import pl.gda.pg.eti.kernelhive.gui.workflow.wizard.IWorkflowWizardDisplay;
import pl.gda.pg.eti.kernelhive.gui.workflow.wizard.WorkflowWizardDisplay;
import pl.gda.pg.eti.kernelhive.gui.workflow.wizard.WorkflowWizardDisplayException;
import pl.gda.pg.eti.kernelhive.repository.kernel.repository.IKernelRepository;
import pl.gda.pg.eti.kernelhive.repository.kernel.repository.KernelRepositoryException;
import pl.gda.pg.eti.kernelhive.repository.loader.RepositoryLoaderService;

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

	private final MainFrame frame;
	private IProject project;
	private int newFileCounter;

	/**
	 * constructor
	 * 
	 * @param frame
	 *            {@link MainFrame}
	 */
	public MainFrameController(final MainFrame frame) {
		this.frame = frame;
		newFileCounter = 1;
	}

	private void addTabToWorkspacePane(final JTabContent tab) {
		frame.getWorkspacePane().addTab(tab.getName(), tab);
		final int index = frame.getWorkspacePane().getTabCount() - 1;
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

		while (frame.getWorkspacePane().getTabCount() > 0) {
			final Component c = frame.getWorkspacePane().getTabComponentAt(0);
			if (c instanceof JTabPanel) {
				closeTab((JTabPanel) c);
			}
		}

		frame.getBtnStart().setEnabled(false);
	}

	/**
	 * closes the selected tab
	 * 
	 * @param tab
	 *            {@link JTabPanel}
	 */
	public void closeTab(final JTabPanel tab) {
		JTabContent content;
		int index = -1;
		if (tab != null) {
			content = tab.getTabContent();
			for (int i = 0; i < frame.getWorkspacePane().getTabCount(); i++) {
				final Component c = frame.getWorkspacePane().getTabComponentAt(
						i);
				if (c.equals(tab)) {
					index = i;
					break;
				}
			}
		} else {
			index = frame.getWorkspacePane().getSelectedIndex();
			if (index != -1) {
				content = (JTabContent) frame.getWorkspacePane()
						.getComponentAt(index);
			} else {
				return;
			}
		}
		// TODO i18n
		if (content.isDirty()) {
			final int result = JOptionPane.showConfirmDialog(
					frame.getContentPane(),
					"The selected file has been modified. "
							+ "Do you wanto to save it?", "Save file?",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				final File f = content.getFile();
				if (f != null && f.exists()) {
					content.saveContent(f);
				} else {
					saveTabAs(tab);
				}
				frame.getWorkspacePane().removeTabAt(index);
			} else if (result == JOptionPane.NO_OPTION) {
				frame.getWorkspacePane().removeTabAt(index);
			} else if (result == JOptionPane.CANCEL_OPTION) {
			}
		} else {
			frame.getWorkspacePane().removeTabAt(index);
		}
	}

	/**
	 * copies content from the selected tab
	 */
	public void copy() {
		final JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.copy();
	}

	/**
	 * cuts content from the selected tab
	 */
	public void cut() {
		final JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.cut();
	}

	/**
	 * controls the display of the side panel component
	 * 
	 * @param visible
	 */
	public void displaySidePanel(final boolean visible) {
		// FIXME
		// frame.getSidePane().setVisible(visible);
	}

	/**
	 * controls the display of the statusbar component
	 * 
	 * @param visible
	 *            boolean
	 */
	public void displayStatusbar(final boolean visible) {
		frame.getStatusbar().setVisible(visible);
	}

	/**
	 * controls the display of the toolbox component
	 * 
	 * @param visible
	 *            boolean
	 */
	public void displayToolbox(final boolean visible) {
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

		final NewProjectDialog npd = new NewProjectDialog();
		npd.setVisible(true);
		if (npd.getStatus() == NewProjectDialog.APPROVE_OPTION) {
			try {
				final File projectDir = new File(npd.getProjectDirectory()
						+ System.getProperty("file.separator")
						+ npd.getProjectName());
				project = new KernelHiveProject(projectDir,
						npd.getProjectName());
				project.initProject();
				frame.setTitle(npd.getProjectName() + " - "
						+ BUNDLE.getString("MainFrame.this.title"));

				final FileTreeModel model = new FileTreeModel(
						project.getProjectDirectory());
				final FileTree tree = new FileTree(frame, model);
				tree.setCellRenderer(new FileCellRenderer(tree
						.getCellRenderer()));
				frame.setProjectTree(tree);
				frame.getProjectScrollPane().setViewportView(
						frame.getProjectTree());

				final IKernelRepository repository = RepositoryLoaderService
						.getInstance().getRepository();
				final ListModel repoModel = new RepositoryViewerModel(
						repository.getEntries());
				frame.setRepositoryList(new RepositoryViewer(repoModel));
				frame.getRepositoryScrollPane().setViewportView(
						frame.getRepositoryList());

				frame.getBtnStart().setEnabled(true);

				openWorkflowEditor();

			} catch (final ConfigurationException e) {
				LOG.warning("KH: cannot create new project");
				MessageDialog
						.showErrorDialog(
								frame,
								BUNDLE.getString("MainFrameController.newProject.cannotCreate.title"),
								BUNDLE.getString("MainFrameController.newProject.cannotCreate.text"));
				e.printStackTrace();
			} catch (final KernelRepositoryException e) {
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
		File file = null;
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setMultiSelectionEnabled(false);
		final FileFilter ff = new FileNameExtensionFilter("xml", "xml");
		fc.setFileFilter(ff);
		if (fc.showDialog(frame.getContentPane(), "Select") == JFileChooser.APPROVE_OPTION) {
			try {
				file = fc.getSelectedFile();
				project = new KernelHiveProject(file.getParentFile(), null);
				project.setProjectFile(file);
				project.load();
				final FileTreeModel model = new FileTreeModel(
						project.getProjectDirectory());
				final FileTree tree = new FileTree(frame, model);
				tree.setCellRenderer(new FileCellRenderer(tree
						.getCellRenderer()));
				frame.setProjectTree(tree);
				frame.getProjectScrollPane().setViewportView(
						frame.getProjectTree());

				final IKernelRepository repository = RepositoryLoaderService
						.getInstance().getRepository();
				final ListModel repoModel = new RepositoryViewerModel(
						repository.getEntries());
				frame.setRepositoryList(new RepositoryViewer(repoModel));
				frame.getRepositoryScrollPane().setViewportView(
						frame.getRepositoryList());

				frame.getBtnStart().setEnabled(true);

				openWorkflowEditor();

			} catch (final ConfigurationException e) {
				MessageDialog.showErrorDialog(
						frame,
						"Error",
						"Could not open the project file: "
								+ file.getAbsolutePath() + " Reason: "
								+ e.getMessage());
			} catch (final KernelRepositoryException e) {
				MessageDialog.showErrorDialog(
						frame,
						"Error",
						"Could not open the project file: "
								+ file.getAbsolutePath() + " Reason: "
								+ e.getMessage());
			}
		}
	}

	/**
	 * opens new Tab and associates it with the given {@link File}
	 * 
	 * @param f
	 *            {@link File}
	 */
	public void openTab(final File f) {
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
			final WorkflowEditor editor = new WorkflowEditor(
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
		final WorkflowViewer wv = new WorkflowViewer(frame, "Workflow Viewer");
		addTabToWorkspacePane(wv);
	}

	/**
	 * pastes copied/cut content to the selected tab
	 */
	public void paste() {
		final JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.paste();
	}

	/**
	 * 
	 */
	public void pauseWorkflowGraphExecution() {
	}

	/**
	 * redoes the last action on the selected tab
	 */
	public void redoAction() {
		final JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
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
		final Component[] tabContents = this.frame.getWorkspacePane()
				.getComponents();
		for (final Component c : tabContents) {
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
			final Component c = frame.getWorkspacePane().getTabComponentAt(i);
			if (c instanceof JTabPanel) {
				saveTab((JTabPanel) c);
			}
		}
	}

	/**
	 * saves project
	 */
	public void saveProject() {
		if (project != null) {
			try {
				project.save();
			} catch (final ConfigurationException e) {
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
	public void saveTab(final JTabPanel tab) {
		JTabContent content;
		if (tab != null) {
			content = tab.getTabContent();
		} else {
			final int index = frame.getWorkspacePane().getSelectedIndex();
			content = (JTabContent) frame.getWorkspacePane().getComponentAt(
					index);
		}

		if (!content.saveContent()) {
			saveTabAs(tab);
		}
	}

	/**
	 * saves the tab as...
	 * 
	 * @param tab
	 *            {@link JTabPanel}
	 */
	public void saveTabAs(final JTabPanel tab) {
		JTabContent content;
		if (tab != null) {
			content = tab.getTabContent();
		} else {
			final int index = frame.getWorkspacePane().getSelectedIndex();
			content = (JTabContent) frame.getWorkspacePane().getComponentAt(
					index);
		}

		final NewFileDialog nfd = new NewFileDialog();
		nfd.setVisible(true);
		if (nfd.getStatus() == NewFileDialog.APPROVE_OPTION) {
			try {
				final File f = FileUtils.createNewFile(nfd.getFileDirectory()
						+ System.getProperty("file.separator")
						+ nfd.getFileName());
				if (f == null) {
					MessageDialog.showErrorDialog(
							frame,
							"Error",
							"The file: " + nfd.getFileDirectory()
									+ System.getProperty("file.separator")
									+ nfd.getFileName() + " already exists");
				} else {
					content.saveContent(f);
					if (content.getFile() == null) {
						tab.getLabel().setText(content.getName());
						content.setFile(f);
					}
				}
			} catch (final IOException e) {
				LOG.severe("KH: cannot create new file!");
				e.printStackTrace();
			}
		}
	}

	/**
	 * selects all content in the selected tab
	 */
	public void selectAll() {
		final JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.selectAll();
	}

	/**
	 * show application preferences window
	 */
	public void showPreferences() {
		final PreferencesDialog dialog = new PreferencesDialog(frame);
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	/**
	 * shows the project properties window
	 */
	public void showProjectProperties() {
		if (project != null) {
			final ProjectPropertiesDialog dialog = new ProjectPropertiesDialog(
					frame, project);
			dialog.setModal(true);
			dialog.setVisible(true);
		}
	}

	/**
	 * 
	 */
	public void startWorkflowGraphExecution() {
		final IEngineGraphConfiguration engConfig = new EngineGraphConfiguration();
		final StringWriter w = new StringWriter();
		IWorkflowWizardDisplay wizardDisplay;
		final IWorkflowExecution execution = new WorkflowExecution();
		execution.addWorkflowExecutionListener(new WorkflowExecutionListener() {

			@Override
			public void workflowSubmissionCompleted(final Integer workflowId) {
				JOptionPane.showMessageDialog(frame,
						"Workflow sent to execution. Workflow ID is "
								+ workflowId, "Success",
						JOptionPane.INFORMATION_MESSAGE, null);

				frame.getBtnPause().setEnabled(true);
				frame.getBtnStop().setEnabled(true);
			}
		});

		if (project != null) {
			try {
				wizardDisplay = new WorkflowWizardDisplay(frame,
						"Send Workflow To Execution", project);
				if (wizardDisplay.displayWizard() == IWorkflowWizardDisplay.WIZARD_FINISH_RETURN_CODE) {
					engConfig.setProjectName(project.getProjectName());
					engConfig.setInputDataURL(wizardDisplay.getInputDataUrl()
							.toExternalForm());
					engConfig.saveGraphForEngine(GraphNodeDecoratorConverter
							.convertGuiToEngine(project.getProjectNodes()), w);
					final byte[] graphStream = w.getBuffer().toString()
							.getBytes("utf-8");
					execution.setUsername(wizardDisplay.getUsername());
					execution.setPassword(wizardDisplay.getPassword());
					execution.setSerializedGraphStream(graphStream);
					execution.submitForExecution();
				}
			} catch (final WorkflowWizardDisplayException e) {
				MessageDialog.showErrorDialog(frame, "Error",
						"Error occured in displaying workflow wizard!");
				e.printStackTrace();
			} catch (final GraphNodeDecoratorConverterException e) {
				MessageDialog.showErrorDialog(frame, "Error",
						"Could not convert the graph node with id: "
								+ e.getNodeDecorator().getGraphNode()
										.getNodeId()
								+ " to the format required by engine!");
				e.printStackTrace();
			} catch (final ConfigurationException e) {
				MessageDialog.showErrorDialog(frame, "Error",
						"Error saving graph configuration to stream!");
				e.printStackTrace();
			} catch (final UnsupportedEncodingException e) {
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
		final JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.undoAction();
	}

	public void openInfrastructureBrowser() {
		final InfrastructureBrowser ib = new InfrastructureBrowser(frame,
				"Infrastructure Browser");
		addTabToWorkspacePane(ib);

	}

	public void openResourceMonitor() {
		// TODO Auto-generated method stub
	}

	public void loginUser() {
		// TODO Auto-generated method stub

	}

	public void clearHighlight() {
		final JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.clearSelection();
	}

	public void openGoToLineDialog() {
		final GoToLineDialog dialog = new GoToLineDialog(frame);
		dialog.setModal(false);
		dialog.setVisible(true);
	}

	public void openAboutDialog() {
		// TODO Auto-generated method stub

	}

	public void logoutUser() {
		// TODO Auto-generated method stub

	}

	public void deleteSelected() {
		// TODO Auto-generated method stub

	}

	public void openFindReplaceDialog() {
		final FindReplaceDialog dialog = new FindReplaceDialog(frame);
		dialog.setModal(false);
		dialog.setVisible(true);
	}
}