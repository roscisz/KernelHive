package pl.gda.pg.eti.kernelhive.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main Frame of the Application
 * @author mschally
 * @version 1.0
 */
public class MainFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2018750030834289098L;
	private static ResourceBundle BUNDLE = ResourceBundle.getBundle("messages", new Locale("en", "US"));
	
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
	private JMenuItem mntmImport;
	private JMenuItem mntmExport;
	private JMenuItem mntmExit;
	private JMenu mnEdit;
	private JMenuItem mntmUndo;
	private JMenuItem mntmRedo;
	private JMenuItem mntmCut;
	private JMenuItem mntmCopy;
	private JMenuItem mntmPaste;
	private JMenuItem mntmDelete;
	private JMenuItem mntmSelectAll;
	private JMenuItem mntmSetEncoding;
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
	private Panel statusbar;
	private JSplitPane centerPane;
	private JTabbedPane workspacePane;
	private JTabbedPane sidebarPane;

	private void initUI() {
		setTitle(BUNDLE.getString("MainFrame.this.title"));  
		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		mainMenuBar = new JMenuBar();
		setJMenuBar(mainMenuBar);

		mnFile = new JMenu(BUNDLE.getString("MainFrame.mnFile.text"));  
		mainMenuBar.add(mnFile);

		mntmNew = new JMenuItem(BUNDLE.getString("MainFrame.mntmNew.text"));  
		mnFile.add(mntmNew);

		mntmOpen = new JMenuItem(BUNDLE.getString("MainFrame.mntmOpen.text"));  
		mnFile.add(mntmOpen);

		mntmClose = new JMenuItem(BUNDLE.getString("MainFrame.mntmClose.text"));  
		mnFile.add(mntmClose);

		mntmCloseAll = new JMenuItem(BUNDLE.getString("MainFrame.mntmCloseAll.text"));  
		mnFile.add(mntmCloseAll);

		mntmSave = new JMenuItem(BUNDLE.getString("MainFrame.mntmSave.text"));  
		mnFile.add(mntmSave);

		mntmSaveAs = new JMenuItem(BUNDLE.getString("MainFrame.mntmSaveAs.text"));  
		mnFile.add(mntmSaveAs);

		mntmSaveAll = new JMenuItem(BUNDLE.getString("MainFrame.mntmSaveAll.text"));  
		mnFile.add(mntmSaveAll);

		mntmRefresh = new JMenuItem(BUNDLE.getString("MainFrame.mntmRefresh.text"));  
		mnFile.add(mntmRefresh);

		mntmImport = new JMenuItem(BUNDLE.getString("MainFrame.mntmImport.text"));  
		mnFile.add(mntmImport);

		mntmExport = new JMenuItem(BUNDLE.getString("MainFrame.mntmExport.text"));  
		mnFile.add(mntmExport);

		mntmExit = new JMenuItem(BUNDLE.getString("MainFrame.mntmExit.text"));  
		mnFile.add(mntmExit);

		mnEdit = new JMenu(BUNDLE.getString("MainFrame.mnEdit.text"));  
		mainMenuBar.add(mnEdit);

		mntmUndo = new JMenuItem(BUNDLE.getString("MainFrame.mntmUndo.text"));  
		mnEdit.add(mntmUndo);

		mntmRedo = new JMenuItem(BUNDLE.getString("MainFrame.mntmRedo.text"));  
		mnEdit.add(mntmRedo);

		mntmCut = new JMenuItem(BUNDLE.getString("MainFrame.mntmCut.text"));  
		mnEdit.add(mntmCut);

		mntmCopy = new JMenuItem(BUNDLE.getString("MainFrame.mntmCopy.text"));  
		mnEdit.add(mntmCopy);

		mntmPaste = new JMenuItem(BUNDLE.getString("MainFrame.mntmPaste.text"));  
		mnEdit.add(mntmPaste);

		mntmDelete = new JMenuItem(BUNDLE.getString("MainFrame.mntmDelete.text"));  
		mnEdit.add(mntmDelete);

		mntmSelectAll = new JMenuItem(BUNDLE.getString("MainFrame.mntmSelectAll.text"));  
		mnEdit.add(mntmSelectAll);

		mntmSetEncoding = new JMenuItem(BUNDLE.getString("MainFrame.mntmSetEncoding.text"));  
		mnEdit.add(mntmSetEncoding);

		mnView = new JMenu(BUNDLE.getString("MainFrame.mnView.text"));  
		mainMenuBar.add(mnView);

		chckbxmntmToolbox = new JCheckBoxMenuItem(BUNDLE.getString("MainFrame.chckbxmntmToolbox.text"));  
		mnView.add(chckbxmntmToolbox);

		chckbxmntmStatusbar = new JCheckBoxMenuItem(BUNDLE.getString("MainFrame.chckbxmntmStatusbar.text"));  
		mnView.add(chckbxmntmStatusbar);

		chckbxmntmSidePanel = new JCheckBoxMenuItem(BUNDLE.getString("MainFrame.chckbxmntmSidePanel.text"));  
		mnView.add(chckbxmntmSidePanel);

		mntmFullscreen = new JMenuItem(BUNDLE.getString("MainFrame.mntmFullscreen.text"));  
		mnView.add(mntmFullscreen);

		mnSearch = new JMenu(BUNDLE.getString("MainFrame.mnSearch.text"));  
		mainMenuBar.add(mnSearch);

		mntmFindreplace = new JMenuItem(BUNDLE.getString("MainFrame.mntmFindreplace.text"));  
		mnSearch.add(mntmFindreplace);

		mntmFindNext = new JMenuItem(BUNDLE.getString("MainFrame.mntmFindNext.text"));  
		mnSearch.add(mntmFindNext);

		mntmFindPrevious = new JMenuItem(BUNDLE.getString("MainFrame.mntmFindPrevious.text"));  
		mnSearch.add(mntmFindPrevious);
		
		mntmClearHighlight = new JMenuItem(BUNDLE.getString("MainFrame.mntmClearHighlight.text"));  
		mnSearch.add(mntmClearHighlight);

		mntmGoToLine = new JMenuItem(BUNDLE.getString("MainFrame.mntmGoToLine.text"));  
		mnSearch.add(mntmGoToLine);

		mnProject = new JMenu(BUNDLE.getString("MainFrame.mnProject.text"));  
		mainMenuBar.add(mnProject);

		mnTools = new JMenu(BUNDLE.getString("MainFrame.mnTools.text"));  
		mainMenuBar.add(mnTools);

		mnHelp = new JMenu(BUNDLE.getString("MainFrame.mnHelp.text"));  
		mainMenuBar.add(mnHelp);

		mntmContents = new JMenuItem(BUNDLE.getString("MainFrame.mntmContents.text"));  
		mnHelp.add(mntmContents);

		mntmAbout = new JMenuItem(BUNDLE.getString("MainFrame.mntmAbout.text"));  
		mnHelp.add(mntmAbout);

		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(800, 600));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);

		statusbar = new Panel();
		contentPane.add(statusbar, BorderLayout.SOUTH);
		statusbar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		// TODO XXX what info in statusbar?
		Label label = new Label("New label");
		statusbar.add(label);

		Label label_1 = new Label("New label");
		statusbar.add(label_1);

		Label label_2 = new Label("New label");
		statusbar.add(label_2);

		Label label_3 = new Label("New label");
		statusbar.add(label_3);
		//

		centerPane = new JSplitPane();
		contentPane.add(centerPane, BorderLayout.CENTER);

		workspacePane = new JTabbedPane(JTabbedPane.TOP);
		centerPane.setRightComponent(workspacePane);

		sidebarPane = new JTabbedPane(JTabbedPane.TOP);
		centerPane.setLeftComponent(sidebarPane);

		// TODO XXX what tabs in side pane?
		JPanel panel_1 = new JPanel();
		sidebarPane.addTab("New tab", null, panel_1, null);

		JPanel panel_2 = new JPanel();
		sidebarPane.addTab("New tab", null, panel_2, null);
		//
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		initUI();
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public JMenuBar getMainMenuBar() {
		return mainMenuBar;
	}

	public void setMainMenuBar(JMenuBar mainMenuBar) {
		this.mainMenuBar = mainMenuBar;
	}

	public JMenu getMnFile() {
		return mnFile;
	}

	public void setMnFile(JMenu mnFile) {
		this.mnFile = mnFile;
	}

	public JMenuItem getMntmNew() {
		return mntmNew;
	}

	public void setMntmNew(JMenuItem mntmNew) {
		this.mntmNew = mntmNew;
	}

	public JMenuItem getMntmOpen() {
		return mntmOpen;
	}

	public void setMntmOpen(JMenuItem mntmOpen) {
		this.mntmOpen = mntmOpen;
	}

	public JMenuItem getMntmClose() {
		return mntmClose;
	}

	public void setMntmClose(JMenuItem mntmClose) {
		this.mntmClose = mntmClose;
	}

	public JMenuItem getMntmCloseAll() {
		return mntmCloseAll;
	}

	public void setMntmCloseAll(JMenuItem mntmCloseAll) {
		this.mntmCloseAll = mntmCloseAll;
	}

	public JMenuItem getMntmSave() {
		return mntmSave;
	}

	public void setMntmSave(JMenuItem mntmSave) {
		this.mntmSave = mntmSave;
	}

	public JMenuItem getMntmSaveAs() {
		return mntmSaveAs;
	}

	public void setMntmSaveAs(JMenuItem mntmSaveAs) {
		this.mntmSaveAs = mntmSaveAs;
	}

	public JMenuItem getMntmSaveAll() {
		return mntmSaveAll;
	}

	public void setMntmSaveAll(JMenuItem mntmSaveAll) {
		this.mntmSaveAll = mntmSaveAll;
	}

	public JMenuItem getMntmRefresh() {
		return mntmRefresh;
	}

	public void setMntmRefresh(JMenuItem mntmRefresh) {
		this.mntmRefresh = mntmRefresh;
	}

	public JMenuItem getMntmImport() {
		return mntmImport;
	}

	public void setMntmImport(JMenuItem mntmImport) {
		this.mntmImport = mntmImport;
	}

	public JMenuItem getMntmExport() {
		return mntmExport;
	}

	public void setMntmExport(JMenuItem mntmExport) {
		this.mntmExport = mntmExport;
	}

	public JMenuItem getMntmExit() {
		return mntmExit;
	}

	public void setMntmExit(JMenuItem mntmExit) {
		this.mntmExit = mntmExit;
	}

	public JMenu getMnEdit() {
		return mnEdit;
	}

	public void setMnEdit(JMenu mnEdit) {
		this.mnEdit = mnEdit;
	}

	public JMenuItem getMntmUndo() {
		return mntmUndo;
	}

	public void setMntmUndo(JMenuItem mntmUndo) {
		this.mntmUndo = mntmUndo;
	}

	public JMenuItem getMntmRedo() {
		return mntmRedo;
	}

	public void setMntmRedo(JMenuItem mntmRedo) {
		this.mntmRedo = mntmRedo;
	}

	public JMenuItem getMntmCut() {
		return mntmCut;
	}

	public void setMntmCut(JMenuItem mntmCut) {
		this.mntmCut = mntmCut;
	}

	public JMenuItem getMntmCopy() {
		return mntmCopy;
	}

	public void setMntmCopy(JMenuItem mntmCopy) {
		this.mntmCopy = mntmCopy;
	}

	public JMenuItem getMntmPaste() {
		return mntmPaste;
	}

	public void setMntmPaste(JMenuItem mntmPaste) {
		this.mntmPaste = mntmPaste;
	}

	public JMenuItem getMntmDelete() {
		return mntmDelete;
	}

	public void setMntmDelete(JMenuItem mntmDelete) {
		this.mntmDelete = mntmDelete;
	}

	public JMenuItem getMntmSelectAll() {
		return mntmSelectAll;
	}

	public void setMntmSelectAll(JMenuItem mntmSelectAll) {
		this.mntmSelectAll = mntmSelectAll;
	}

	public JMenuItem getMntmSetEncoding() {
		return mntmSetEncoding;
	}

	public void setMntmSetEncoding(JMenuItem mntmSetEncoding) {
		this.mntmSetEncoding = mntmSetEncoding;
	}

	public JMenu getMnView() {
		return mnView;
	}

	public void setMnView(JMenu mnView) {
		this.mnView = mnView;
	}

	public JCheckBoxMenuItem getChckbxmntmToolbox() {
		return chckbxmntmToolbox;
	}

	public void setChckbxmntmToolbox(JCheckBoxMenuItem chckbxmntmToolbox) {
		this.chckbxmntmToolbox = chckbxmntmToolbox;
	}

	public JCheckBoxMenuItem getChckbxmntmStatusbar() {
		return chckbxmntmStatusbar;
	}

	public void setChckbxmntmStatusbar(JCheckBoxMenuItem chckbxmntmStatusbar) {
		this.chckbxmntmStatusbar = chckbxmntmStatusbar;
	}

	public JCheckBoxMenuItem getChckbxmntmSidePanel() {
		return chckbxmntmSidePanel;
	}

	public void setChckbxmntmSidePanel(JCheckBoxMenuItem chckbxmntmSidePanel) {
		this.chckbxmntmSidePanel = chckbxmntmSidePanel;
	}

	public JMenuItem getMntmFullscreen() {
		return mntmFullscreen;
	}

	public void setMntmFullscreen(JMenuItem mntmFullscreen) {
		this.mntmFullscreen = mntmFullscreen;
	}

	public JMenu getMnSearch() {
		return mnSearch;
	}

	public void setMnSearch(JMenu mnSearch) {
		this.mnSearch = mnSearch;
	}

	public JMenuItem getMntmFindreplace() {
		return mntmFindreplace;
	}

	public void setMntmFindreplace(JMenuItem mntmFindreplace) {
		this.mntmFindreplace = mntmFindreplace;
	}

	public JMenuItem getMntmFindNext() {
		return mntmFindNext;
	}

	public void setMntmFindNext(JMenuItem mntmFindNext) {
		this.mntmFindNext = mntmFindNext;
	}

	public JMenuItem getMntmFindPrevious() {
		return mntmFindPrevious;
	}

	public void setMntmFindPrevious(JMenuItem mntmFindPrevious) {
		this.mntmFindPrevious = mntmFindPrevious;
	}

	public JMenuItem getMntmClearHighlight() {
		return mntmClearHighlight;
	}

	public void setMntmClearHighlight(JMenuItem mntmClearHighlight) {
		this.mntmClearHighlight = mntmClearHighlight;
	}

	public JMenuItem getMntmGoToLine() {
		return mntmGoToLine;
	}

	public void setMntmGoToLine(JMenuItem mntmGoToLine) {
		this.mntmGoToLine = mntmGoToLine;
	}

	public JMenu getMnProject() {
		return mnProject;
	}

	public void setMnProject(JMenu mnProject) {
		this.mnProject = mnProject;
	}

	public JMenu getMnTools() {
		return mnTools;
	}

	public void setMnTools(JMenu mnTools) {
		this.mnTools = mnTools;
	}

	public JMenu getMnHelp() {
		return mnHelp;
	}

	public void setMnHelp(JMenu mnHelp) {
		this.mnHelp = mnHelp;
	}

	public JMenuItem getMntmContents() {
		return mntmContents;
	}

	public void setMntmContents(JMenuItem mntmContents) {
		this.mntmContents = mntmContents;
	}

	public JMenuItem getMntmAbout() {
		return mntmAbout;
	}

	public void setMntmAbout(JMenuItem mntmAbout) {
		this.mntmAbout = mntmAbout;
	}

	public JToolBar getToolBar() {
		return toolBar;
	}

	public void setToolBar(JToolBar toolBar) {
		this.toolBar = toolBar;
	}

	public Panel getStatusbar() {
		return statusbar;
	}

	public void setStatusbar(Panel statusbar) {
		this.statusbar = statusbar;
	}

	public JSplitPane getCenterPane() {
		return centerPane;
	}

	public void setCenterPane(JSplitPane centerPane) {
		this.centerPane = centerPane;
	}

	public JTabbedPane getWorkspacePane() {
		return workspacePane;
	}

	public void setWorkspacePane(JTabbedPane workspacePane) {
		this.workspacePane = workspacePane;
	}

	public JTabbedPane getSidebarPane() {
		return sidebarPane;
	}

	public void setSidebarPane(JTabbedPane sidebarPane) {
		this.sidebarPane = sidebarPane;
	}
	
	
	
}
