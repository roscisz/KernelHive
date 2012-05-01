package pl.gda.pg.eti.kernelhive.gui.controller;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;
import pl.gda.pg.eti.kernelhive.gui.file.io.FileUtils;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.frame.NewProjectDialog;
import pl.gda.pg.eti.kernelhive.gui.project.KernelHiveProject;

public class MainFrameController {

	private static final Logger LOG = Logger.getLogger(MainFrameController.class.getName());
	private static ResourceBundle BUNDLE = AppConfiguration.getInstance().getLanguageResourceBundle();
	
	private MainFrame frame;
	private KernelHiveProject project;
	
	public MainFrameController(MainFrame frame){
		this.frame = frame;
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
		
	}
	
	public void closeTab(){
		
	}
	
	public void openTab(){
		
	}
}
