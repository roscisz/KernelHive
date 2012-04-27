package pl.gda.pg.eti.kernelhive.gui.controller;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.frame.NewProjectDialog;

public class MainFrameController {

	private MainFrame frame;
	
	public MainFrameController(MainFrame frame){
		this.frame = frame;
	}
	
	
	public void newProject(){
		NewProjectDialog npd = new NewProjectDialog();
		npd.setVisible(true);
		if(npd.getStatus()==NewProjectDialog.APPROVE_OPTION){
			//TODO
		}
	}
	
	public void openProject(){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setMultiSelectionEnabled(false);
		FileFilter ff = new FileNameExtensionFilter("project", "project");
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
