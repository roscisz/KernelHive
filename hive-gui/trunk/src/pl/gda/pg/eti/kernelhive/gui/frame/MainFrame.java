package pl.gda.pg.eti.kernelhive.gui.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;

import pl.gda.pg.eti.kernelhive.gui.component.JTabPanel;
import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;
import pl.gda.pg.eti.kernelhive.gui.controller.MainFrameController;
import pl.gda.pg.eti.kernelhive.gui.project.KernelHiveProject;
import pl.gda.pg.eti.kernelhive.gui.source.editor.SourceCodeEditor;

/**
 * Main Frame of the Application
 * @author mschally
 * @version 1.0
 */
public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = -2018750030834289098L;
	private static ResourceBundle BUNDLE = AppConfiguration.getInstance().getLanguageResourceBundle();
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
	
	private MainFrameController controller;

	private void initUI() {
		setTitle(BUNDLE.getString("MainFrame.this.title"));  
		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
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
	
	private void initFileMenu(){
		mnFile = new JMenu(BUNDLE.getString("MainFrame.mnFile.text"));
		mnFile.setMnemonic(KeyEvent.VK_F);
		mainMenuBar.add(mnFile);

		mntmNew = new JMenuItem(BUNDLE.getString("MainFrame.mntmNew.text"));  
		mntmNew.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/New16.gif")));
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		mntmNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.newProject();
			}
		});
		mnFile.add(mntmNew);

		mntmOpen = new JMenuItem(BUNDLE.getString("MainFrame.mntmOpen.text"));  
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmOpen.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Open16.gif")));
		mntmOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.openProject();
				
				
				//TODO remove
				JPanel panel = new JPanel(new BorderLayout());
				mxGraph graph = new mxGraph();
				Object parent = graph.getDefaultParent();
				graph.getModel().beginUpdate();
				try{
					mxCell v1 = (mxCell)graph.insertVertex(parent, "Node1", null, 20, 20, 80, 30);
					mxCell v2 = (mxCell)graph.insertVertex(parent, "Node2", null, 240, 150, 80, 30);
					graph.insertEdge(parent, "Edge", "", v1, v2);
				}finally{
					graph.getModel().endUpdate();
				}
				mxGraphComponent graphComp = new mxGraphComponent(graph);
				panel.add(graphComp);
				workspacePane.addTab("workflow", panel);
				//
			}
		});
		mnFile.add(mntmOpen);
		
		separator = new JSeparator();
		mnFile.add(separator);

		mntmClose = new JMenuItem(BUNDLE.getString("MainFrame.mntmClose.text"));  
		mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		mntmClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO remove
				JPanel sourcePanel = SourceCodeEditor.createNewEditor();
				workspacePane.add("source", sourcePanel);
				workspacePane.setTabComponentAt(0, new JTabPanel(sourcePanel, workspacePane));
				workspacePane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
				//
				
				controller.closeTab();				
			}
		});
		mnFile.add(mntmClose);

		mntmCloseAll = new JMenuItem(BUNDLE.getString("MainFrame.mntmCloseAll.text")); 
		mntmCloseAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.closeProject();				
			}
		});
		mnFile.add(mntmCloseAll);
		
		separator_1 = new JSeparator();
		mnFile.add(separator_1);

		mntmSave = new JMenuItem(BUNDLE.getString("MainFrame.mntmSave.text"));  
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmSave.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Save16.gif")));
		mntmSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		mnFile.add(mntmSave);

		mntmSaveAs = new JMenuItem(BUNDLE.getString("MainFrame.mntmSaveAs.text"));  
		mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmSaveAs.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/SaveAs16.gif")));
		mntmSaveAs.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		mnFile.add(mntmSaveAs);

		mntmSaveAll = new JMenuItem(BUNDLE.getString("MainFrame.mntmSaveAll.text"));  
		mntmSaveAll.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/SaveAll16.gif")));
		mntmSaveAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		mnFile.add(mntmSaveAll);
		
		separator_2 = new JSeparator();
		mnFile.add(separator_2);

		mntmRefresh = new JMenuItem(BUNDLE.getString("MainFrame.mntmRefresh.text"));  
		mntmRefresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mntmRefresh.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Refresh16.gif")));
		mntmRefresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		mnFile.add(mntmRefresh);
		
		separator_3 = new JSeparator();
		mnFile.add(separator_3);

		mntmImport = new JMenuItem(BUNDLE.getString("MainFrame.mntmImport.text"));  
		mntmImport.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Import16.gif")));
		mnFile.add(mntmImport);

		mntmExport = new JMenuItem(BUNDLE.getString("MainFrame.mntmExport.text"));  
		mntmExport.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Export16.gif")));
		mnFile.add(mntmExport);
		
		separator_4 = new JSeparator();
		mnFile.add(separator_4);

		mntmExit = new JMenuItem(BUNDLE.getString("MainFrame.mntmExit.text"));  
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mntmExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		mnFile.add(mntmExit);

	}
	
	private void initEditMenu(){
		mnEdit = new JMenu(BUNDLE.getString("MainFrame.mnEdit.text"));  
		mnEdit.setMnemonic(KeyEvent.VK_E);
		mainMenuBar.add(mnEdit);

		mntmUndo = new JMenuItem(BUNDLE.getString("MainFrame.mntmUndo.text"));  
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		mntmUndo.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Undo16.gif")));
		mnEdit.add(mntmUndo);

		mntmRedo = new JMenuItem(BUNDLE.getString("MainFrame.mntmRedo.text"));  
		mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmRedo.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Redo16.gif")));
		mnEdit.add(mntmRedo);
		
		separator_5 = new JSeparator();
		mnEdit.add(separator_5);

		mntmCut = new JMenuItem(BUNDLE.getString("MainFrame.mntmCut.text"));  
		mntmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mntmCut.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Cut16.gif")));
		mnEdit.add(mntmCut);

		mntmCopy = new JMenuItem(BUNDLE.getString("MainFrame.mntmCopy.text"));  
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mntmCopy.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Copy16.gif")));
		mnEdit.add(mntmCopy);

		mntmPaste = new JMenuItem(BUNDLE.getString("MainFrame.mntmPaste.text"));  
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mntmPaste.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Paste16.gif")));
		mnEdit.add(mntmPaste);
		
		separator_6 = new JSeparator();
		mnEdit.add(separator_6);

		mntmDelete = new JMenuItem(BUNDLE.getString("MainFrame.mntmDelete.text"));  
		mntmDelete.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Delete16.gif")));
		mnEdit.add(mntmDelete);
		
		separator_7 = new JSeparator();
		mnEdit.add(separator_7);

		mntmSelectAll = new JMenuItem(BUNDLE.getString("MainFrame.mntmSelectAll.text"));  
		mntmSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		mnEdit.add(mntmSelectAll);
		
		separator_8 = new JSeparator();
		mnEdit.add(separator_8);

		mntmSetEncoding = new JMenuItem(BUNDLE.getString("MainFrame.mntmSetEncoding.text"));  
		mnEdit.add(mntmSetEncoding);
		
		mntmPreferences = new JMenuItem(BUNDLE.getString("MainFrame.mntmPreferences.text")); //$NON-NLS-1$
		mntmPreferences.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Preferences16.gif")));
		mnEdit.add(mntmPreferences);
	}
	
	private void initViewMenu(){
		mnView = new JMenu(BUNDLE.getString("MainFrame.mnView.text"));  
		mnView.setMnemonic(KeyEvent.VK_V);
		mainMenuBar.add(mnView);

		chckbxmntmToolbox = new JCheckBoxMenuItem(BUNDLE.getString("MainFrame.chckbxmntmToolbox.text"));  
		chckbxmntmToolbox.setSelected(true);
		mnView.add(chckbxmntmToolbox);

		chckbxmntmStatusbar = new JCheckBoxMenuItem(BUNDLE.getString("MainFrame.chckbxmntmStatusbar.text"));  
		chckbxmntmStatusbar.setSelected(true);
		mnView.add(chckbxmntmStatusbar);

		chckbxmntmSidePanel = new JCheckBoxMenuItem(BUNDLE.getString("MainFrame.chckbxmntmSidePanel.text"));  
		chckbxmntmSidePanel.setSelected(true);
		mnView.add(chckbxmntmSidePanel);
		
		separator_9 = new JSeparator();
		mnView.add(separator_9);

		mntmFullscreen = new JMenuItem(BUNDLE.getString("MainFrame.mntmFullscreen.text"));  
		mntmFullscreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
		mnView.add(mntmFullscreen);
	}
	
	private void initSearchMenu(){
		mnSearch = new JMenu(BUNDLE.getString("MainFrame.mnSearch.text"));  
		mnSearch.setMnemonic(KeyEvent.VK_S);
		mainMenuBar.add(mnSearch);

		mntmFindreplace = new JMenuItem(BUNDLE.getString("MainFrame.mntmFindreplace.text"));  
		mntmFindreplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		mntmFindreplace.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Find16.gif")));
		mnSearch.add(mntmFindreplace);

		mntmFindNext = new JMenuItem(BUNDLE.getString("MainFrame.mntmFindNext.text"));  
		mntmFindNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
		mnSearch.add(mntmFindNext);

		mntmFindPrevious = new JMenuItem(BUNDLE.getString("MainFrame.mntmFindPrevious.text"));  
		mntmFindPrevious.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnSearch.add(mntmFindPrevious);
		
		separator_10 = new JSeparator();
		mnSearch.add(separator_10);
		
		mntmClearHighlight = new JMenuItem(BUNDLE.getString("MainFrame.mntmClearHighlight.text"));  
		mntmClearHighlight.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnSearch.add(mntmClearHighlight);
		
		separator_11 = new JSeparator();
		mnSearch.add(separator_11);

		mntmGoToLine = new JMenuItem(BUNDLE.getString("MainFrame.mntmGoToLine.text"));  
		mntmGoToLine.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
		mnSearch.add(mntmGoToLine);
	}
	
	private void initProjectMenu(){
		mnProject = new JMenu(BUNDLE.getString("MainFrame.mnProject.text"));  
		mnProject.setMnemonic(KeyEvent.VK_P);
		mainMenuBar.add(mnProject);
		
		mntmProperties = new JMenuItem(BUNDLE.getString("MainFrame.mntmProperties.text")); //$NON-NLS-1$
		mntmProperties.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Properties16.gif")));
		mnProject.add(mntmProperties);
	}
	
	private void initToolsMenu(){
		mnTools = new JMenu(BUNDLE.getString("MainFrame.mnTools.text"));  
		mnTools.setMnemonic(KeyEvent.VK_T);
		mainMenuBar.add(mnTools);
	}
	
	private void initHelpMenu(){
		mnHelp = new JMenu(BUNDLE.getString("MainFrame.mnHelp.text"));  
		mnHelp.setMnemonic(KeyEvent.VK_H);
		mainMenuBar.add(mnHelp);

		mntmContents = new JMenuItem(BUNDLE.getString("MainFrame.mntmContents.text"));  
		mntmContents.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mntmContents.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/About16.gif")));
		mnHelp.add(mntmContents);

		mntmAbout = new JMenuItem(BUNDLE.getString("MainFrame.mntmAbout.text"));  
		mntmAbout.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/general/Information16.gif")));
		mnHelp.add(mntmAbout);
	}

	private void initMenu(){
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
	
	private void initWorkspace(){
		workspacePane = new JTabbedPane(JTabbedPane.TOP);
		centerPane.setRightComponent(workspacePane);
	}
	
	private void initToolbar(){
		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		btnStart = new JButton(BUNDLE.getString("MainFrame.btnStart.text")); //$NON-NLS-1$
		btnStart.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/media/Play24.gif")));
		toolBar.add(btnStart);
		
		btnPause = new JButton(BUNDLE.getString("MainFrame.btnPause.text")); //$NON-NLS-1$
		btnPause.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/media/Pause24.gif")));
		toolBar.add(btnPause);
		
		btnStop = new JButton(BUNDLE.getString("MainFrame.btnStop.text")); //$NON-NLS-1$
		btnStop.setIcon(new ImageIcon(MainFrame.class.getResource("/toolbarButtonGraphics/media/Stop24.gif")));
		toolBar.add(btnStop);
	}
	
	private void initStatusbar(){
		statusbar = new JPanel();
		contentPane.add(statusbar, BorderLayout.SOUTH);
		statusbar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// TODO XXX what info in statusbar?
		JLabel label = new JLabel("New label");
		statusbar.add(label);

		JLabel label_1 = new JLabel("New label");
		statusbar.add(label_1);

		JLabel label_2 = new JLabel("New label");
		statusbar.add(label_2);

		JLabel label_3 = new JLabel("New label");
		statusbar.add(label_3);
		//
	}
	
	private void initSidePane(){
		sidePane = new JTabbedPane(JTabbedPane.TOP);
		centerPane.setLeftComponent(sidePane);
		sidePane.setMinimumSize(new Dimension(180, 0));

		projectPanel = new JPanel();
		sidePane.addTab("Project", null, projectPanel, null);

		repositoryPanel = new JPanel();
		sidePane.addTab("Repository", null, repositoryPanel, null);		
	}
	
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		controller = new MainFrameController(this);
		initUI();
	}

	/*
	 * getters and setters
	 */
	
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

	public JPanel getStatusbar() {
		return statusbar;
	}

	public void setStatusbar(JPanel statusbar) {
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
		return sidePane;
	}

	public void setSidebarPane(JTabbedPane sidebarPane) {
		this.sidePane = sidebarPane;
	}

	public JPanel getProjectPanel() {
		return projectPanel;
	}

	public void setProjectPanel(JPanel projectPanel) {
		this.projectPanel = projectPanel;
	}

	public JPanel getRepositoryPanel() {
		return repositoryPanel;
	}

	public void setRepositoryPanel(JPanel repositoryPanel) {
		this.repositoryPanel = repositoryPanel;
	}

	public JMenuItem getMntmProperties() {
		return mntmProperties;
	}

	public void setMntmProperties(JMenuItem mntmProperties) {
		this.mntmProperties = mntmProperties;
	}

	public JButton getBtnStart() {
		return btnStart;
	}

	public void setBtnStart(JButton btnStart) {
		this.btnStart = btnStart;
	}

	public JButton getBtnPause() {
		return btnPause;
	}

	public void setBtnPause(JButton btnPause) {
		this.btnPause = btnPause;
	}

	public JButton getBtnStop() {
		return btnStop;
	}

	public void setBtnStop(JButton btnStop) {
		this.btnStop = btnStop;
	}	
}
