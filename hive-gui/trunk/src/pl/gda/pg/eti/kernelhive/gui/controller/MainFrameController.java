package pl.gda.pg.eti.kernelhive.gui.controller;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

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
				File projectFile = FileUtils.createNewFile(npd.getProjectDirectory()+
						System.getProperty("file.separator")+
						npd.getProjectName()+
						System.getProperty("file.separator")+
						".project");
				project = new KernelHiveProject(projectFile);	
				frame.setTitle(npd.getProjectName()+" - "+BUNDLE.getString("MainFrame.this.title"));
				//TODO load files to treeview
			} catch (IOException e) {
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
		FileFilter ff = new FileNameExtensionFilter("project", "project");
		fc.setFileFilter(ff);
		if(fc.showDialog(frame.getContentPane(), "Select")==JFileChooser.APPROVE_OPTION){
			File file = fc.getSelectedFile();
		}
	}
	
	public void closeProject(){
		
	}
	
	public void saveProject(){
		
	}
	
	public void closeTab(){
		
	}	
}
