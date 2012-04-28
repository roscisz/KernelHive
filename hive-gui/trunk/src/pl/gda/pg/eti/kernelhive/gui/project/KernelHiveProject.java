package pl.gda.pg.eti.kernelhive.gui.project;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class KernelHiveProject implements Serializable {

	private static final long serialVersionUID = -4797108604024696381L;
	private static final Logger LOG = Logger.getLogger(KernelHiveProject.class.getName());
	
	private PropertiesConfiguration config;
	private String name;
	private File projectFile;
	private List<File> files = new ArrayList<File>();
	
	public KernelHiveProject(File projectFile){
		this.projectFile = projectFile;
		try{
			config = new PropertiesConfiguration(this.projectFile);
		} catch(ConfigurationException e){
			LOG.severe("Cannot open configuration file!");
			e.printStackTrace();
		}
	}
	
	private void updateProjectName() throws ConfigurationException{
		//TODO update project name in catalog structure
		config.setProperty("project.name", this.name);
		config.save();
	}
	
	private void updateFilePaths() throws ConfigurationException{
		List<String> filePaths = new ArrayList<String>();
		for(File file : files){
			filePaths.add(file.getPath());
		}
		config.setProperty("file.paths", filePaths);
		config.save();
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public List<File> getFiles(){
		return files;
	}
	
	public void setFiles(List<File> files){
		this.files = files;
	}
	
	public boolean addFile(File f){
		if(!files.contains(f)){
			return files.add(f);
		} else{
			return false;
		}
	}
	
	public boolean removeFile(File f, boolean removeFromDisc){
		boolean result = false;
		if(files.contains(f)){
			result = files.remove(f);
			if(removeFromDisc){
				result &= f.delete();
			}
		}
		return result;
	}
	
	public boolean save(){
		try {
			updateProjectName();
			updateFilePaths();
			//TODO what else to save?
			return true;
		} catch (ConfigurationException e) {
			LOG.severe("KH: cannot save to project configuration file!");
			e.printStackTrace();
			return false;
		}		
	}
	
	public void load(){
		name = config.getString("project.name");
		List<Object> list = config.getList("file.paths");
		files = new ArrayList<File>();
		for(Object o : list){
			files.add(new File((String)o));
		}
	}
}
