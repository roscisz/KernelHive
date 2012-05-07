package pl.gda.pg.eti.kernelhive.gui.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.configuration.ConfigurationException;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.component.JTabPanel;
import pl.gda.pg.eti.kernelhive.gui.component.SourceCodeEditor;
import pl.gda.pg.eti.kernelhive.gui.component.SourceCodeEditor.SyntaxStyle;
import pl.gda.pg.eti.kernelhive.gui.component.WorkflowEditor;
import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;
import pl.gda.pg.eti.kernelhive.gui.file.io.FileUtils;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.frame.NewFileDialog;
import pl.gda.pg.eti.kernelhive.gui.frame.NewProjectDialog;
import pl.gda.pg.eti.kernelhive.gui.project.KernelHiveProject;

public class MainFrameController {

	private static final Logger LOG = Logger.getLogger(MainFrameController.class.getName());
	private static ResourceBundle BUNDLE = AppConfiguration.getInstance().getLanguageResourceBundle();
	
	private MainFrame frame;
	private KernelHiveProject project;
	private HashMap<JTabContent, File> openedTabs;
	
	public MainFrameController(MainFrame frame){
		this.frame = frame;
		openedTabs = new HashMap<JTabContent, File>();
	}
	
	public void newProject(){
		
		NewProjectDialog npd = new NewProjectDialog();
		npd.setVisible(true);
		if(npd.getStatus()==NewProjectDialog.APPROVE_OPTION){
			try {
				project = new KernelHiveProject(npd.getProjectDirectory(), npd.getProjectName());
				project.initProject();
				frame.setTitle(npd.getProjectName()+" - "+BUNDLE.getString("MainFrame.this.title"));
				
				//test
				DefaultMutableTreeNode top = new DefaultMutableTreeNode(npd.getProjectName());
				for(int i=0; i<30; i++){
					top.add(new DefaultMutableTreeNode("t1reererererererererererererererererererererer"));
				}
				JTree tree = new JTree(top);
				frame.setProjectTree(tree);
				frame.getProjectScrollPane().setViewportView(frame.getProjectTree());
				//
				
				//TODO load files to treeview
			} catch (ConfigurationException e) {
				JOptionPane.showMessageDialog(frame, 
						BUNDLE.getString("MainFrameController.newProject.cannotCreate.text"), 
						BUNDLE.getString("MainFrameController.newProject.cannotCreate.title"), 
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}
	
	public void openProject(){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setMultiSelectionEnabled(false);
		FileFilter ff = new FileNameExtensionFilter("xml", "xml");
		fc.setFileFilter(ff);
		if(fc.showDialog(frame.getContentPane(), "Select")==JFileChooser.APPROVE_OPTION){
			File file = fc.getSelectedFile();
			KernelHiveProject project = new KernelHiveProject(file.getParent(), null);
			project.load();
			//TODO load files to treeview
			//TODO load graph nodes to workflow composition tab
		}
	}
	
	public void closeProject(){
		
	}
	
	public void saveProject(){
		try {
			project.save();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeTab(JTabPanel tab){
		JTabContent content;
		if(tab!=null){
			content = tab.getPanel();
		} else{
			int index = frame.getWorkspacePane().getSelectedIndex();
			if(index!=-1){
				content = (JTabContent) frame.getWorkspacePane().getComponentAt(index);
			} else{
				return;
			}
		}
		//TODO i18n
		if(content.isDirty()){
			int result = JOptionPane.showConfirmDialog(frame.getContentPane(), 
					"The selected file has been modified. " +
					"Do you wanto to save it?", 
					"Save file?", 
					JOptionPane.YES_NO_CANCEL_OPTION);
			if(result==JOptionPane.YES_OPTION){
				File f = openedTabs.get(content);
				if(f!=null&&f.exists()){
					content.saveContent(f);
				} else{
					NewFileDialog nfd = new NewFileDialog();
					nfd.setVisible(true);
					if(nfd.getStatus()==NewFileDialog.APPROVE_OPTION){
						try {
							f = FileUtils.createNewFile(nfd.getFileDirectory()+
									System.getProperty("file.separator")+
									nfd.getFileName());
							content.saveContent(f);
						} catch (IOException e) {
							LOG.severe("KH: cannot create new file!");
							e.printStackTrace();
						}
					}
				}
				frame.getWorkspacePane().remove(content);
			} else if(result==JOptionPane.NO_OPTION){
				frame.getWorkspacePane().remove(content);
				openedTabs.remove(content);
			} else if(result==JOptionPane.CANCEL_OPTION){
				
			}
		} else{
			frame.getWorkspacePane().remove(content);
			openedTabs.remove(content);
		}
	}
	
	public void openTab(){
		//TODO remove
		SourceCodeEditor sourcePanel = new SourceCodeEditor(frame, "source", SyntaxStyle.CPLUSPLUS);
		frame.getWorkspacePane().add(sourcePanel, 0);
		frame.getWorkspacePane().setTabComponentAt(0, new JTabPanel(sourcePanel, frame.getWorkspacePane()));
		openedTabs.put(sourcePanel, null);
		//
	}
	
	public void saveTab(JTabPanel tab){
		JTabContent content;
		if(tab!=null){
			content = tab.getPanel();
		} else{
			int index = frame.getWorkspacePane().getSelectedIndex();
			content = (JTabContent) frame.getWorkspacePane().getComponentAt(index);
		}
		
		File f = openedTabs.get(content);
		if(f!=null&&f.exists()){
			content.saveContent(f);
		} else{
			NewFileDialog nfd = new NewFileDialog();
			nfd.setVisible(true);
			if(nfd.getStatus()==NewFileDialog.APPROVE_OPTION){
				try {
					f = FileUtils.createNewFile(nfd.getFileDirectory()+
							System.getProperty("file.separator")+
							nfd.getFileName());
					content.saveContent(f);
				} catch (IOException e) {
					LOG.severe("KH: cannot create new file!");
					e.printStackTrace();
				}
			}
		}
	}
	
	public void saveTabAs(JTabPanel tab){
		
	}
	
	public void saveAll(){
		WorkflowEditor editor = new WorkflowEditor(frame, "graph editor");
		frame.getWorkspacePane().add(editor, 0);
		frame.getWorkspacePane().setTabComponentAt(0, new JTabPanel(editor, frame.getWorkspacePane()));
	}
	
	public void refresh(){
		
	}
	
	public void exit(){
		
	}
	
	public void showPreferences(){
		
	}
	
	public void displayToolbox(boolean visible){
		
	}
	
	public void displayStatusbar(boolean visible){
		
	}
	
	public void displaySidePanel(boolean visible){
		
	}
	
	public void showProjectProperties(){
		
	}
}