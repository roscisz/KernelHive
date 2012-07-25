package pl.gda.pg.eti.kernelhive.gui.controller;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.clientService.ClientBean;
import pl.gda.pg.eti.kernelhive.common.clientService.ClientBeanService;
import pl.gda.pg.eti.kernelhive.common.file.FileUtils;
import pl.gda.pg.eti.kernelhive.common.graph.configuration.IEngineGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.configuration.impl.EngineGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeDecoratorConverter;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeDecoratorConverterException;
import pl.gda.pg.eti.kernelhive.common.kernel.repository.IKernelRepository;
import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRepositoryEntry;
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
import pl.gda.pg.eti.kernelhive.gui.component.workflow.WorkflowEditor;
import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.frame.NewFileDialog;
import pl.gda.pg.eti.kernelhive.gui.frame.NewProjectDialog;
import pl.gda.pg.eti.kernelhive.gui.frame.wizard.Wizard;
import pl.gda.pg.eti.kernelhive.gui.frame.wizard.WizardPanelDescriptor;
import pl.gda.pg.eti.kernelhive.gui.frame.wizard.WizardPanelNotFoundException;
import pl.gda.pg.eti.kernelhive.gui.frame.wizard.workflow.GraphValidationPanelDescriptor;
import pl.gda.pg.eti.kernelhive.gui.frame.wizard.workflow.InputDataPanelDescriptor;
import pl.gda.pg.eti.kernelhive.gui.frame.wizard.workflow.UserCredentialsPanelDescriptor;
import pl.gda.pg.eti.kernelhive.gui.project.IProject;
import pl.gda.pg.eti.kernelhive.gui.project.impl.KernelHiveProject;

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
	private HashMap<JTabContent, File> openedTabs;
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
		openedTabs = new HashMap<JTabContent, File>();
	}

	/**
	 * closes project
	 */
	public void closeProject() {
		saveAll();
		saveProject();
	}

	/**
	 * closes the selected tab
	 * 
	 * @param tab
	 *            {@link JTabPanel}
	 */
	public void closeTab(JTabPanel tab) {
		JTabContent content;
		if (tab != null) {
			content = tab.getTabContent();
		} else {
			int index = frame.getWorkspacePane().getSelectedIndex();
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
				// File f = openedTabs.get(content);
				if (f != null && f.exists()) {
					content.saveContent(f);
				} else {
					NewFileDialog nfd = new NewFileDialog();
					nfd.setVisible(true);
					if (nfd.getStatus() == NewFileDialog.APPROVE_OPTION) {
						try {
							f = FileUtils.createNewFile(nfd.getFileDirectory()
									+ System.getProperty("file.separator")
									+ nfd.getFileName());
							content.saveContent(f);
						} catch (IOException e) {
							LOG.severe("KH: cannot create new file!");
							e.printStackTrace();
						}
					}
				}
				frame.getWorkspacePane().remove(content);
			} else if (result == JOptionPane.NO_OPTION) {
				frame.getWorkspacePane().remove(content);
				openedTabs.remove(content);
			} else if (result == JOptionPane.CANCEL_OPTION) {

			}
		} else {
			frame.getWorkspacePane().remove(content);
			openedTabs.remove(content);
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
				ListModel<KernelRepositoryEntry> repoModel = new RepositoryViewerModel(
						repository.getEntries());
				frame.setRepositoryList(new RepositoryViewer(repoModel));
				frame.getRepositoryScrollPane().setViewportView(
						frame.getRepositoryList());

			} catch (ConfigurationException e) {
				LOG.warning("KH: cannot create new project");
				JOptionPane
						.showMessageDialog(
								frame,
								BUNDLE.getString("MainFrameController.newProject.cannotCreate.text"),
								BUNDLE.getString("MainFrameController.newProject.cannotCreate.title"),
								JOptionPane.ERROR_MESSAGE);
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
				ListModel<KernelRepositoryEntry> repoModel = new RepositoryViewerModel(
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
		frame.getWorkspacePane().addTab(sourcePanel.getName(), sourcePanel);
		frame.getWorkspacePane().setTabComponentAt(0,
				new JTabPanel(sourcePanel));
		int index = frame.getWorkspacePane().getTabCount() - 1;
		frame.getWorkspacePane().setTabComponentAt(index,
				new JTabPanel(sourcePanel));
		frame.getWorkspacePane().getModel().setSelectedIndex(index);
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
			frame.getWorkspacePane().add(editor, 0);
			JTabPanel tabControl = new JTabPanel(editor);
			editor.setFile(project.getProjectFile());
			frame.getWorkspacePane().setTabComponentAt(0, tabControl);
		} else {
			JOptionPane
					.showMessageDialog(
							frame,
							BUNDLE.getString("MainFrameController.openWorkflowEditor.error.text"),
							BUNDLE.getString("MainFrameController.openWorkflowEditor.error.title"),
							JOptionPane.ERROR_MESSAGE);
		}
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
		Set<JTabContent> tabs = this.openedTabs.keySet();
		for (JTabContent tab : tabs) {
			tab.refresh();
		}
	}

	/**
	 * saves all tabs
	 */
	public void saveAll() {
		for(int i=0; i<frame.getWorkspacePane().getTabCount(); i++){
			Component c = frame.getWorkspacePane().getTabComponentAt(i);
			if(c instanceof JTabPanel){
				JTabContent tc = ((JTabPanel)c).getTabContent();
				if(tc.getFile()!=null){
					tc.saveContent(tc.getFile());
				}
			}
		}
	}

	/**
	 * saves project
	 */
	public void saveProject() {
		try {
			project.save();
		} catch (ConfigurationException e) {
			LOG.warning("KH: could not save project");
			e.printStackTrace();
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

		File f = content.getFile();
		if (f != null && f.exists()) {
			content.saveContent(f);
		} else {
			NewFileDialog nfd = new NewFileDialog();
			nfd.setVisible(true);
			if (nfd.getStatus() == NewFileDialog.APPROVE_OPTION) {
				try {
					f = FileUtils.createNewFile(nfd.getFileDirectory()
							+ System.getProperty("file.separator")
							+ nfd.getFileName());
					if (f == null) {
						// TODO overwrite file?
					} else {
						content.setFile(f);
						content.saveContent(f);
						tab.getLabel().setText(content.getName());
						openedTabs.put(content, f);
					}
				} catch (IOException e) {
					LOG.severe("KH: cannot create new file!");
					e.printStackTrace();
				}
			}
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
		if(project!=null){
			// TODO
			Wizard wizard = new Wizard(frame);
			wizard.getDialog().setTitle("Send Workflow To Execution");
			WizardPanelDescriptor desc1 = new GraphValidationPanelDescriptor(project);
			wizard.registerWizardPanel(GraphValidationPanelDescriptor.IDENTIFIER, desc1);
			WizardPanelDescriptor desc2 = new InputDataPanelDescriptor();
			wizard.registerWizardPanel(InputDataPanelDescriptor.IDENTIFIER, desc2);
			WizardPanelDescriptor desc3 = new UserCredentialsPanelDescriptor();
			wizard.registerWizardPanel(UserCredentialsPanelDescriptor.IDENTIFIER, desc3);
			try {
				wizard.setCurrentPanel(GraphValidationPanelDescriptor.IDENTIFIER);
			} catch (WizardPanelNotFoundException e) {
				// TODO handle
				e.printStackTrace();
				return;
			}
			int ret = wizard.showNonModalDialog();
			if(ret==Wizard.FINISH_RETURN_CODE){
				IEngineGraphConfiguration engConfig = new EngineGraphConfiguration();
				StringWriter w = new StringWriter();
				List<EngineGraphNodeDecorator> engineNodes = new ArrayList<EngineGraphNodeDecorator>();
				for(GUIGraphNodeDecorator g : project.getProjectNodes()){
					try {
						engineNodes.add(GraphNodeDecoratorConverter.convertGuiToEngine(g));
					} catch (GraphNodeDecoratorConverterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
				}
				try {
					engConfig.saveGraphForEngine(engineNodes, w);
					String engineGraphString = w.getBuffer().toString();
					
					ClientBean clientBean = new ClientBeanService().getClientBeanPort();
					clientBean.runGraph(engineGraphString);
				} catch (ConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				// save graph nodes and data url to engine xml format
				// send xml as bytes[] or String via WS (+authorization with user/pass
				// pair)
				// receive response after send
				// display response
			}		
		}
	}

	/**
	 * 
	 */
	public void stopWorkflowGraphExecution() {
		// TODO Auto-generated method stub
		//stop the last started execution
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