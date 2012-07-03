package pl.gda.pg.eti.kernelhive.gui.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.component.JTabPanel;
import pl.gda.pg.eti.kernelhive.gui.component.source.SourceCodeEditor;
import pl.gda.pg.eti.kernelhive.gui.component.source.SourceCodeEditor.SyntaxStyle;
import pl.gda.pg.eti.kernelhive.gui.component.tree.FileCellRenderer;
import pl.gda.pg.eti.kernelhive.gui.component.tree.FileTree;
import pl.gda.pg.eti.kernelhive.gui.component.tree.FileTreeModel;
import pl.gda.pg.eti.kernelhive.gui.component.workflow.WorkflowEditor;
import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;
import pl.gda.pg.eti.kernelhive.gui.file.FileUtils;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.frame.NewFileDialog;
import pl.gda.pg.eti.kernelhive.gui.frame.NewProjectDialog;
import pl.gda.pg.eti.kernelhive.gui.project.IProject;
import pl.gda.pg.eti.kernelhive.gui.project.KernelHiveProject;

public class MainFrameController {

	private static final Logger LOG = Logger
			.getLogger(MainFrameController.class.getName());
	private static ResourceBundle BUNDLE = AppConfiguration.getInstance()
			.getLanguageResourceBundle();

	private MainFrame frame;
	private IProject project;
	private HashMap<JTabContent, File> openedTabs;
	private int newFileCounter;

	public MainFrameController(MainFrame frame) {
		this.frame = frame;
		newFileCounter = 1;
		openedTabs = new HashMap<JTabContent, File>();
	}

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

	public void openProject() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setMultiSelectionEnabled(false);
		FileFilter ff = new FileNameExtensionFilter("xml", "xml");
		fc.setFileFilter(ff);
		if (fc.showDialog(frame.getContentPane(), "Select") == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			project = new KernelHiveProject(file.getParentFile(), null);
			project.setProjectFile(file);
			project.load();
			FileTreeModel model = new FileTreeModel(
					project.getProjectDirectory());
			FileTree tree = new FileTree(frame, model);
			tree.setCellRenderer(new FileCellRenderer(tree.getCellRenderer()));
			frame.setProjectTree(tree);
			frame.getProjectScrollPane()
					.setViewportView(frame.getProjectTree());
			openWorkflowEditor();
		}
	}

	public void closeProject() {
		saveAll();
		saveProject();
	}

	public void saveProject() {
		try {
			project.save();
		} catch (ConfigurationException e) {
			LOG.warning("KH: could not save project");
			e.printStackTrace();
		}
	}

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
				File f = openedTabs.get(content);
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

	public void openTab(File f) {
		if (f != null) {
			SourceCodeEditor sourcePanel = new SourceCodeEditor(frame,
					f.getName());
			sourcePanel.loadContent(f);
			sourcePanel.setSyntaxStyle(SyntaxStyle.resolveSyntaxStyle(f
					.getName().substring(f.getName().indexOf(".") + 1,
							f.getName().length())));
			frame.getWorkspacePane().add(sourcePanel, 0);
			frame.getWorkspacePane().setTabComponentAt(0,
					new JTabPanel(sourcePanel));
			openedTabs.put(sourcePanel, f);
		} else {
			SourceCodeEditor sourcePanel = new SourceCodeEditor(frame, "new"
					+ newFileCounter);
			newFileCounter++;
			frame.getWorkspacePane().add(sourcePanel, 0);
			frame.getWorkspacePane().setTabComponentAt(0,
					new JTabPanel(sourcePanel));
			openedTabs.put(sourcePanel, null);
		}
	}

	public void saveTab(JTabPanel tab) {
		JTabContent content;
		if (tab != null) {
			content = tab.getTabContent();
		} else {
			int index = frame.getWorkspacePane().getSelectedIndex();
			content = (JTabContent) frame.getWorkspacePane().getComponentAt(
					index);
		}

		File f = openedTabs.get(content);
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
					if (openedTabs.get(content) == null) {
						tab.getLabel().setText(content.getName());
						openedTabs.put(content, f);
					}
				}
			} catch (IOException e) {
				LOG.severe("KH: cannot create new file!");
				e.printStackTrace();
			}
		}

	}

	public void saveAll() {

	}

	public void refresh() {

	}

	public void exit() {
		saveAll();
		saveProject();
		frame.dispose();
	}

	public void showPreferences() {

	}

	public void displayToolbox(boolean visible) {
		frame.getToolBar().setVisible(visible);
	}

	public void displayStatusbar(boolean visible) {
		frame.getStatusbar().setVisible(visible);
	}

	public void displaySidePanel(boolean visible) {
		// FIXME
		// frame.getSidePane().setVisible(visible);
	}

	public void showProjectProperties() {

	}

	public void openWorkflowEditor() {
		if (project != null) {
			WorkflowEditor editor = new WorkflowEditor(
					frame,
					BUNDLE.getString("MainFrameController.openWorkflowEditor.workflowEditor.text"),
					project);
			frame.getWorkspacePane().add(editor, 0);
			JTabPanel tabControl = new JTabPanel(editor);
			openedTabs.put(editor, project.getProjectFile());
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

	public void undoAction() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.undoAction();
	}

	public void redoAction() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.redoAction();
	}

	public void cut() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.cut();
	}

	public void copy() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.copy();
	}

	public void paste() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.paste();
	}

	public void selectAll() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.selectAll();
	}
}