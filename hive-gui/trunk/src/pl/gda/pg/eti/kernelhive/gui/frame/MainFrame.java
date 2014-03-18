/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.gui.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -2018750030834289098L;
	private static ResourceBundle BUNDLE = AppConfiguration.getInstance()
			.getLanguageResourceBundle();
	private static Logger LOG = Logger.getLogger(MainFrame.class.getName());

	private JPanel contentPane;
	private JMenuBar mainMenuBar;
	private JMenu mnFile;
	private JMenuItem mntmNew;
	private JMenuItem mntmOpen;
	private JMenuItem mntmClose;
	private JMenuItem mntmCloseAll;
	private JMenuItem mntmSave;
	private JMenuItem mntmSaveAs;
	private JMenuItem mntmSaveAll;
	private JMenuItem mntmRefresh;
	private JMenuItem mntmLogin;
	private JMenuItem mntmLogout;
	private JMenuItem mntmExit;
	private JMenu mnEdit;
	private JMenuItem mntmUndo;
	private JMenuItem mntmRedo;
	private JMenuItem mntmCut;
	private JMenuItem mntmCopy;
	private JMenuItem mntmPaste;
	private JMenuItem mntmDelete;
	private JMenuItem mntmSelectAll;
	private JMenu mnView;
	private JCheckBoxMenuItem chckbxmntmToolbox;
	private JCheckBoxMenuItem chckbxmntmStatusbar;
	private JCheckBoxMenuItem chckbxmntmSidePanel;
	private JMenuItem mntmFullscreen;
	private JMenu mnSearch;
	private JMenuItem mntmFindreplace;
	private JMenuItem mntmFindNext;
	private JMenuItem mntmFindPrevious;
	private JMenuItem mntmClearHighlight;
	private JMenuItem mntmGoToLine;
	private JMenu mnProject;
	private JMenu mnTools;
	private JMenu mnHelp;
	private JMenuItem mntmContents;
	private JMenuItem mntmAbout;
	private JToolBar toolBar;
	private JPanel statusbar;
	private JSplitPane centerPane;
	private JTabbedPane workspacePane;
	private JTabbedPane sidePane;
	private JPanel projectPanel;
	private JPanel repositoryPanel;
	private JSeparator separator;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JSeparator separator_3;
	private JSeparator separator_4;
	private JSeparator separator_5;
	private JSeparator separator_6;
	private JSeparator separator_7;
	private JSeparator separator_8;
	private JSeparator separator_9;
	private JSeparator separator_10;
	private JSeparator separator_11;
	private JMenuItem mntmProperties;
	private JButton btnStart;
	private JButton btnPause;
	private JButton btnStop;
	private JMenuItem mntmPreferences;
	private JTree projectTree;
	private JList repositoryList;
	private JScrollPane projectScrollPane;
	private JMenuItem mntmWorkflowEditor;
	private JMenuItem mntmWorkflowExecutions;
	private JScrollPane repositoryScrollPane;

	private MainFrameController controller;
	private JMenuItem mntmInfrastructureBrowser;
	private JMenuItem mntmResourceMonitor;

	private void initUI() {
		setTitle(BUNDLE.getString("MainFrame.this.title"));
		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 768);

		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(800, 600));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		centerPane = new JSplitPane();
		contentPane.add(centerPane, BorderLayout.CENTER);
		centerPane.setDividerLocation(200);

		initMenu();
		initStatusbar();
		initSidePane();
		initToolbar();
		initWorkspace();
	}

	private void initFileMenu() {
		mnFile = new JMenu(BUNDLE.getString("MainFrame.mnFile.text"));
		mnFile.setMnemonic(KeyEvent.VK_F);
		mainMenuBar.add(mnFile);

		mntmNew = new JMenuItem(BUNDLE.getString("MainFrame.mntmNew.text"));
		mntmNew.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/New16.gif")));
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_DOWN_MASK));
		mntmNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.newProject();
			}
		});
		mnFile.add(mntmNew);

		mntmOpen = new JMenuItem(BUNDLE.getString("MainFrame.mntmOpen.text"));
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_MASK));
		mntmOpen.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/Open16.gif")));
		mntmOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.openProject();
			}
		});
		mnFile.add(mntmOpen);

		separator = new JSeparator();
		mnFile.add(separator);

		mntmClose = new JMenuItem(BUNDLE.getString("MainFrame.mntmClose.text"));
		mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
				InputEvent.CTRL_MASK));
		mntmClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.closeTab(null);
			}
		});
		mnFile.add(mntmClose);

		mntmCloseAll = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmCloseAll.text"));
		mntmCloseAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.closeProject();
			}
		});
		mnFile.add(mntmCloseAll);

		separator_1 = new JSeparator();
		mnFile.add(separator_1);

		mntmSave = new JMenuItem(BUNDLE.getString("MainFrame.mntmSave.text"));
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		mntmSave.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/Save16.gif")));
		mntmSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.saveTab(null);

			}
		});
		mnFile.add(mntmSave);

		mntmSaveAs = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmSaveAs.text"));
		mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmSaveAs.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/SaveAs16.gif")));
		mntmSaveAs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.saveTabAs(null);
			}
		});
		mnFile.add(mntmSaveAs);

		mntmSaveAll = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmSaveAll.text"));
		mntmSaveAll.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/SaveAll16.gif")));
		mntmSaveAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.saveAll();
			}
		});
		mnFile.add(mntmSaveAll);

		separator_2 = new JSeparator();
		mnFile.add(separator_2);

		mntmRefresh = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmRefresh.text"));
		mntmRefresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				InputEvent.CTRL_MASK));
		mntmRefresh.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/Refresh16.gif")));
		mntmRefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.refresh();
			}
		});
		mnFile.add(mntmRefresh);

		separator_3 = new JSeparator();
		mnFile.add(separator_3);

		mntmLogin = new JMenuItem(BUNDLE.getString("MainFrame.mntmLogin.text"));
		mntmLogin.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/Import16.gif")));
		mntmLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.loginUser();
			}
		});
		mnFile.add(mntmLogin);

		mntmLogout = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmLogout.text"));
		mntmLogout.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/Export16.gif")));
		mntmLogout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.logoutUser();
			}
		});
		mnFile.add(mntmLogout);

		separator_4 = new JSeparator();
		mnFile.add(separator_4);

		mntmExit = new JMenuItem(BUNDLE.getString("MainFrame.mntmExit.text"));
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				InputEvent.CTRL_MASK));
		mntmExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.exit();
			}
		});
		mnFile.add(mntmExit);

	}

	private void initEditMenu() {
		mnEdit = new JMenu(BUNDLE.getString("MainFrame.mnEdit.text"));
		mnEdit.setMnemonic(KeyEvent.VK_E);
		mainMenuBar.add(mnEdit);

		mntmUndo = new JMenuItem(BUNDLE.getString("MainFrame.mntmUndo.text"));
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				InputEvent.CTRL_MASK));
		mntmUndo.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/Undo16.gif")));
		mntmUndo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.undoAction();

			}
		});
		mnEdit.add(mntmUndo);

		mntmRedo = new JMenuItem(BUNDLE.getString("MainFrame.mntmRedo.text"));
		mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmRedo.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/Redo16.gif")));
		mntmRedo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.redoAction();
			}
		});
		mnEdit.add(mntmRedo);

		separator_5 = new JSeparator();
		mnEdit.add(separator_5);

		mntmCut = new JMenuItem(BUNDLE.getString("MainFrame.mntmCut.text"));
		mntmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				InputEvent.CTRL_MASK));
		mntmCut.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/Cut16.gif")));
		mntmCut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.cut();
			}
		});
		mnEdit.add(mntmCut);

		mntmCopy = new JMenuItem(BUNDLE.getString("MainFrame.mntmCopy.text"));
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				InputEvent.CTRL_MASK));
		mntmCopy.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/Copy16.gif")));
		mntmCopy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.copy();
			}
		});
		mnEdit.add(mntmCopy);

		mntmPaste = new JMenuItem(BUNDLE.getString("MainFrame.mntmPaste.text"));
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				InputEvent.CTRL_MASK));
		mntmPaste.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/Paste16.gif")));
		mntmPaste.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.paste();
			}
		});
		mnEdit.add(mntmPaste);

		separator_6 = new JSeparator();
		mnEdit.add(separator_6);

		mntmDelete = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmDelete.text"));
		mntmDelete.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/Delete16.gif")));
		mntmDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.deleteSelected();
			}
		});
		mnEdit.add(mntmDelete);

		separator_7 = new JSeparator();
		mnEdit.add(separator_7);

		mntmSelectAll = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmSelectAll.text"));
		mntmSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				InputEvent.CTRL_MASK));
		mntmSelectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.selectAll();
			}
		});
		mnEdit.add(mntmSelectAll);

		separator_8 = new JSeparator();
		mnEdit.add(separator_8);

		mntmPreferences = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmPreferences.text")); //$NON-NLS-1$
		mntmPreferences
				.setIcon(new ImageIcon(
						MainFrame.class
								.getResource("/toolbarButtonGraphics/general/Preferences16.gif")));
		mntmPreferences.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.showPreferences();
			}
		});
		mnEdit.add(mntmPreferences);
	}

	private void initViewMenu() {
		mnView = new JMenu(BUNDLE.getString("MainFrame.mnView.text"));
		mnView.setMnemonic(KeyEvent.VK_V);
		mainMenuBar.add(mnView);

		chckbxmntmToolbox = new JCheckBoxMenuItem(
				BUNDLE.getString("MainFrame.chckbxmntmToolbox.text"));
		chckbxmntmToolbox.setSelected(true);
		chckbxmntmToolbox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.displayToolbox(chckbxmntmToolbox.getState());
			}
		});
		mnView.add(chckbxmntmToolbox);

		chckbxmntmStatusbar = new JCheckBoxMenuItem(
				BUNDLE.getString("MainFrame.chckbxmntmStatusbar.text"));
		chckbxmntmStatusbar.setSelected(true);
		chckbxmntmStatusbar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.displayStatusbar(chckbxmntmStatusbar.getState());
			}
		});
		mnView.add(chckbxmntmStatusbar);

		chckbxmntmSidePanel = new JCheckBoxMenuItem(
				BUNDLE.getString("MainFrame.chckbxmntmSidePanel.text"));
		chckbxmntmSidePanel.setSelected(true);
		chckbxmntmSidePanel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.displaySidePanel(chckbxmntmSidePanel.getState());
			}
		});
		mnView.add(chckbxmntmSidePanel);

		separator_9 = new JSeparator();
		mnView.add(separator_9);

		mntmFullscreen = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmFullscreen.text"));
		mntmFullscreen.setAccelerator(KeyStroke
				.getKeyStroke(KeyEvent.VK_F11, 0));
		mnView.add(mntmFullscreen);
	}

	private void initSearchMenu() {
		mnSearch = new JMenu(BUNDLE.getString("MainFrame.mnSearch.text"));
		mnSearch.setMnemonic(KeyEvent.VK_S);
		mainMenuBar.add(mnSearch);

		mntmFindreplace = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmFindreplace.text"));
		mntmFindreplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				InputEvent.CTRL_MASK));
		mntmFindreplace.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/general/Find16.gif")));
		mntmFindreplace.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.openFindReplaceDialog();
			}
		});
		mnSearch.add(mntmFindreplace);

		// mntmFindNext = new
		// JMenuItem(BUNDLE.getString("MainFrame.mntmFindNext.text"));
		// mntmFindNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
		// InputEvent.CTRL_MASK));
		// mnSearch.add(mntmFindNext);
		//
		// mntmFindPrevious = new
		// JMenuItem(BUNDLE.getString("MainFrame.mntmFindPrevious.text"));
		// mntmFindPrevious.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
		// InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		// mnSearch.add(mntmFindPrevious);

		separator_10 = new JSeparator();
		mnSearch.add(separator_10);

		mntmClearHighlight = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmClearHighlight.text"));
		mntmClearHighlight.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,
				InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmClearHighlight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.clearHighlight();
			}
		});
		mnSearch.add(mntmClearHighlight);

		separator_11 = new JSeparator();
		mnSearch.add(separator_11);

		mntmGoToLine = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmGoToLine.text"));
		mntmGoToLine.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				InputEvent.CTRL_MASK));
		mntmGoToLine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.openGoToLineDialog();
			}
		});
		mnSearch.add(mntmGoToLine);
	}

	private void initProjectMenu() {
		mnProject = new JMenu(BUNDLE.getString("MainFrame.mnProject.text"));
		mnProject.setMnemonic(KeyEvent.VK_P);
		mainMenuBar.add(mnProject);

		mntmProperties = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmProperties.text"));
		mntmProperties
				.setIcon(new ImageIcon(
						MainFrame.class
								.getResource("/toolbarButtonGraphics/general/Properties16.gif")));
		mntmProperties.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.showProjectProperties();
			}
		});
		mnProject.add(mntmProperties);
	}

	private void initToolsMenu() {
		mnTools = new JMenu(BUNDLE.getString("MainFrame.mnTools.text"));
		mnTools.setMnemonic(KeyEvent.VK_T);
		mainMenuBar.add(mnTools);

		mntmWorkflowEditor = new JMenuItem(
				BUNDLE.getString("MainFrame.mntmWorkflowEditor.text"));
		mntmWorkflowEditor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.openWorkflowEditor();
			}
		});
		mnTools.add(mntmWorkflowEditor);

		mntmWorkflowExecutions = new JMenuItem("Workflow Executions");
		mntmWorkflowExecutions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.openWorkflowViewer();
			}
		});
		mnTools.add(mntmWorkflowExecutions);

		mntmInfrastructureBrowser = new JMenuItem("Infrastructure Browser");
		mntmInfrastructureBrowser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.openInfrastructureBrowser();
			}
		});
		mnTools.add(mntmInfrastructureBrowser);

		mntmResourceMonitor = new JMenuItem("Resource Monitor");
		mntmResourceMonitor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.openResourceMonitor();
			}
		});
		mnTools.add(mntmResourceMonitor);
	}

	private void initHelpMenu() {
		mnHelp = new JMenu(BUNDLE.getString("MainFrame.mnHelp.text"));
		mnHelp.setMnemonic(KeyEvent.VK_H);
		mainMenuBar.add(mnHelp);

		// mntmContents = new
		// JMenuItem(BUNDLE.getString("MainFrame.mntmContents.text"));
		// mntmContents.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
		// 0));
		// mntmContents.setIcon(new
		// ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/About16.gif")));
		// mntmContents.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// mnHelp.add(mntmContents);

		mntmAbout = new JMenuItem(BUNDLE.getString("MainFrame.mntmAbout.text"));
		mntmAbout
				.setIcon(new ImageIcon(
						MainFrame.class
								.getResource("/toolbarButtonGraphics/general/Information16.gif")));
		mntmAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.openAboutDialog();
			}
		});
		mnHelp.add(mntmAbout);
	}

	private void initMenu() {
		mainMenuBar = new JMenuBar();
		setJMenuBar(mainMenuBar);

		initFileMenu();
		initEditMenu();
		initViewMenu();
		initSearchMenu();
		initProjectMenu();
		initToolsMenu();
		initHelpMenu();
	}

	private void initWorkspace() {
		workspacePane = new JTabbedPane(JTabbedPane.TOP);
		workspacePane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		centerPane.setRightComponent(workspacePane);
	}

	private void initToolbar() {
		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);

		btnStart = new JButton("Start Execution");
		btnStart.setEnabled(false);
		btnStart.setToolTipText("Sends Workflow To Execution");
		btnStart.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/media/Play24.gif")));
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.startWorkflowGraphExecution();
			}
		});
		toolBar.add(btnStart);

		btnPause = new JButton("Pause Execution");
		btnPause.setEnabled(false);
		btnPause.setToolTipText("Pauses Previously Started Execution");
		btnPause.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/media/Pause24.gif")));
		btnPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.pauseWorkflowGraphExecution();
			}
		});
		toolBar.add(btnPause);

		btnStop = new JButton("Stop Execution");
		btnStop.setEnabled(false);
		btnStop.setToolTipText("Stops Previously Started Execution");
		btnStop.setIcon(new ImageIcon(MainFrame.class
				.getResource("/toolbarButtonGraphics/media/Stop24.gif")));
		btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.stopWorkflowGraphExecution();
			}
		});
		toolBar.add(btnStop);
	}

	private void initStatusbar() {
		statusbar = new JPanel();
		contentPane.add(statusbar, BorderLayout.SOUTH);
		statusbar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// TODO XXX what info in statusbar?
		// JLabel label = new JLabel("New label");
		// statusbar.add(label);
		//
		// JLabel label_1 = new JLabel("New label");
		// statusbar.add(label_1);
		//
		// JLabel label_2 = new JLabel("New label");
		// statusbar.add(label_2);
		//
		// JLabel label_3 = new JLabel("New label");
		// statusbar.add(label_3);
		//
	}

	private void initSidePane() {
		sidePane = new JTabbedPane(JTabbedPane.TOP);
		centerPane.setLeftComponent(sidePane);
		sidePane.setMinimumSize(new Dimension(180, 0));

		projectPanel = new JPanel();
		sidePane.addTab("Project", null, projectPanel, null);
		projectPanel.setLayout(new BorderLayout(0, 0));

		projectScrollPane = new JScrollPane();
		projectPanel.add(projectScrollPane, BorderLayout.CENTER);

		repositoryPanel = new JPanel();
		sidePane.addTab("Repository", null, repositoryPanel, null);
		repositoryPanel.setLayout(new BorderLayout(0, 0));

		repositoryScrollPane = new JScrollPane();
		repositoryPanel.add(repositoryScrollPane, BorderLayout.CENTER);
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		LOG.info("KH: constructor fired!");
		controller = new MainFrameController(this);
		initUI();
	}

	/*
	 * getters and setters
	 */

	public JMenuBar getMainMenuBar() {
		return mainMenuBar;
	}

	public void setMainMenuBar(final JMenuBar mainMenuBar) {
		this.mainMenuBar = mainMenuBar;
	}

	public JMenu getMnFile() {
		return mnFile;
	}

	public void setMnFile(final JMenu mnFile) {
		this.mnFile = mnFile;
	}

	public JMenuItem getMntmNew() {
		return mntmNew;
	}

	public void setMntmNew(final JMenuItem mntmNew) {
		this.mntmNew = mntmNew;
	}

	public JMenuItem getMntmOpen() {
		return mntmOpen;
	}

	public void setMntmOpen(final JMenuItem mntmOpen) {
		this.mntmOpen = mntmOpen;
	}

	public JMenuItem getMntmClose() {
		return mntmClose;
	}

	public void setMntmClose(final JMenuItem mntmClose) {
		this.mntmClose = mntmClose;
	}

	public JMenuItem getMntmCloseAll() {
		return mntmCloseAll;
	}

	public void setMntmCloseAll(final JMenuItem mntmCloseAll) {
		this.mntmCloseAll = mntmCloseAll;
	}

	public JMenuItem getMntmSave() {
		return mntmSave;
	}

	public void setMntmSave(final JMenuItem mntmSave) {
		this.mntmSave = mntmSave;
	}

	public JMenuItem getMntmSaveAs() {
		return mntmSaveAs;
	}

	public void setMntmSaveAs(final JMenuItem mntmSaveAs) {
		this.mntmSaveAs = mntmSaveAs;
	}

	public JMenuItem getMntmSaveAll() {
		return mntmSaveAll;
	}

	public void setMntmSaveAll(final JMenuItem mntmSaveAll) {
		this.mntmSaveAll = mntmSaveAll;
	}

	public JMenuItem getMntmRefresh() {
		return mntmRefresh;
	}

	public void setMntmRefresh(final JMenuItem mntmRefresh) {
		this.mntmRefresh = mntmRefresh;
	}

	public JMenuItem getMntmImport() {
		return mntmLogin;
	}

	public void setMntmImport(final JMenuItem mntmImport) {
		this.mntmLogin = mntmImport;
	}

	public JMenuItem getMntmExport() {
		return mntmLogout;
	}

	public void setMntmExport(final JMenuItem mntmExport) {
		this.mntmLogout = mntmExport;
	}

	public JMenuItem getMntmExit() {
		return mntmExit;
	}

	public void setMntmExit(final JMenuItem mntmExit) {
		this.mntmExit = mntmExit;
	}

	public JMenu getMnEdit() {
		return mnEdit;
	}

	public void setMnEdit(final JMenu mnEdit) {
		this.mnEdit = mnEdit;
	}

	public JMenuItem getMntmUndo() {
		return mntmUndo;
	}

	public void setMntmUndo(final JMenuItem mntmUndo) {
		this.mntmUndo = mntmUndo;
	}

	public JMenuItem getMntmRedo() {
		return mntmRedo;
	}

	public void setMntmRedo(final JMenuItem mntmRedo) {
		this.mntmRedo = mntmRedo;
	}

	public JMenuItem getMntmCut() {
		return mntmCut;
	}

	public void setMntmCut(final JMenuItem mntmCut) {
		this.mntmCut = mntmCut;
	}

	public JMenuItem getMntmCopy() {
		return mntmCopy;
	}

	public void setMntmCopy(final JMenuItem mntmCopy) {
		this.mntmCopy = mntmCopy;
	}

	public JMenuItem getMntmPaste() {
		return mntmPaste;
	}

	public void setMntmPaste(final JMenuItem mntmPaste) {
		this.mntmPaste = mntmPaste;
	}

	public JMenuItem getMntmDelete() {
		return mntmDelete;
	}

	public void setMntmDelete(final JMenuItem mntmDelete) {
		this.mntmDelete = mntmDelete;
	}

	public JMenuItem getMntmSelectAll() {
		return mntmSelectAll;
	}

	public void setMntmSelectAll(final JMenuItem mntmSelectAll) {
		this.mntmSelectAll = mntmSelectAll;
	}

	public JMenu getMnView() {
		return mnView;
	}

	public void setMnView(final JMenu mnView) {
		this.mnView = mnView;
	}

	public JCheckBoxMenuItem getChckbxmntmToolbox() {
		return chckbxmntmToolbox;
	}

	public void setChckbxmntmToolbox(final JCheckBoxMenuItem chckbxmntmToolbox) {
		this.chckbxmntmToolbox = chckbxmntmToolbox;
	}

	public JCheckBoxMenuItem getChckbxmntmStatusbar() {
		return chckbxmntmStatusbar;
	}

	public void setChckbxmntmStatusbar(
			final JCheckBoxMenuItem chckbxmntmStatusbar) {
		this.chckbxmntmStatusbar = chckbxmntmStatusbar;
	}

	public JCheckBoxMenuItem getChckbxmntmSidePanel() {
		return chckbxmntmSidePanel;
	}

	public void setChckbxmntmSidePanel(
			final JCheckBoxMenuItem chckbxmntmSidePanel) {
		this.chckbxmntmSidePanel = chckbxmntmSidePanel;
	}

	public JMenuItem getMntmFullscreen() {
		return mntmFullscreen;
	}

	public void setMntmFullscreen(final JMenuItem mntmFullscreen) {
		this.mntmFullscreen = mntmFullscreen;
	}

	public JMenu getMnSearch() {
		return mnSearch;
	}

	public void setMnSearch(final JMenu mnSearch) {
		this.mnSearch = mnSearch;
	}

	public JMenuItem getMntmFindreplace() {
		return mntmFindreplace;
	}

	public void setMntmFindreplace(final JMenuItem mntmFindreplace) {
		this.mntmFindreplace = mntmFindreplace;
	}

	public JMenuItem getMntmFindNext() {
		return mntmFindNext;
	}

	public void setMntmFindNext(final JMenuItem mntmFindNext) {
		this.mntmFindNext = mntmFindNext;
	}

	public JMenuItem getMntmFindPrevious() {
		return mntmFindPrevious;
	}

	public void setMntmFindPrevious(final JMenuItem mntmFindPrevious) {
		this.mntmFindPrevious = mntmFindPrevious;
	}

	public JMenuItem getMntmClearHighlight() {
		return mntmClearHighlight;
	}

	public void setMntmClearHighlight(final JMenuItem mntmClearHighlight) {
		this.mntmClearHighlight = mntmClearHighlight;
	}

	public JMenuItem getMntmGoToLine() {
		return mntmGoToLine;
	}

	public void setMntmGoToLine(final JMenuItem mntmGoToLine) {
		this.mntmGoToLine = mntmGoToLine;
	}

	public JMenu getMnProject() {
		return mnProject;
	}

	public void setMnProject(final JMenu mnProject) {
		this.mnProject = mnProject;
	}

	public JMenu getMnTools() {
		return mnTools;
	}

	public void setMnTools(final JMenu mnTools) {
		this.mnTools = mnTools;
	}

	public JMenu getMnHelp() {
		return mnHelp;
	}

	public void setMnHelp(final JMenu mnHelp) {
		this.mnHelp = mnHelp;
	}

	public JMenuItem getMntmContents() {
		return mntmContents;
	}

	public void setMntmContents(final JMenuItem mntmContents) {
		this.mntmContents = mntmContents;
	}

	public JMenuItem getMntmAbout() {
		return mntmAbout;
	}

	public void setMntmAbout(final JMenuItem mntmAbout) {
		this.mntmAbout = mntmAbout;
	}

	public JToolBar getToolBar() {
		return toolBar;
	}

	public void setToolBar(final JToolBar toolBar) {
		this.toolBar = toolBar;
	}

	public JPanel getStatusbar() {
		return statusbar;
	}

	public void setStatusbar(final JPanel statusbar) {
		this.statusbar = statusbar;
	}

	public JSplitPane getCenterPane() {
		return centerPane;
	}

	public void setCenterPane(final JSplitPane centerPane) {
		this.centerPane = centerPane;
	}

	public JTabbedPane getWorkspacePane() {
		return workspacePane;
	}

	public void setWorkspacePane(final JTabbedPane workspacePane) {
		this.workspacePane = workspacePane;
	}

	public JPanel getProjectPanel() {
		return projectPanel;
	}

	public void setProjectPanel(final JPanel projectPanel) {
		this.projectPanel = projectPanel;
	}

	public JPanel getRepositoryPanel() {
		return repositoryPanel;
	}

	public void setRepositoryPanel(final JPanel repositoryPanel) {
		this.repositoryPanel = repositoryPanel;
	}

	public JMenuItem getMntmProperties() {
		return mntmProperties;
	}

	public void setMntmProperties(final JMenuItem mntmProperties) {
		this.mntmProperties = mntmProperties;
	}

	public JButton getBtnStart() {
		return btnStart;
	}

	public void setBtnStart(final JButton btnStart) {
		this.btnStart = btnStart;
	}

	public JButton getBtnPause() {
		return btnPause;
	}

	public void setBtnPause(final JButton btnPause) {
		this.btnPause = btnPause;
	}

	public JButton getBtnStop() {
		return btnStop;
	}

	public void setBtnStop(final JButton btnStop) {
		this.btnStop = btnStop;
	}

	public JTabbedPane getSidePane() {
		return sidePane;
	}

	public void setSidePane(final JTabbedPane sidePane) {
		this.sidePane = sidePane;
	}

	public JMenuItem getMntmPreferences() {
		return mntmPreferences;
	}

	public void setMntmPreferences(final JMenuItem mntmPreferences) {
		this.mntmPreferences = mntmPreferences;
	}

	public JScrollPane getProjectScrollPane() {
		return projectScrollPane;
	}

	public void setProjectScrollPane(final JScrollPane projectScrollPane) {
		this.projectScrollPane = projectScrollPane;
	}

	public JTree getProjectTree() {
		return projectTree;
	}

	public void setProjectTree(final JTree projectTree) {
		this.projectTree = projectTree;
	}

	public MainFrameController getController() {
		return controller;
	}

	public void setController(final MainFrameController controller) {
		this.controller = controller;
	}

	public JList getRepositoryList() {
		return repositoryList;
	}

	public void setRepositoryList(final JList repositoryList) {
		this.repositoryList = repositoryList;
	}

	public JMenuItem getMntmWorkflowEditor() {
		return mntmWorkflowEditor;
	}

	public void setMntmWorkflowEditor(final JMenuItem mntmWorkflowEditor) {
		this.mntmWorkflowEditor = mntmWorkflowEditor;
	}

	public JScrollPane getRepositoryScrollPane() {
		return repositoryScrollPane;
	}

	public void setRepositoryScrollPane(final JScrollPane repositoryScrollPane) {
		this.repositoryScrollPane = repositoryScrollPane;
	}
}
